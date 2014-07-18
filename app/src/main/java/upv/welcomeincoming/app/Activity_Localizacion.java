package upv.welcomeincoming.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
import util.MarcadorUPV;
import util.MarcadorValenbisi;


public class Activity_Localizacion extends FragmentActivity {
    private GoogleMap mMap;
    private ArrayList<MarcadorUPV> UpvMarkersArray = new ArrayList<MarcadorUPV>();
    private HashMap<Marker, MarcadorUPV> UpvMarkersHashMap;
    private ArrayList<MarcadorValenbisi> VBMarkersArray = new ArrayList<MarcadorValenbisi>();
    private HashMap<Marker, MarcadorValenbisi> VBMarkersHashMap;
    private LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacion);

        linear = (LinearLayout) findViewById(R.id.lineaerlayout_info_valenbisi);
        // Initialize the HashMap for Markers and MyMarker object
        VBMarkersHashMap = new HashMap<Marker, MarcadorValenbisi>();

        DBHandler_Horarios helper = new DBHandler_Horarios(this);
        SQLiteDatabase db = helper.getWritableDatabase();
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
                    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                        linear.removeAllViews();
                        Log.e("marker", VBMarkersHashMap.get(marker).toString());
                        View view = getLayoutInflater().inflate(R.layout.layout_info_valenbisi, null);
                        TextView textDireccion = (TextView) view.findViewById(R.id.txtDireccion);
                        textDireccion.setText(VBMarkersHashMap.get(marker).getDireccion());
                        TextView textTotal = (TextView) view.findViewById(R.id.txtNumplazas);
                        textTotal.setText(VBMarkersHashMap.get(marker).getNumeroPlazas() + "");
                        TextView textDisponibles = (TextView) view.findViewById(R.id.txtPlazasdisponibles);
                        textDisponibles.setText(VBMarkersHashMap.get(marker).getPlazasDisponibles() + "");
                        linear.addView(view);
                        return true;
                    }
                });
            } else
                Toast.makeText(getApplicationContext(), "Unable to create Maps", Toast.LENGTH_SHORT).show();
        }
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
}