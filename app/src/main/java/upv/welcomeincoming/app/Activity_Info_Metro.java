package upv.welcomeincoming.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_Info_Metro extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_info_metro);
        final String nombre = getIntent().getStringExtra("nombre");
        String[] info = getIntent().getStringArrayExtra("info");
        String informacion = info.toString();
        int id = getIntent().getIntExtra("id", -1);

        TextView textNombre = (TextView) findViewById(R.id.txtNombre);
        textNombre.setText(nombre);
        TextView textWeb = (TextView) findViewById(R.id.txtDireccionWeb);
        textWeb.setText("http://www.metrovalencia.es/estacion_detalle.php?id=" + id);
        textWeb.setLinksClickable(true);
        textWeb.setMovementMethod(LinkMovementMethod.getInstance());
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLineas);
        if (informacion.contains("1")) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.drawable.icon_linea1_big);
            linearLayout.addView(imageView);
        }
        if (informacion.contains("3")) {

            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.drawable.icon_linea3_big);
            linearLayout.addView(imageView);
        }
        if (informacion.contains("4")) {

            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.drawable.icon_linea4_big);
            linearLayout.addView(imageView);
        }
        if (informacion.contains("5")) {

            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.drawable.icon_linea5_big);
            linearLayout.addView(imageView);
        }
        if (informacion.contains("6")) {

            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.drawable.icon_linea6_big);
            linearLayout.addView(imageView);
        }
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
