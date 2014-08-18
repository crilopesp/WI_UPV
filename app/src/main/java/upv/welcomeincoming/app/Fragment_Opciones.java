package upv.welcomeincoming.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.Locale;

import upv.welcomeincoming.app.calendarAlarm.AlarmReceiver;
import util.DBHandler_Horarios;
import util.InternetConnectionChecker;
import util.Preferencias;


/**
 * Created by Marcos on 3/05/14.
 */
public class Fragment_Opciones extends Fragment {

    private LinearLayout linearLista;
    private LayoutInflater inflater;
    InternetConnectionChecker icc;
    ShowcaseView sv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        this.inflater = inflater;

        icc = new InternetConnectionChecker();
        linearLista = (LinearLayout) view.findViewById(R.id.linearOpciones);


        //Language
        View itemLanguage = generateItemLanguage();
        linearLista.addView(itemLanguage);
        addDividier();
        //Sesion
        final View itemSesion = generateItemSesion(getString(R.string.sesion));
        int idItemSesion = 213;
        itemSesion.setId(R.id.sesion_opciones);
        itemSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sv != null)
                    sv.hide();
                if (Preferencias.getDNI(getActivity()) == "") {
                    if (icc.checkInternetConnection(getActivity())) {
                        Intent i = new Intent(getActivity(), Activity_login.class);
                        startActivity(i);
                    } else {
                        Intent intent = new Intent(getActivity(), Activity_no_connection.class);
                        intent.putExtra("case", getString(R.string.noInetBlock));
                        startActivity(intent);
                    }
                } else {
                    Preferencias.setDNI(getActivity(), "");
                    Preferencias.setPIN(getActivity(), "");
                    borrarDatosUsuario();
                    TextView tvi = (TextView) itemSesion.findViewById(R.id.textSesionInfo);
                    TextView tvv = (TextView) itemSesion.findViewById(R.id.textSesionValue);
                    tvi.setText(getString(R.string.nosesion));
                    tvv.setText(getString(R.string.login));
                }
                //Intent intentSesion = new Intent(getActivity(),SesionActivity.class);
            }
        });
        linearLista.addView(itemSesion);
        addDividier();

        //Alertas
        final View itemAlertas = generateItemAlerta(getString(R.string.alertaCalendar), "Calendar");
        itemAlertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferencias.setForumAlerts(getActivity(), false);
            }
        });
        linearLista.addView(itemAlertas);
        addDividier();

        View itemAlertaForum = generateItemAlerta(getString(R.string.alertaForum), "Forum");
        itemAlertaForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferencias.setForumAlerts(getActivity(), false);
            }
        });
        linearLista.addView(itemAlertaForum);
        addDividier();

        //Perfil
        View itemPerfil = inflater.inflate(R.layout.item_settings_perfil, null);
        itemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPerfil = new Intent(getActivity(), Activity_Profile.class);
                startActivity(intentPerfil);
            }
        });
        linearLista.addView(itemPerfil);
        addDividier();
        //About
        View itemAbout = inflater.inflate(R.layout.item_settings_acerca, null);

        itemAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAbout = new Intent(getActivity(), Activity_Acercade.class);
                startActivity(intentAbout);
            }
        });
        linearLista.addView(itemAbout);
        addDividier();

        return view;

    }

    private View generateItemSesion(String string) {
        View item = inflater.inflate(R.layout.item_settings_sesion, null);
        TextView tvi = (TextView) item.findViewById(R.id.textSesionInfo);
        TextView tvv = (TextView) item.findViewById(R.id.textSesionValue);
        if (Preferencias.getDNI(getActivity()) == "") {
            tvi.setText(getString(R.string.nosesion));
            tvv.setText(getString(R.string.login));
            tvv.setBackgroundResource(R.drawable.selector_button_back);
        } else {
            tvi.setText(getString(R.string.sisesion));
            tvv.setText(Preferencias.getNombre(getActivity()) + " " + Preferencias.getApellidos(getActivity()));
        }
        ViewTarget target = new ViewTarget(tvv);
        if (Preferencias.getFirstOptions(getActivity()) == 1) {
            sv = new ShowcaseView.Builder(getActivity())
                    .setTarget(target)
                    .setContentTitle(R.string.tutorial_options_title)
                    .setContentText(R.string.tutorial_options_message)
                    .setStyle(R.style.ShowCaseTheme)
                    .build();
            Preferencias.setFirstOptions(getActivity(), 0);
        }
        return item;
    }

    private View generateItemAlerta(String string, final String tipo) {
        View viewAlerta = inflater.inflate(R.layout.item_settings_alerta, null);
        TextView txtAlerta = (TextView) viewAlerta.findViewById(R.id.textAlerta);
        txtAlerta.setText(string);
        CheckBox check = (CheckBox) viewAlerta.findViewById(R.id.checkAlertCalendar);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (tipo.equals("Calendar")) {

                        Preferencias.setCalendarAlerts(getActivity(), true);
                        startService();
                    } else if (tipo.equals("Forum")) {
                    }
                } else if (!b) {
                    if (tipo.equals("Calendar")) {

                    } else if (tipo.equals("Forum")) {
                        Preferencias.setCalendarAlerts(getActivity(), false);
                        stopService();
                    }
                }
            }
        });

        return viewAlerta;
    }

    private View generateItemLanguage() {
        final View viewSpinner = inflater.inflate(R.layout.item_settings_idioma, null);
        final Spinner spinner = (Spinner) viewSpinner.findViewById(R.id.options_lenguage_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.lenguages, R.layout.item_lista_spinner);
        adapter.setDropDownViewResource(R.layout.item_lista_spinner);
        spinner.setAdapter(adapter);
        Locale locale1 = getActivity().getApplicationContext().getResources().getConfiguration().locale;
        if (locale1.getCountry().equals("ES")) spinner.setSelection(1);
        else if (locale1.getCountry().equals("US")) spinner.setSelection(0);

        ImageButton btn_accept = (ImageButton) viewSpinner.findViewById(R.id.btn_accept);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner.getSelectedItemPosition() == 0) {
                    Locale locale = new Locale("en_US");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getActivity().getApplicationContext().getResources().updateConfiguration(config, null);
                    getActivity().recreate();
                } else if (spinner.getSelectedItemPosition() == 1) {
                    Locale locale = new Locale("es_ES");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getActivity().getApplicationContext().getResources().updateConfiguration(config, null);
                    getActivity().recreate();
                }
            }
        });
        return viewSpinner;
    }

    private void addDividier() {
        View v = inflater.inflate(R.layout.dividier_itemdrawer, null);
        linearLista.addView(v);
    }

    //start the service
    public void startService() {
        //start the service from here //MyService is your service class name
        getActivity().startService(new Intent(getActivity(), AlarmReceiver.class));
    }

    //Stop the started service
    public void stopService() {
        //Stop the running service from here//MyService is your service class name
        //Service will only stop if it is already running.
        getActivity().stopService(new Intent(getActivity(), AlarmReceiver.class));
    }

    private void borrarDatosUsuario() {
        SQLiteDatabase db = new DBHandler_Horarios(getActivity()).getWritableDatabase();
        db.execSQL("DELETE FROM Evento");
        db.execSQL("DELETE FROM Horario");
    }
}