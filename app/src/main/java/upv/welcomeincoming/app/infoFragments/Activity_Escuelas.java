package upv.welcomeincoming.app.infoFragments;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import upv.welcomeincoming.app.R;
import util.Escuela;
import util.ExpandableListAdapter_Asignaturas;
import util.Parser_XML;

/**
 * Created by Marcos on 30/04/14.
 */
public class Activity_Escuelas extends Activity {

    ExpandableListAdapter_Asignaturas listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info_escuelas);

        expListView = (ExpandableListView) findViewById(R.id.expandableListView);

        // preparing list data
        prepareListData();
        String[] _nombres = {getString(R.string.urlP), getString(R.string.urlD), getString(R.string.mail), getString(R.string.fax), getString(R.string.nomCoor), getString(R.string.mailCoor), getString(R.string.telfCoor), getString(R.string.nomTec), getString(R.string.mailTec), getString(R.string.telfTec), getString(R.string.extTec)};

        listAdapter = new ExpandableListAdapter_Asignaturas(this, listDataHeader, listDataChild, _nombres);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        InputStream fichero = this.getResources().openRawResource(R.raw.telefonos);
        Parser_XML parseador = new Parser_XML("telefonos");
        List<Escuela> lista = parseador.parseando(fichero);
        String mostrar = "";
        for (int i = 0; i < lista.size(); i++) {
            Escuela escuela = lista.get(i);
            listDataHeader.add(escuela.getEscuelanombre());
            List<String> info = new ArrayList<String>();
            info.add(escuela.getUrlprincipal());
            info.add(escuela.getUrldocencia());
            info.add(escuela.getEscuelamail());
            info.add(escuela.getEscuelafax());
            info.add(escuela.getCoordinadornombre());
            info.add(escuela.getCoordinadormail());
            info.add(escuela.getCoordinadortelefono());
            info.add(escuela.getTecniconombre());
            info.add(escuela.getTecnicomail());
            info.add(escuela.getTecnicotelefono());
            info.add(escuela.getTecnicoextension());
            listDataChild.put(listDataHeader.get(i), info);
        }

    }

}