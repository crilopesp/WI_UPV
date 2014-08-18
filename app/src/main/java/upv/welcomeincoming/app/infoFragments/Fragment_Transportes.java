package upv.welcomeincoming.app.infoFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import upv.welcomeincoming.app.R;
import util.Parser_XML;
import util.Transporte;


/**
 * Created by Marcos on 30/04/14.
 */
public class Fragment_Transportes extends Fragment {

    List<Transporte> listaTransportes;
    FrameLayout panelLayout;
    LayoutInflater inflador;
    TextView tv_nomTransporte;
    Button currentSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_transportes, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        InputStream fichero = this.getResources().openRawResource(R.raw.transportes);
        Parser_XML parseador = new Parser_XML("transportes");
        listaTransportes = parseador.parseando(fichero);
        listaTransportes.remove(0);
        arreglarTransportes();

    }

    private String firsToUpper(String nombre) {
        return Character.toUpperCase(nombre.charAt(0)) + nombre.substring(1);
    }

    private void arreglarTransportes() {
        for (int i = 1; i < listaTransportes.size(); i++) {
            Transporte transport = listaTransportes.get(i);
            transport.setNombre(firsToUpper(transport.getNombre()));
        }

    }
}