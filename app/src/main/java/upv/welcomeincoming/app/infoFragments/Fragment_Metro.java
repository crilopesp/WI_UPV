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

import upv.welcomeincoming.app.Activity_Localizacion_Metro;
import upv.welcomeincoming.app.Activity_no_connection;
import upv.welcomeincoming.app.R;
import util.DBHandler_Horarios;
import util.InternetConnectionChecker;
import util.Transporte;

public class Fragment_Metro extends Fragment {
    Transporte metro;
    InternetConnectionChecker icc;
    private SQLiteDatabase db;

    public Fragment_Metro() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_metro, container, false);
        db = new DBHandler_Horarios(getActivity()).getReadableDatabase();
        metro = obtener();
        ImageButton static_map = (ImageButton) view.findViewById(R.id.imageviewStaticmap);
        ImageButton google_map = (ImageButton) view.findViewById(R.id.imageviewGooglemap);
        ImageButton web = (ImageButton) view.findViewById(R.id.imageWeb);
        TextView txtDescripcion = (TextView) view.findViewById(R.id.txtDescripcion);
        TextView txtTelefono = (TextView) view.findViewById(R.id.txtTelefono);
        icc = new InternetConnectionChecker();

        txtDescripcion.setText(metro.getDescripcion());
        txtTelefono.setText(metro.getTelefono());
        static_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_FullScreen_Image.class));
            }
        });

        google_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (icc.checkInternetConnection(getActivity())) {
                    startActivity(new Intent(getActivity(), Activity_Localizacion_Metro.class));
                } else {
                    Intent intent = new Intent(getActivity(), Activity_no_connection.class);
                    intent.putExtra("case", getString(R.string.noInetMaps));
                    startActivity(intent);
                }
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(metro.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        return view;
    }

    private Transporte obtener() {
        String sql = "SELECT * FROM Transporte WHERE nombre = 'metro'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Transporte transporte = new Transporte(cursor.getString(cursor.getColumnIndex("nombre")), cursor.getString(cursor.getColumnIndex("descripcion")), cursor.getString(cursor.getColumnIndex("telefono")), cursor.getString(cursor.getColumnIndex("url")));
            return transporte;
        }
        cursor.close();
        return null;
    }
}
