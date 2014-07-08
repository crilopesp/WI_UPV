package upv.welcomeincoming.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import calendarupv.DiaryJSON;
import intranet.IntranetConnection;
import intranet.OutPutParamsIntranetConnection;
import util.Preferencias;


public class Fragment_Diary extends ListFragment implements Observer {

    public interface DiaryListener {
        public void DiaryListenerOnClick(DiaryJSON diaryJSON);

        public void DiaryListenerError(String string);
    }

    private List<DiaryJSON> diaryJSONList;
    private ProgressDialog progressDialog;
    private IntranetConnection intranetConnection;
    private DiaryListener diaryListener;

    public Fragment_Diary() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        progressDialog = ProgressDialog.show(this.getActivity(), getString(R.string.loading), "", true);
        //internet

        intranetConnection = new IntranetConnection(
                Preferencias.getDNI(this.getActivity()),
                Preferencias.getPIN(this.getActivity()),
                this
        );
        progressDialog.setMessage(getString(R.string.connecting));
        intranetConnection.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar_show_diary, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        this.diaryListener.DiaryListenerOnClick(diaryJSONList.get(position));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.diaryListener = (DiaryListener) activity;
        } catch (ClassCastException e) {
        }
    }

    @Override
    public void update(Observable observable, Object data) {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        OutPutParamsIntranetConnection outPutParamsIntranetConnection = (OutPutParamsIntranetConnection) observable;

        //error?
        if (outPutParamsIntranetConnection.getException() != null) {


            if (outPutParamsIntranetConnection.isUserFail()) {
                Preferencias.setDNI(this.getActivity(), "");
                Preferencias.setPIN(this.getActivity(), "");
            }

            DiaryListener activity = (DiaryListener) getActivity();
            activity.DiaryListenerError(outPutParamsIntranetConnection.getException().getMessage());

            return;
        }

        //no hay error...
        if (data.equals("login")) {


            progressDialog.setMessage(getString(R.string.obtainCalendars));
            intranetConnection.getCalendars();

        } else if (data.equals("calendars")) {


            diaryJSONList = outPutParamsIntranetConnection.getCalendars().getDiaries();
            ArrayAdapterCalendarDiaryList adapter = new ArrayAdapterCalendarDiaryList(this.getActivity(), diaryJSONList);
            setListAdapter(adapter);

        } else {
        }
    }

    private class ArrayAdapterCalendarDiaryList extends ArrayAdapter<DiaryJSON> {
        private final Context context;
        private final List<DiaryJSON> values;

        public ArrayAdapterCalendarDiaryList(Context context, List<DiaryJSON> values) {
            super(context, R.layout.fragment_calendar_show_diary_item, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.fragment_calendar_show_diary_item, parent, false);

            TextView name = (TextView) rowView.findViewById(R.id.firstLine);

            name.setText(values.get(position).getNombre());

            return rowView;
        }
    }

}