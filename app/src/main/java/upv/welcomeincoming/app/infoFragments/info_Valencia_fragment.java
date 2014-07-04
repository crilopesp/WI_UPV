package upv.welcomeincoming.app.infoFragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import upv.welcomeincoming.app.R;
import util.XML_parser;
import util.valencia;

/**
 * Created by Marcos on 30/04/14.
 */
public class info_Valencia_fragment extends Fragment {


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_valencia, container,false);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        parsearTexto(tv);
        return view;
    }

    private void parsearTexto(TextView tv){
        InputStream fichero = this.getResources().openRawResource(R.raw.valencia_intro);
        XML_parser parseador = new XML_parser("valencia_intro");
        List<valencia> lista = parseador.parseando(fichero);
        String mostrar = "";
        for( int i = 0 ; i < lista.size() ; i++ ){
            valencia objeto = lista.get(i);
            mostrar = mostrar + objeto.getDescripcion();
        }
        tv.setText(mostrar);
        tv.setTextColor(Color.BLACK);
        tv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_font.ttf"));
    }
}