package upv.welcomeincoming.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Marcos on 3/05/14.
 */
public class Fragment_Traduccion extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kitdev, container, false);
        /*ImageButton btn_speech = (ImageButton) view.findViewById(R.id.btn_speech);
        ImageButton btn_ra = (ImageButton) view.findViewById(R.id.btnRA);
        btn_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Traduccion.class);
                startActivity(intent);
            }
        });
        btn_ra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_RealidadAumentada.class);
                startActivity(intent);
            }
        });*/
        return view;
    }

}