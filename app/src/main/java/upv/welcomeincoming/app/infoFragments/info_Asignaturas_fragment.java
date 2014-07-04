package upv.welcomeincoming.app.infoFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import upv.welcomeincoming.app.R;
import util.ExpandableListAdapter;
import util.MultiExpandableListAdapter;
import util.XML_parser;
import util.asignatura;
import util.school;

/**
 * Created by Marcos on 30/04/14.
 */
public class info_Asignaturas_fragment extends Fragment {

    MultiExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<asignatura>> listDataChild;
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_asignaturas, container,false);

        expListView = (ExpandableListView) view.findViewById(R.id.multiexpandableListView);

        // preparing list data
        prepareListData();

        listAdapter = new MultiExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        return view;
    }
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<asignatura>>();


        InputStream fichero = this.getResources().openRawResource(R.raw.asignaturas);
        XML_parser parseador = new XML_parser("asignaturas");
        List<school> lista = parseador.parseando(fichero);
        String mostrar = "";
        for( int i = 0 ; i < lista.size() ; i++ ){
            school escuela = lista.get(i);
            listDataHeader.add(escuela.getEscuelanombre());
            List<asignatura> info = new ArrayList<asignatura>();
            List<asignatura> asig = escuela.getAsignaturas();
            for(int j = 0;j< asig.size();j++){
                info.add(asig.get(j));
            }
            listDataChild.put(listDataHeader.get(i),info);
        }

    }


}