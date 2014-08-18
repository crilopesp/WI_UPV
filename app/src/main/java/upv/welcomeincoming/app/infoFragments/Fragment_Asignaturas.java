package upv.welcomeincoming.app.infoFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import upv.welcomeincoming.app.R;
import util.Asignatura;
import util.Escuela;
import util.MultiExpandableListAdapter_Escuelas;
import util.Parser_XML;

/**
 * Created by Marcos on 30/04/14.
 */
public class Fragment_Asignaturas extends Fragment {

    MultiExpandableListAdapter_Escuelas listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<Asignatura>> listDataChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_asignaturas, container, false);

        expListView = (ExpandableListView) view.findViewById(R.id.multiexpandableListView);

        // preparing list data
        prepareListData();

        listAdapter = new MultiExpandableListAdapter_Escuelas(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        return view;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Asignatura>>();


        InputStream fichero = this.getResources().openRawResource(R.raw.asignaturas);
        Parser_XML parseador = new Parser_XML("asignaturas");
        List<Escuela> lista = parseador.parseando(fichero);
        String mostrar = "";
        for (int i = 0; i < lista.size(); i++) {
            Escuela escuela = lista.get(i);
            listDataHeader.add(escuela.getEscuelanombre());
            List<Asignatura> info = new ArrayList<Asignatura>();
            List<Asignatura> asig = escuela.getAsignaturas();
            for (int j = 0; j < asig.size(); j++) {
                info.add(asig.get(j));
            }
            listDataChild.put(listDataHeader.get(i), info);
        }

    }


}