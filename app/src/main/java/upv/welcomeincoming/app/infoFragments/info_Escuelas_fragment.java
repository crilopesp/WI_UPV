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
import util.ExpandableListAdapter;
import util.XML_parser;
import util.school;
import util.valencia;

/**
 * Created by Marcos on 30/04/14.
 */
public class info_Escuelas_fragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_escuelas, container,false);

        expListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        return view;
    }
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        InputStream fichero = this.getResources().openRawResource(R.raw.telefonos);
        XML_parser parseador = new XML_parser("telefonos");
        List<school> lista = parseador.parseando(fichero);
        String mostrar = "";
        for( int i = 0 ; i < lista.size() ; i++ ){
            school escuela = lista.get(i);
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
            listDataChild.put(listDataHeader.get(i),info);
        }

    }

}