package upv.welcomeincoming.app.infoFragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import upv.welcomeincoming.app.R;
import util.XML_parser;
import util.transporte;


/**
 * Created by Marcos on 30/04/14.
 */
public class info_transportes_fragment extends Fragment {

    List<transporte> listaTransportes;
    FrameLayout panelLayout;
    LayoutInflater inflador;
    TextView tv_nomTransporte;
    Button currentSelected;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_transportes, container,false);
        ((View)view.findViewById(R.id.toFront)).bringToFront();
        tv_nomTransporte = (TextView)view.findViewById(R.id.tv_nomTransport);
        tv_nomTransporte.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_font.ttf"));
        panelLayout = (FrameLayout) view.findViewById(R.id.panelTransportes);
        inflador = inflater;
        final Button avion = (Button) view.findViewById(R.id.btnAvion);
        final Button taxi = (Button) view.findViewById(R.id.btnTaxi);
        final Button metro = (Button) view.findViewById(R.id.btnMetro);
        final Button bus = (Button) view.findViewById(R.id.btnBus);
        final Button tren = (Button) view.findViewById(R.id.btnTren);
        final Button mar = (Button) view.findViewById(R.id.btnMar);
        final Button carretera = (Button) view.findViewById(R.id.btnCarretera);
        final Button autocar = (Button) view.findViewById(R.id.btnAutocar);
        currentSelected = avion;

        InputStream fichero = this.getResources().openRawResource(R.raw.transportes);
        XML_parser parseador = new XML_parser("transportes");
        listaTransportes = parseador.parseando(fichero);

        avion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarVista(encontrarTransportById("avion"));
                avion.setBackgroundResource(R.drawable.button_custom_grey);
                currentSelected = avion;
            }
        });
        avion.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_font.ttf"));
        taxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarVista(encontrarTransportById("taxi"));
                taxi.setBackgroundResource(R.drawable.button_custom_grey);
                currentSelected = taxi;
            }
        });
        taxi.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_font.ttf"));
        metro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarVista(encontrarTransportById("metro"));
                metro.setBackgroundResource(R.drawable.button_custom_grey);
                currentSelected = metro;
            }
        });
        metro.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_font.ttf"));
        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarVista(encontrarTransportById("bus"));
                bus.setBackgroundResource(R.drawable.button_custom_grey);
                currentSelected = bus;
            }
        });
        bus.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_font.ttf"));
        tren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarVista(encontrarTransportById("tren"));
                tren.setBackgroundResource(R.drawable.button_custom_grey);
                currentSelected = tren;
            }
        });
        tren.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_font.ttf"));
        autocar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarVista(encontrarTransportById("autocar"));
                autocar.setBackgroundResource(R.drawable.button_custom_grey);
                currentSelected = autocar;
            }
        });
        autocar.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_font.ttf"));
        carretera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarVista(encontrarTransportById("carreteras"));
                carretera.setBackgroundResource(R.drawable.button_custom_grey);
                currentSelected = carretera;
            }
        });
        carretera.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_font.ttf"));
        mar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarVista(encontrarTransportById("maritimo"));
                mar.setBackgroundResource(R.drawable.button_custom_grey);
                currentSelected = mar;
            }
        });
        mar.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_font.ttf"));


        return view;
    }

    private void cargarVista(transporte t){
        if(t==null) return;
        View vista = inflador.inflate(R.layout.item_transporte,null);
        tv_nomTransporte.setText(firsToUpper(t.getNombre()));
        currentSelected.setBackgroundResource(R.drawable.button_custom_red);
        TextView tv_descripcion = (TextView) vista.findViewById(R.id.tv_descripcion_transport);
        tv_descripcion.setText(t.getDescripcion());
        tv_descripcion.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_font.ttf"));
        TextView tv_website = (TextView) vista.findViewById(R.id.tv_website);
        tv_website.setText(t.getUrl());
        tv_website.setAutoLinkMask(Linkify.WEB_URLS); tv_website.setMovementMethod(new ScrollingMovementMethod());
        TextView tv_phone1 = (TextView) vista.findViewById(R.id.tv_phone1);
        tv_phone1.setAutoLinkMask(Linkify.PHONE_NUMBERS); tv_phone1.setMovementMethod(new ScrollingMovementMethod());
        TextView tv_phone2 = (TextView) vista.findViewById(R.id.tv_phone2);
        tv_phone2.setAutoLinkMask(Linkify.PHONE_NUMBERS); tv_phone2.setMovementMethod(new ScrollingMovementMethod());
        TextView tv_phone3 = (TextView) vista.findViewById(R.id.tv_phone3);
        tv_phone3.setAutoLinkMask(Linkify.PHONE_NUMBERS); tv_phone3.setMovementMethod(new ScrollingMovementMethod());
        String[] telefonos = t.getTelefono();
        if(telefonos[0] != null) tv_phone1.setText(telefonos[0]);
        if(telefonos[1] != null) tv_phone2.setText(telefonos[1]);
        if(telefonos[2] != null) tv_phone3.setText(telefonos[2]);

        panelLayout.removeAllViews();
        panelLayout.addView(vista);
    }

    private String firsToUpper(String nombre) {
        return Character.toUpperCase(nombre.charAt(0)) + nombre.substring(1);
    }

    private transporte encontrarTransportById(String nombreTransporte){
        for( int i = 0 ; i < listaTransportes.size() ; i++ ){
            transporte transport = listaTransportes.get(i);
            if(transport.getNombre().equals(nombreTransporte)) return transport;
        }
        return null;
    }

}