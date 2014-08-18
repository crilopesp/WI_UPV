package upv.welcomeincoming.app.foro;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import upv.welcomeincoming.app.Activity_Home;
import upv.welcomeincoming.app.R;
import util.MyPhotoUtil;
import util.Preferencias;

public class Activity_ver_foto extends ActionBarActivity {


    Activity _context;
    Bitmap bitmap;
    String nombreUsuario;
    ImageView iv_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_foto);

        _context = this;
        nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        bitmap = (Bitmap) getIntent().getParcelableExtra("BitmapImage");
        iv_photo = (ImageView) findViewById(R.id.imageView);
        int ratio = 6;
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * ratio, bitmap.getHeight() * ratio, true);
        iv_photo.setImageBitmap(bitmap);
        setTitle(nombreUsuario);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //String regid = GCMConfig.getGcmRegId(this);


        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Log.e("", "insertando usuario...");
                    Controlador.dameControlador().insertarUsuario(_context, "Marcos", "Perello", "es");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(null, null, null);
        String texto = getString(R.string.msgNotificacionRegistro).replace("%%", "%");
        texto = String.format(texto, Preferencias.getNombre(this));//texto del mensaje
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Activity_Home.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_aplicacion)
                .setContentTitle(getString(R.string.bienvenido))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(texto))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(texto);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ver_foto, menu);
        MenuItem item = menu.add(Menu.NONE, R.id.actualizar_action, 10, R.string.compartir);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.ic_action_share_white);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actualizar_action: //en realidad es compartir..
                //String pathTobmp = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"user_profile_welcomeincoming", null);
                String pathToFile = MyPhotoUtil.saveImageProfile(bitmap);
                if (pathToFile != null) {
                    Uri bmpUri = Uri.parse(pathToFile);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    intent.setType("image/jpeg");
                    startActivity(intent);
                } else {
                    Toast.makeText(_context, R.string.error, Toast.LENGTH_SHORT).show();
                }
                return true;

            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
