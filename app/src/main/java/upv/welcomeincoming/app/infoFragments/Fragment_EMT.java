package upv.welcomeincoming.app.infoFragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import upv.welcomeincoming.app.Activity_Localizacion_EMT;
import upv.welcomeincoming.app.R;
import util.Transporte;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_EMT extends Fragment {

    Transporte emt;

    public Fragment_EMT(Transporte emt) {
        this.emt = emt;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emt, container, false);
        ImageButton google_map = (ImageButton) view.findViewById(R.id.imageviewGooglemap);
        ImageButton web = (ImageButton) view.findViewById(R.id.imageWeb);
        TextView txtDescripcion = (TextView) view.findViewById(R.id.txtDescripcion);
        TextView txtTelefono = (TextView) view.findViewById(R.id.txtTelefono);

        txtDescripcion.setText(emt.getDescripcion());
        txtTelefono.setText(emt.getTelefono());
        google_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Localizacion_EMT.class));
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://" + emt.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        return view;
    }


}
