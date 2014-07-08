package upv.welcomeincoming.app.infoFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import upv.welcomeincoming.app.R;
import util.Parser_XML;
import util.Valencia;

/**
 * Created by Marcos on 30/04/14.
 */
public class Fragment_Valencia extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_valencia, container, false);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        parsearTexto(tv);
        return view;
    }

    private void parsearTexto(TextView tv) {
        InputStream fichero = this.getResources().openRawResource(R.raw.valencia_intro);
        Parser_XML parseador = new Parser_XML("valencia_intro");
        List<Valencia> lista = parseador.parseando(fichero);
        String mostrar = "";
        for (int i = 0; i < lista.size(); i++) {
            Valencia objeto = lista.get(i);
            mostrar = mostrar + objeto.getDescripcion();
        }
        tv.setText(mostrar);
    }
}