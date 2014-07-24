package upv.welcomeincoming.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import util.DBHandler_Horarios;
import util.InternetConnectionChecker;
import util.MarcadorValenbisi;
import util.Parser_XML_valenbisi;
import util.ProgressDialog_Custom;


public class Activity_Localizacion_Valenbisi extends FragmentActivity {
    private GoogleMap mMap;
    private ArrayList<MarcadorValenbisi> VBMarkersArray = new ArrayList<MarcadorValenbisi>();
    private HashMap<Marker, MarcadorValenbisi> VBMarkersHashMap;
    private LinearLayout linear;
    private Marker current;
    ProgressDialog_Custom progress;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHandler_Horarios helper = new DBHandler_Horarios(this);
        db = helper.getWritableDatabase();
        setContentView(R.layout.activity_localizacion);

        progress = new ProgressDialog_Custom(this, getString(R.string.loading));
        linear = (LinearLayout) findViewById(R.id.lineaerlayout_info_valenbisi);
        // Initialize the HashMap for Markers and MyMarker object

        VBMarkersHashMap = new HashMap<Marker, MarcadorValenbisi>();
        VBMarkersArray = obtenerMarcadoresVB(db);
        setUpMap();
        plotMarkers(VBMarkersArray);
    }

    private void plotMarkers(ArrayList<MarcadorValenbisi> markers) {
        if (markers.size() > 0) {
            for (MarcadorValenbisi myMarker : markers) {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(Double.parseDouble(myMarker.getLatitud()), (Double.parseDouble(myMarker.getLongitud()))));
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_valenbici));

                Marker currentMarker = mMap.addMarker(markerOption);
                VBMarkersHashMap.put(currentMarker, myMarker);
            }
        }
    }

    private void setUpMap() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_view)).getMap();
            LatLng posicionUPV = new LatLng(39.4810811, -0.3421079);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(posicionUPV, 15, 0, 20));
            mMap.animateCamera(cameraUpdate);

            // Check if we were successful in obtaining the map.

            if (mMap != null) {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final com.google.android.gms.maps.model.Marker marker) {
                        linear.removeAllViews();
                        LatLng posicionVB = new LatLng(Double.parseDouble(VBMarkersHashMap.get(marker).getLatitud()), Double.parseDouble(VBMarkersHashMap.get(marker).getLongitud()));
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(posicionVB, 17, 0, 20));
                        mMap.animateCamera(cameraUpdate);
                        current = marker;
                        if (new InternetConnectionChecker().checkInternetConnection(getApplicationContext())) {
                            RetrieveFeedTask plazas = new RetrieveFeedTask(marker, linear);
                            plazas.execute();
                        }

                        return true;
                    }
                });
            } else
                Toast.makeText(getApplicationContext(), "Unable to create Maps", Toast.LENGTH_SHORT).show();
        }
    }

    private MarcadorValenbisi obtenerMarcadorVB(int numero) {
        String sql = "SELECT * FROM Valenbisi WHERE num=" + numero + ";";
        Cursor cursor = db.rawQuery(sql, null);
        MarcadorValenbisi marcadorValenbisi = null;
        cursor.moveToFirst();
        marcadorValenbisi = new MarcadorValenbisi(cursor.getInt(cursor.getColumnIndex("num")), cursor.getString(cursor.getColumnIndex("direccion")), cursor.getString(cursor.getColumnIndex("longitud")), cursor.getString(cursor.getColumnIndex("latitud")), cursor.getInt(cursor.getColumnIndex("numPlazas")), cursor.getInt(cursor.getColumnIndex("plazasDisponibles")));
        cursor.close();
        return marcadorValenbisi;
    }

    private ArrayList<MarcadorValenbisi> obtenerMarcadoresVB(SQLiteDatabase db) {
        String sql = "SELECT * FROM Valenbisi;";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<MarcadorValenbisi> markers = new ArrayList<MarcadorValenbisi>();
        while (cursor.moveToNext()) {
            MarcadorValenbisi marker = new MarcadorValenbisi(cursor.getInt(cursor.getColumnIndex("num")), cursor.getString(cursor.getColumnIndex("direccion")), cursor.getString(cursor.getColumnIndex("longitud")), cursor.getString(cursor.getColumnIndex("latitud")), cursor.getInt(cursor.getColumnIndex("numPlazas")), cursor.getInt(cursor.getColumnIndex("plazasDisponibles")));
            markers.add(marker);
        }
        cursor.close();
        return markers;
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {
        private Marker mvb;
        private LinearLayout linear;

        RetrieveFeedTask(Marker mvb, LinearLayout linear) {
            this.mvb = mvb;
            this.linear = linear;
        }

        @Override
        protected void onPostExecute(Void v) {
            View view = getLayoutInflater().inflate(R.layout.layout_info_valenbisi, null);
            TextView textDireccion = (TextView) view.findViewById(R.id.txtDireccion);
            textDireccion.setText(VBMarkersHashMap.get(current).getDireccion());
            MarcadorValenbisi mvb = obtenerMarcadorVB(VBMarkersHashMap.get(current).getNumero());
            VBMarkersHashMap.remove(current);
            VBMarkersHashMap.put(current, mvb);
            TextView textTotal = (TextView) view.findViewById(R.id.txtNumplazas);
            textTotal.setText(VBMarkersHashMap.get(current).getNumeroPlazas() + "");
            TextView textDisponibles = (TextView) view.findViewById(R.id.txtPlazasdisponibles);
            textDisponibles.setText(VBMarkersHashMap.get(current).getPlazasDisponibles() + "");
            linear.addView(view);
            progress.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.getWindow().setGravity(Gravity.BOTTOM);
            progress.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            Parser_XML_valenbisi parser_xml_valenbisi = new Parser_XML_valenbisi();
            parser_xml_valenbisi.parsearPlazas(VBMarkersHashMap.get(mvb), db);
            return null;
        }
    }

}