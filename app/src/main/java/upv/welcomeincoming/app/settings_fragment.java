package upv.welcomeincoming.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import util.Preferences;


/**
 * Created by Marcos on 3/05/14.
 */
public class settings_fragment extends Fragment {

    private LinearLayout linearLista;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        this.inflater = inflater;


        linearLista = (LinearLayout) view.findViewById(R.id.linearOpciones);


        //Language
        View itemLanguage = generateItemLanguage();
        linearLista.addView(itemLanguage);
        addDividier();
        //Sesion
        final View itemSesion = generateItemSesion(getString(R.string.sesion));
        itemSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Preferences.getDNI(getActivity()) == "") {
                    DialogLoginSettings fragmentCalendarLogin = new DialogLoginSettings();
                    fragmentCalendarLogin.show(getFragmentManager(), getString(R.string.login));
                } else {
                    Preferences.setDNI(getActivity(), "");
                    Preferences.setPIN(getActivity(), "");
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
        View itemAlertas = generateItemAlerta(getString(R.string.alertaCalendar));
        itemAlertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intentAlerta = new Intent(getActivity(),AlertaActivity.class);
            }
        });
        linearLista.addView(itemAlertas);
        addDividier();

        View itemAlertaForum = generateItemAlerta(getString(R.string.alertaForum));
        itemAlertaForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intentAlerta = new Intent(getActivity(),AlertaActivity.class);
            }
        });
        linearLista.addView(itemAlertaForum);
        addDividier();

        //Perfil
        View itemPerfil = inflater.inflate(R.layout.option_item_profile, null);
        itemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intentAlerta = new Intent(getActivity(),AlertaActivity.class);
            }
        });
        linearLista.addView(itemPerfil);
        addDividier();
        //About
        View itemAbout = inflater.inflate(R.layout.option_item_about, null);

        itemAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intentAlerta = new Intent(getActivity(),AlertaActivity.class);
            }
        });
        linearLista.addView(itemAbout);
        addDividier();


        return view;

    }

    private View generateItemSesion(String string) {
        View item = inflater.inflate(R.layout.option_item_session, null);
        TextView tvi = (TextView) item.findViewById(R.id.textSesionInfo);
        TextView tvv = (TextView) item.findViewById(R.id.textSesionValue);
        if (Preferences.getDNI(getActivity()) == "") {
            tvi.setText(getString(R.string.nosesion));
            tvv.setText(getString(R.string.login));
        } else {
            tvi.setText(getString(R.string.sisesion));
            tvv.setText(Preferences.getDNI(getActivity()));
        }
        return item;
    }

    private View generateItemAlerta(String string) {
        View viewAlerta = inflater.inflate(R.layout.option_item_alerta, null);
        TextView txtAlerta = (TextView) viewAlerta.findViewById(R.id.textAlerta);
        txtAlerta.setText(string);
        return viewAlerta;
    }

    private View generateItemLanguage() {
        View viewSpinner = inflater.inflate(R.layout.option_item_lenguage, null);
        Spinner spinner = (Spinner) viewSpinner.findViewById(R.id.options_lenguage_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.lenguages, R.layout.row_spinner_language);
        adapter.setDropDownViewResource(R.layout.row_spinner_language);
        spinner.setAdapter(adapter);
        return viewSpinner;
    }

    private void addDividier() {
        View v = inflater.inflate(R.layout.dividier_itemdrawer, null);
        linearLista.addView(v);
    }
}