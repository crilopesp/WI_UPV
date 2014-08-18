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

public class Fragment_Taxi extends Fragment {
    Transporte tren;
    private SQLiteDatabase db;

    public Fragment_Taxi() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taxi, container, false);
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
        String sql = "SELECT * FROM Transporte WHERE nombre = 'taxi'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Transporte transporte = new Transporte(cursor.getString(cursor.getColumnIndex("nombre")), cursor.getString(cursor.getColumnIndex("descripcion")), cursor.getString(cursor.getColumnIndex("telefono")), cursor.getString(cursor.getColumnIndex("url")));
            return transporte;
        }
        cursor.close();
        return null;
    }
}
