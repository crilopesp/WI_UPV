package upv.welcomeincoming.app;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.util.CompatibilityHints;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import CalendarUPV.CalendarICAL;
import CalendarUPV.DiaryJSON;
import CalendarUPV.EventICAL;
import Intranet.IntranetConnection;
import Intranet.OutPutParamsIntranetConnection;
import util.Preferences;

public class FragmentCalendar extends ListFragment implements Observer {

    public interface CalendarListener{
        public void CalendarListenerError(String string);
    }

    private IntranetConnection intranetConnection;
    private ProgressDialog progressDialog;
    private DiaryJSON diaryJSON;
    private CalendarICAL calendarICAL;

    private TextView textViewName;
    private TextView textViewGroup;
    private TextView textViewInfo;
    private TextView textViewEventos;
    private ListView listView;

    private ArrayAdapterCalendarDiaryItemList adapter;

    public FragmentCalendar(DiaryJSON diaryJSON) {
        this.diaryJSON = diaryJSON;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textViewName = (TextView)this.getView().findViewById(R.id.textViewCalendarName);
        textViewGroup = (TextView)this.getView().findViewById(R.id.textViewCalendarGroup);
        textViewInfo = (TextView)this.getView().findViewById(R.id.textViewCalendarInfo);
        textViewEventos = (TextView)this.getView().findViewById(R.id.textViewEvents);

        listView = this.getListView();

        progressDialog = ProgressDialog.show(this.getActivity(), "Loading...", "", true);

        intranetConnection = new IntranetConnection(
                Preferences.getUser(this.getActivity()),
                Preferences.getPass(this.getActivity()),
                this
        );

        progressDialog.setMessage("Connecting to UPV");
        intranetConnection.connect();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar_show_calendar, container, false);
    }

    @Override
    public void update(Observable observable, Object data) {

        OutPutParamsIntranetConnection outPutParamsIntranetConnection = (OutPutParamsIntranetConnection) observable;

        //error?
        if (outPutParamsIntranetConnection.getException() != null) {

            Log.d(((Object) this).getClass().getName(), outPutParamsIntranetConnection.getException().getMessage());

            if (outPutParamsIntranetConnection.isUserFail()) {
                Preferences.setUser(this.getActivity(), "");
                Preferences.setPass(this.getActivity(), "");
            }

            CalendarListener activity = (CalendarListener) getActivity();
            activity.CalendarListenerError(outPutParamsIntranetConnection.getException().getMessage());

            return;
        }

        if (data.equals("login")) {

            progressDialog.setMessage(getString(R.string.downloading));

            intranetConnection.getICS(this.diaryJSON.getUrl());

        } else if (data.equals("ics")) {

            Log.w(((Object) this).getClass().getName(), "update -> ics");

            try {

                //parsear ical
                CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, true);
                StringReader sin = new StringReader(outPutParamsIntranetConnection.getIcsString());
                CalendarBuilder builder = new CalendarBuilder();
                Calendar calendar = builder.build(sin);

                calendarICAL = new CalendarICAL(calendar,this.diaryJSON.getUid());

                Log.w(((Object) this).getClass().getName(), "Objecto ->" + this.diaryJSON);

                Log.w(((Object) this).getClass().getName(), "Objecto ->" + calendarICAL);

                textViewName.setText(this.diaryJSON.getNombre());
                textViewGroup.setText(this.diaryJSON.getGrupo());

                if(calendarICAL.getEvents().size()>0) {

                    this.adapter = new ArrayAdapterCalendarDiaryItemList(this.getActivity(),calendarICAL.getEvents());
                    this.adapter.setVisibleValues(calendarICAL.getEvents().size() > 20 ? 20 : calendarICAL.getEvents().size()-1);
                    this.setListAdapter(adapter);

                    listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {}

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                            if(firstVisibleItem + visibleItemCount >= totalItemCount) {
                                adapter.setVisibleValues(adapter.getVisibleValues()+visibleItemCount > calendarICAL.getEvents().size()-1 ? calendarICAL.getEvents().size()-1 : adapter.getVisibleValues()+visibleItemCount);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });


                }else{
                    textViewEventos.setText("There is no events");
                }

                if(progressDialog!=null)
                    progressDialog.dismiss();

            } catch (Exception e) {
                Log.w(((Object) this).getClass().getName(), "Exception", e);
            }

        } else {
            Log.w(((Object) this).getClass().getName(), "Yo no tengo que salir!");
        }
    }
    private class ArrayAdapterCalendarDiaryItemList extends BaseAdapter {

        private Context context;
        private List<EventICAL> values;
        private int visibleValues = 20;

        public ArrayAdapterCalendarDiaryItemList(Context context, List<EventICAL> values){
            this.context=context;
            this.values=values;
        }

        public List<EventICAL> getValues() {
            return values;
        }

        public void setValues(List<EventICAL> values) {
            this.values = values;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public int getVisibleValues() {
            return visibleValues;
        }

        public void setVisibleValues(int visibleValues) {
            this.visibleValues = visibleValues;
        }

        private final SimpleDateFormat simpleDateFormatOriginal = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault());

        @Override
        public Object getItem(int pos) { return values.get(pos); }

        @Override
        public long getItemId(int pos) { return pos; }

        @Override
        public int getCount() { return values.size(); }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.fragment_calendar_show_calendar_item, parent, false);

            Log.e("fecha", "" + values.get(position).getDtstart());
            LinearLayout layEdif = (LinearLayout) rowView.findViewById(R.id.layoutEdificio);
            LinearLayout layEdifL = (LinearLayout) rowView.findViewById(R.id.layoutEdificioL);
            LinearLayout layEdifE = (LinearLayout) rowView.findViewById(R.id.layoutExtEdifi);
            TextView firstLine = (TextView) rowView.findViewById(R.id.calendarFirstLine);
            TextView secondLineLeft = (TextView) rowView.findViewById(R.id.calendarSecondLineLeft);
            TextView thirdLineRight = (TextView) rowView.findViewById(R.id.calendarThirdLine);


            if(values.get(position).getDescription()!=null) {
                firstLine.setText(values.get(position).getDescription());
            }

            if(values.get(position).getLocation()!=null)
                secondLineLeft.setText(values.get(position).getLocation());

            if(values.get(position).getDtstartFormat()!=null)
                thirdLineRight.setText(values.get(position).getDtstartFormat());
            String edificio = values.get(position).getBuilding();
            if(edificio!=null
                    && edificio.equals("1B") || edificio.equals("1C") || edificio.equals("1G")
                    || edificio.equals("1H") ||edificio.equals("2F")
                    ||edificio.equals("3H") ||edificio.equals("3M")
                    || edificio.equals("3P") ||edificio.equals("4D")
                    || edificio.equals("4H") ||edificio.equals("5F")
                    || edificio.equals("7B") ||edificio.equals("7I")
                    || edificio.equals("7J")){
                final int positionV = position;
                Button fourthLineRight = new Button(getActivity());
                fourthLineRight.setText(values.get(position).getBuilding());
                fourthLineRight.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                fourthLineRight.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                fourthLineRight.setPadding(10, 0, 10, 0);
                fourthLineRight.setBackgroundResource(R.drawable.button_custom_grey);
                fourthLineRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ARViewActivity.class);
                        String edificio = values.get(positionV).getBuilding();
                        intent.putExtra("poi", edificio);
                        startActivity(intent);
                    }
                });
                layEdif.addView(fourthLineRight);
            }
            else layEdifE.removeView(layEdifL);

            return rowView;
        }
    }

}