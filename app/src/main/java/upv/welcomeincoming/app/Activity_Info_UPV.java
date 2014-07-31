package upv.welcomeincoming.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Activity_Info_UPV extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_info_edificio);
        final String edificio = getIntent().getStringExtra("edificio");
        String[] info = getIntent().getStringArrayExtra("info");
        boolean poi = false;
        TextView textNombre = (TextView) findViewById(R.id.txtDireccion);
        textNombre.setText(getString(R.string.edificio) + " " + edificio);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearEdificio);
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

        String[] edificios = getResources().getStringArray(R.array.edificios);
        for (int i = 0; i < edificios.length; i++)
            if (edificios[i].contains(edificio)) poi = true;
        if (poi) {
            linearLayout.addView(imageView);
        } else {
            linearLayout.removeView(imageView);
        }
        //final String[] info = UpvMarkersHashMap.get(marker).getInformacion().split(";");
        ListView listaInfo = (ListView) findViewById(R.id.listaInfor);
        listaInfo.setDivider(getResources().getDrawable(R.drawable.divisor));
        listaInfo.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
        listaInfo.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.item_lista_info_upv, info));

        Button btnAceptar = (Button) findViewById(R.id.btnAceptar);
        Button btnCancelar = (Button) findViewById(R.id.btnCancelar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
