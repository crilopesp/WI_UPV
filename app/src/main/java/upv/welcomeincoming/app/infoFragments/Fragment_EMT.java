package upv.welcomeincoming.app.infoFragments;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import util.DBHandler_Horarios;
import util.Transporte;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_EMT extends Fragment {

    Transporte emt;
    private SQLiteDatabase db;

    public Fragment_EMT() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emt, container, false);
        db = new DBHandler_Horarios(getActivity()).getReadableDatabase();
        emt = obtener();
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

    private Transporte obtener() {
        String sql = "SELECT * FROM Transporte WHERE nombre = 'bus'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Transporte transporte = new Transporte(cursor.getString(cursor.getColumnIndex("nombre")), cursor.getString(cursor.getColumnIndex("descripcion")), cursor.getString(cursor.getColumnIndex("telefono")), cursor.getString(cursor.getColumnIndex("url")));
            return transporte;
        }
        cursor.close();
        return null;
    }
}
