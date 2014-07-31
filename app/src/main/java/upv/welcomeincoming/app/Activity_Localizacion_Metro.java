package upv.welcomeincoming.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
import util.Metro;
import util.ProgressDialog_Custom;


public class Activity_Localizacion_Metro extends FragmentActivity {
    private GoogleMap mMap;
    private ArrayList<Metro> MarkersArray = new ArrayList<Metro>();
    private HashMap<Marker, Metro> MarkersHashMap;
    private LinearLayout linear;
    private Marker current;
    ProgressDialog_Custom progress;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHandler_Horarios helper = new DBHandler_Horarios(this);
        db = helper.getWritableDatabase();
        setContentView(R.layout.activity_localizacion_noinfo);

        progress = new ProgressDialog_Custom(this, getString(R.string.loading));
        linear = (LinearLayout) findViewById(R.id.lineaerlayout_info_valenbisi);
        // Initialize the HashMap for Markers and MyMarker object
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else {
            MarkersHashMap = new HashMap<Marker, Metro>();
            MarkersArray = obtenerMarcadoresMetro(db);
            setUpMap();
            plotMarkers(MarkersArray);
        }
    }

    private void plotMarkers(ArrayList<Metro> markers) {
        if (markers.size() > 0) {
            for (Metro myMarker : markers) {
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(Double.parseDouble(myMarker.getLatitud()), (Double.parseDouble(myMarker.getLongitud()))));
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.parada_metro));

                Marker currentMarker = mMap.addMarker(markerOption);
                MarkersHashMap.put(currentMarker, myMarker);
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

            mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.

            if (mMap != null) {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        LatLng posicionUPV = new LatLng(Double.parseDouble(MarkersHashMap.get(marker).getLongitud()), Double.parseDouble(MarkersHashMap.get(marker).getLatitud()));
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(posicionUPV, 17, 0, 20));
                        mMap.animateCamera(cameraUpdate);

                        String nombre = MarkersHashMap.get(marker).getNombre();
                        String[] lineas = MarkersHashMap.get(marker).getLineasString();
                        int id = MarkersHashMap.get(marker).getId();
                        Intent intent = new Intent(getApplicationContext(), Activity_Info_UPV.class);
                        intent.putExtra("nombre", nombre);
                        intent.putExtra("lineas", lineas);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        return true;
                    }
                });
            } else
                Toast.makeText(getApplicationContext(), "Unable to create Maps", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Metro> obtenerMarcadoresMetro(SQLiteDatabase db) {
        String sql = "SELECT * FROM Metro";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<Metro> markers = new ArrayList<Metro>();
        while (cursor.moveToNext()) {
            Metro marker = new Metro(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("nombre")), cursor.getString(cursor.getColumnIndex("latitud")), cursor.getString(cursor.getColumnIndex("longitud")), cursor.getString(cursor.getColumnIndex("lineas")));
            markers.add(marker);
        }
        cursor.close();
        return markers;
    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}