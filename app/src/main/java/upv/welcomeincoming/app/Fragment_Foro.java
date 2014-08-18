package upv.welcomeincoming.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import upv.welcomeincoming.app.foro.Activity_Nuevo_Tema;
import upv.welcomeincoming.app.foro.Activity_Temas_Buscar;
import upv.welcomeincoming.app.foro.Activity_Temas_Language;
import upv.welcomeincoming.app.foro.Activity_Temas_byMe;
import upv.welcomeincoming.app.foro.Activity_Temas_comentMe;
import upv.welcomeincoming.app.foro.Activity_Temas_todos;
import upv.welcomeincoming.app.foro.Controlador;
import util.Preferencias;


/**
 * Created by Marcos on 3/05/14.
 */
public class Fragment_Foro extends Fragment {

    Controlador control;
    LinearLayout layout_temasAll;
    LinearLayout layout_temasMe;
    LinearLayout layout_temasComent;
    LinearLayout layout_temasSearch;
    LinearLayout layout_temasLanguage;
    LinearLayout layout_NewTema;
    TextView tv_nombre;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foro, container, false);
        layout_temasAll = (LinearLayout) view.findViewById(R.id.layout_temas_all);
        layout_temasMe = (LinearLayout) view.findViewById(R.id.layout_misTemas);
        layout_temasComent = (LinearLayout) view.findViewById(R.id.layout_temasComentados);
        layout_temasSearch = (LinearLayout) view.findViewById(R.id.layout_temasBuscar);
        layout_temasLanguage = (LinearLayout) view.findViewById(R.id.layout_temasLanguage);
        layout_NewTema = (LinearLayout) view.findViewById(R.id.layout_nuevoTema);
        tv_nombre = (TextView) view.findViewById(R.id.tv_nombreUsuario);

        initComponents();


        return view;
    }

    private void initComponents() {
        try {
            control = Controlador.dameControlador();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //obtenemos nombre usuario de las preferencias
        String nombre = Preferencias.getNombre(getActivity()) + " " + Preferencias.getApellidos(getActivity());
        tv_nombre.setText(nombre);
        layout_temasAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Activity_Temas_todos.class);
                startActivity(i);
            }
        });
        layout_temasMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Activity_Temas_byMe.class);
                startActivity(i);
            }
        });
        layout_temasComent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Activity_Temas_comentMe.class);
                startActivity(i);
            }
        });
        layout_temasSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Activity_Temas_Buscar.class);
                startActivity(i);
            }
        });
        layout_temasLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Activity_Temas_Language.class);
                startActivity(i);
            }
        });
        layout_NewTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Activity_Nuevo_Tema.class);
                startActivity(i);
            }
        });


    }


}