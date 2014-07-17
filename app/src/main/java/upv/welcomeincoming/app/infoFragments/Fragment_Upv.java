package upv.welcomeincoming.app.infoFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import upv.welcomeincoming.app.R;

/**
 * Created by Marcos on 30/04/14.
 */
public class Fragment_Upv extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_upv, container, false);
        return view;
    }
}