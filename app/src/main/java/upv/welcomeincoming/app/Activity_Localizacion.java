package upv.welcomeincoming.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
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

import util.MarcadorUPV;


public class Activity_Localizacion extends FragmentActivity {
    private GoogleMap mMap;
    private ArrayList<MarcadorUPV> mMyMarkersArray = new ArrayList<MarcadorUPV>();
    private HashMap<Marker, MarcadorUPV> mMarkersHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacion);

        // Initialize the HashMap for Markers and MyMarker object
        mMarkersHashMap = new HashMap<Marker, MarcadorUPV>();

        mMyMarkersArray.add(new MarcadorUPV("Brasil", Double.parseDouble("-28.5971788"), Double.parseDouble("-52.7309824")));
        mMyMarkersArray.add(new MarcadorUPV("United States", Double.parseDouble("33.7266622"), Double.parseDouble("-87.1469829")));
        mMyMarkersArray.add(new MarcadorUPV("Canada", Double.parseDouble("51.8917773"), Double.parseDouble("-86.0922954")));
        mMyMarkersArray.add(new MarcadorUPV("England", Double.parseDouble("52.4435047"), Double.parseDouble("-3.4199249")));
        mMyMarkersArray.add(new MarcadorUPV("España", Double.parseDouble("41.8728262"), Double.parseDouble("-0.2375882")));
        mMyMarkersArray.add(new MarcadorUPV("Portugal", Double.parseDouble("40.8316649"), Double.parseDouble("-4.936009")));
        mMyMarkersArray.add(new MarcadorUPV("Deutschland", Double.parseDouble("51.1642292"), Double.parseDouble("10.4541194")));
        mMyMarkersArray.add(new MarcadorUPV("Atlantic Ocean", Double.parseDouble("-13.1294607"), Double.parseDouble("-19.9602353")));

        setUpMap();

        plotMarkers(mMyMarkersArray);
    }

    private void plotMarkers(ArrayList<MarcadorUPV> markers) {
        if (markers.size() > 0) {
            for (MarcadorUPV myMarker : markers) {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon1));

                Marker currentMarker = mMap.addMarker(markerOption);
                mMarkersHashMap.put(currentMarker, myMarker);

                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }

    private int manageMarkerIcon(String markerIcon) {
        if (markerIcon.equals("icon1"))
            return R.drawable.icon1;
        else if (markerIcon.equals("icon2"))
            return R.drawable.icon2;
        else if (markerIcon.equals("icon3"))
            return R.drawable.icon3;
        else if (markerIcon.equals("icon4"))
            return R.drawable.icon4;
        else if (markerIcon.equals("icon5"))
            return R.drawable.icon5;
        else if (markerIcon.equals("icon6"))
            return R.drawable.icon6;
        else if (markerIcon.equals("icon7"))
            return R.drawable.icon7;
        else
            return R.drawable.icondefault;
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
                        marker.showInfoWindow();
                        return true;
                    }
                });
            } else
                Toast.makeText(getApplicationContext(), "Unable to create Maps", Toast.LENGTH_SHORT).show();
        }
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        public MarkerInfoWindowAdapter() {
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v = getLayoutInflater().inflate(R.layout.infowindow_layout, null);

            MarcadorUPV myMarker = mMarkersHashMap.get(marker);

            TextView markerLabel = (TextView) v.findViewById(R.id.marker_label);
            markerLabel.setText(myMarker.getmLabel());

            return v;
        }
    }
}