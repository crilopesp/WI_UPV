package upv.welcomeincoming.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import util.MarcadorEdificio;
import util.ProgressDialog_Custom;


public class Activity_Localizacion_UPV extends FragmentActivity {
    private GoogleMap mMap;
    private ArrayList<MarcadorEdificio> UpvMarkersArray = new ArrayList<MarcadorEdificio>();
    private HashMap<Marker, MarcadorEdificio> UpvMarkersHashMap;
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

        UpvMarkersHashMap = new HashMap<Marker, MarcadorEdificio>();
        UpvMarkersArray = obtenerMarcadoresEdificios(db);
        setUpMap();
        plotMarkers(UpvMarkersArray);
    }

    private void plotMarkers(ArrayList<MarcadorEdificio> markers) {
        if (markers.size() > 0) {
            for (MarcadorEdificio myMarker : markers) {
                View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_edificios, null);
                TextView numTxt = (TextView) view.findViewById(R.id.num_txt);
                numTxt.setText(myMarker.getNumero());
                LatLng pos = new LatLng(Double.parseDouble(myMarker.getLongitud()), Double.parseDouble(myMarker.getLatitud()));
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(myMarker.getNumero())
                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, view))));
                UpvMarkersHashMap.put(marker, myMarker);
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
                    public boolean onMarkerClick(final Marker marker) {
                        linear.removeAllViews();
                        LatLng posicionUPV = new LatLng(Double.parseDouble(UpvMarkersHashMap.get(marker).getLongitud()), Double.parseDouble(UpvMarkersHashMap.get(marker).getLatitud()));
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(posicionUPV, 17, 0, 20));
                        mMap.animateCamera(cameraUpdate);

                        final String edificio = UpvMarkersHashMap.get(marker).getNumero();
                        boolean poi = false;
                        View view = getLayoutInflater().inflate(R.layout.layout_info_edificio, null);
                        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearEdificio);
                        ImageView imageView = new ImageView(getApplicationContext());
                        imageView.setImageResource(R.drawable.ic_action_ic_a_reality2_1);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), Acvitity_VistaRealidadAumentada.class);
                                intent.putExtra("poi", edificio);
                                startActivity(intent);
                            }
                        });
                        TextView textNombre = (TextView) view.findViewById(R.id.txtDireccion);
                        textNombre.setText(getString(R.string.edificio) + " " + UpvMarkersHashMap.get(marker).getNumero());
                        String[] edificios = getResources().getStringArray(R.array.edificios);
                        for (int i = 0; i < edificios.length; i++)
                            if (edificios[i].contains(edificio)) poi = true;
                        if (poi) {
                            linearLayout.addView(imageView);
                        } else {
                            linearLayout.removeView(imageView);
                        }

                        final String[] info = UpvMarkersHashMap.get(marker).getInformacion().split(";");
                        ListView listaInfo = (ListView) view.findViewById(R.id.listaInfor);
                        listaInfo.setDivider(getResources().getDrawable(R.drawable.divisor));
                        listaInfo.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.item_lista_escuelas_padre, info));

                        return true;
                    }
                });
            } else
                Toast.makeText(getApplicationContext(), "Unable to create Maps", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<MarcadorEdificio> obtenerMarcadoresEdificios(SQLiteDatabase db) {
        String sql = "SELECT * FROM Edificio;";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<MarcadorEdificio> markers = new ArrayList<MarcadorEdificio>();
        while (cursor.moveToNext()) {
            MarcadorEdificio marker = new MarcadorEdificio(cursor.getString(cursor.getColumnIndex("num")), cursor.getString(cursor.getColumnIndex("longitud")), cursor.getString(cursor.getColumnIndex("latitud")), cursor.getString(cursor.getColumnIndex("info")));
            markers.add(marker);
            Log.e("edificios MAP", marker.toString());
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