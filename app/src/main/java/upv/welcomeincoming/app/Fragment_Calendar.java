package upv.welcomeincoming.app;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.util.CompatibilityHints;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import calendarupv.CalendarICAL;
import calendarupv.Calendario;
import calendarupv.Evento;
import intranet.IntranetConnection;
import intranet.OutPutParamsIntranetConnection;
import util.Preferencias;
import util.ProgressDialog_Custom;

public class Fragment_Calendar extends ListFragment implements Observer {

    public interface CalendarListener {
        public void CalendarListenerError(String string);
    }

    private IntranetConnection intranetConnection;
    private ProgressDialog_Custom progressDialog;
    private Calendario calendario;
    private CalendarICAL calendarICAL;
    private List<Evento> eventos;
    private TextView textViewName;
    private TextView textViewGroup;
    private TextView textViewInfo;
    private TextView textViewEventos;
    private ListView listView;
    private SQLiteDatabase db;
    private ArrayAdapterCalendarDiaryItemList adapter;
    boolean existe = false;

    public Fragment_Calendar(Calendario calendario, boolean existe, SQLiteDatabase db) {
        this.calendario = calendario;
        this.existe = existe;
        this.db = db;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            String string = data.getStringExtra("RESULT_STRING");
            this.adapter = new ArrayAdapterCalendarDiaryItemList(this.getActivity(), obtenerEventosPorAsignatura(string));
            this.setListAdapter(adapter);
            textViewName.setText(this.calendario.getNombre());
        }
        if (resultCode == 2) {
            String string = data.getStringExtra("RESULT_STRING");
            try {
                this.adapter = new ArrayAdapterCalendarDiaryItemList(this.getActivity(), obtenerEventosPorFecha(string));
                this.setListAdapter(adapter);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            textViewName.setText(this.calendario.getNombre());
        }

    }

