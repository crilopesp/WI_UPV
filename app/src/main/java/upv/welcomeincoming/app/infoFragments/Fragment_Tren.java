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

import upv.welcomeincoming.app.R;
import util.DBHandler_Horarios;
import util.Transporte;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Tren extends Fragment {
    Transporte tren;
    SQLiteDatabase db;

    public Fragment_Tren() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tren, container, false);
        db = new DBHandler_Horarios(getActivity()).getReadableDatabase();
        tren = obtener();
        ImageButton web = (ImageButton) view.findViewById(R.id.imageWeb);
        TextView txtDescripcion = (TextView) view.findViewById(R.id.txtDescripcion);
        TextView txtTelefono = (TextView) view.findViewById(R.id.txtTelefono);

        txtDescripcion.setText(tren.getDescripcion());
        txtTelefono.setText(tren.getTelefono());
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://" + tren.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        return view;
    }


    private Transporte obtener() {
        String sql = "SELECT * FROM Transporte WHERE nombre = 'tren'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Transporte transporte = new Transporte(cursor.getString(cursor.getColumnIndex("nombre")), cursor.getString(cursor.getColumnIndex("descripcion")), cursor.getString(cursor.getColumnIndex("telefono")), cursor.getString(cursor.getColumnIndex("url")));
            return transporte;
        }
        cursor.close();
        return null;
    }
}
