package upv.welcomeincoming.app;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import calendarupv.Calendario;
import intranet.IntranetConnection;
import intranet.OutPutParamsIntranetConnection;
import util.Preferencias;
import util.ProgressDialog_Custom;


public class Fragment_Diary extends ListFragment implements Observer {

    public interface DiaryListener {
        public void DiaryListenerOnClick(Calendario diaryJSON);

        public void DiaryListenerError(String string);
    }

    private SQLiteDatabase db;
    private List<Calendario> diaryJSONList;
    private ProgressDialog_Custom progressDialog;
    private IntranetConnection intranetConnection;
    private DiaryListener diaryListener;

    public Fragment_Diary(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        if (!estaVaciaDB()) {
            progressDialog = new ProgressDialog_Custom(this.getActivity(), getString(R.string.loading));
            progressDialog.getWindow().setGravity(Gravity.BOTTOM);
            progressDialog.show();
            //internet

            intranetConnection = new IntranetConnection(
                    Preferencias.getDNI(this.getActivity()),
                    Preferencias.getPIN(this.getActivity()),
                    this
            );
            progressDialog.setMessage(getString(R.string.connecting));
            intranetConnection.connect();
        } else {
            diaryJSONList = obtenerCalendarios();
            ArrayAdapterCalendarDiaryList adapter = new ArrayAdapterCalendarDiaryList(this.getActivity(), diaryJSONList);
            setListAdapter(adapter);
        }

    }

    private List<Calendario> obtenerCalendarios() {
        String sql = "SELECT * FROM Horario;";
        Cursor cursor = db.rawQuery(sql, null);
        List<Calendario> calendarios = new ArrayList<Calendario>();
        while (cursor.moveToNext()) {
            Calendario calendario = new Calendario(cursor.getString(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("Nombre")), cursor.getString(cursor.getColumnIndex("Url")));
            calendarios.add(calendario);
        }
        cursor.close();
        return calendarios;
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


            diaryJSONList = outPutParamsIntranetConnection.getCalendars().getCalendarios();
            Iterator<Calendario> itCal = diaryJSONList.iterator();
            Calendario calendario = null;
            while (itCal.hasNext()) {
                calendario = itCal.next();
                calendario.insertarCalendariosDB(db);
            }
            ArrayAdapterCalendarDiaryList adapter = new ArrayAdapterCalendarDiaryList(this.getActivity(), diaryJSONList);
            setListAdapter(adapter);

        } else {
        }


    }


    private class ArrayAdapterCalendarDiaryList extends ArrayAdapter<Calendario> {
        private final Context context;
        private final List<Calendario> values;

        public ArrayAdapterCalendarDiaryList(Context context, List<Calendario> values) {
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

    public boolean estaVaciaDB() {
        String sql = "SELECT * FROM Horario;";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}