    private List<Evento> obtenerEventosPorAsignatura(String asignatura) {
        String sql = "SELECT * FROM Evento WHERE nombre=\"" + asignatura + "\" and idHorario =\"" + calendario.getUid() + "\";";
        Cursor cursor = db.rawQuery(sql, null);
        List<Evento> eventos = new ArrayList<Evento>();
        while (cursor.moveToNext()) {
            Evento evento = new Evento(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6));
            eventos.add(evento);
        }
        cursor.close();
        return eventos;
    }

    private List<Evento> obtenerEventosPorFecha(String date) throws ParseException {
        String sql = "SELECT * FROM Evento WHERE idHorario =\"" + calendario.getUid() + "\";";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Cursor cursor = db.rawQuery(sql, null);
        List<Evento> eventos = new ArrayList<Evento>();
        while (cursor.moveToNext()) {
            Evento evento = new Evento(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6));
            String eDate = evento.getFecha().substring(0, 11).trim();
            Log.e("fechas", eDate.toString());
            Log.e("fechas", date.toString());
            Log.e("fechas", eDate.equals(date) + "");
            if (date.equals(eDate))
                eventos.add(evento);
        }
        cursor.close();
        return eventos;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textViewName = (TextView) this.getView().findViewById(R.id.textViewCalendarName);

        listView = this.getListView();

        if (existe) {
            this.eventos = calendario.obtenerEventosDB(db);
            this.adapter = new ArrayAdapterCalendarDiaryItemList(this.getActivity(), eventos);
            this.setListAdapter(adapter);
            textViewName.setText(this.calendario.getNombre());
        } else {
            progressDialog = new ProgressDialog_Custom(getActivity(), getString(R.string.downloading));
            progressDialog.getWindow().setGravity(Gravity.BOTTOM);
            progressDialog.show();
            intranetConnection = new IntranetConnection(
                    Preferencias.getDNI(this.getActivity()),
                    Preferencias.getPIN(this.getActivity()),
                    this
            );

            progressDialog.setMessage(getString(R.string.connecting));
            intranetConnection.connect();
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_filtro, menu);
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case R.id.actualizar_action:
                progressDialog = new ProgressDialog_Custom(getActivity(), getString(R.string.loading));
                progressDialog.getWindow().setGravity(Gravity.BOTTOM);
                progressDialog.show();
                borrarEventosDB(calendario.getUid());
                //internet

                intranetConnection = new IntranetConnection(
                        Preferencias.getDNI(this.getActivity()),
                        Preferencias.getPIN(this.getActivity()),
                        this
                );
                progressDialog.setMessage(getString(R.string.connecting));
                intranetConnection.connect();
                return true;
            case R.id.menuSortAsignaturas:
                Intent filtro = new Intent(getActivity(), Activity_Filtros.class);
                startActivityForResult(filtro, 1);
                break;
            case R.id.menuSortFecha:
                Intent filtro2 = new Intent(getActivity(), Activity_Filtros_Fecha.class);
                startActivityForResult(filtro2, 2);
                break;
            case R.id.menuDeleteSo:
                this.adapter = new ArrayAdapterCalendarDiaryItemList(this.getActivity(), eventos);
                this.setListAdapter(adapter);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void borrarEventosDB(String uid) {
        db.execSQL("DELETE FROM Evento WHERE idHorario=\"" + uid + "\"");
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


            if (outPutParamsIntranetConnection.isUserFail()) {
                Preferencias.setDNI(this.getActivity(), "");
                Preferencias.setPIN(this.getActivity(), "");
            }

            CalendarListener activity = (CalendarListener) getActivity();
            activity.CalendarListenerError(outPutParamsIntranetConnection.getException().getMessage());

            return;
        }
        if (data.equals("login")) {

            progressDialog.setMessage(getString(R.string.downloading));

            intranetConnection.getICS(this.calendario.getUrl());

        } else if (data.equals("ics")) {
            try {
                actualizarEventos(outPutParamsIntranetConnection);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserException e) {
                e.printStackTrace();
            }
        }

    }

    private void actualizarEventos(OutPutParamsIntranetConnection outPutParamsIntranetConnection) throws IOException, ParserException {
        //parsear ical
        CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, true);
        StringReader sin = new StringReader(outPutParamsIntranetConnection.getIcsString());
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = builder.build(sin);
        calendarICAL = new CalendarICAL(calendar, this.calendario.getUid());

        textViewName.setText(this.calendario.getNombre());

        if (calendarICAL.getEvents().size() > 0) {
            calendarICAL.insertarCalendariosDB(db);
            this.eventos = calendarICAL.getEvents();
            this.adapter = new ArrayAdapterCalendarDiaryItemList(this.getActivity(), eventos);
            this.setListAdapter(adapter);

        } else {
            textViewEventos.setText(getString(R.string.noevents));
        }

        if (progressDialog != null)
            progressDialog.dismiss();
    }

    private class ArrayAdapterCalendarDiaryItemList extends BaseAdapter {

        private Context context;
        private List<Evento> values;
        private int visibleValues = 20;

        public ArrayAdapterCalendarDiaryItemList(Context context, List<Evento> values) {
            this.context = context;
            this.values = values;
        }

        public List<Evento> getValues() {
            return values;
        }

        public void setValues(List<Evento> values) {
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
        public Object getItem(int pos) {
            return values.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.fragment_calendar_show_calendar_item, parent, false);

            LinearLayout layEdif = (LinearLayout) rowView.findViewById(R.id.layoutEdificio);
            LinearLayout layEdifL = (LinearLayout) rowView.findViewById(R.id.layoutEdificioL);
            LinearLayout layEdifE = (LinearLayout) rowView.findViewById(R.id.layoutExtEdifi);
            TextView firstLine = (TextView) rowView.findViewById(R.id.calendarFirstLine);
            TextView secondLineLeft = (TextView) rowView.findViewById(R.id.calendarSecondLineLeft);
            TextView thirdLineRight = (TextView) rowView.findViewById(R.id.calendarThirdLine);


            if (values.get(position).getNombre() != null) {
                firstLine.setText(values.get(position).getNombre());
            }

            if (values.get(position).getUbicacion() != null)
                secondLineLeft.setText(values.get(position).getUbicacion());

            if (values.get(position).getFecha() != null)
                thirdLineRight.setText(values.get(position).getFecha());
            String edificio = values.get(position).getEdificio();
            if (edificio != null
                    && edificio.equals("1B") || edificio.equals("1C") || edificio.equals("1G")
                    || edificio.equals("1H") || edificio.equals("2F")
                    || edificio.equals("3H") || edificio.equals("3M")
                    || edificio.equals("3P") || edificio.equals("4D")
                    || edificio.equals("4H") || edificio.equals("5F")
                    || edificio.equals("7B") || edificio.equals("7I")
                    || edificio.equals("7J")) {
                final int positionV = position;
                Button fourthLineRight = new Button(getActivity());
                fourthLineRight.setText(values.get(position).getEdificio());
                fourthLineRight.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                fourthLineRight.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                fourthLineRight.setPadding(10, 0, 10, 0);
                fourthLineRight.setBackgroundResource(R.drawable.selector_listarosaminimalista);
                fourthLineRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Acvitity_VistaRealidadAumentada.class);
                        String edificio = values.get(positionV).getEdificio();
                        intent.putExtra("poi", edificio);
                        startActivity(intent);
                    }
                });
                layEdif.addView(fourthLineRight);
            } else layEdifE.removeView(layEdifL);

            return rowView;
        }
    }
}