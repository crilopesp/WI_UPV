package upv.welcomeincoming.app;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import util.InternetConnectionChecker;
import util.Traducir;

public class Activity_Traduccion extends ActionBarActivity {
    private static final String LOG_SPEECH = "LOG_SPEECH";
    private static final String LOG_TRANS = "LOG_TRANS";
    private int RESULT_SPEECH = 1;
    private TextView tv_trans;
    private TextToSpeech ttobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        if (savedInstanceState == null) {
        }

        ttobj = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            ttobj.setLanguage(new Locale("es", "ES"));//español de españa
                            //ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                }
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.translate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openSpeech(View view) {
        //borramos si lo hubiera la traducción anterior
        tv_trans = (TextView) findViewById(R.id.ShowTranslate);
        tv_trans.setText("");

        //shows dialog box to recognize speech input
        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            startActivityForResult(intent, RESULT_SPEECH);
        } catch (ActivityNotFoundException a) {

            Toast t = Toast.makeText(getApplicationContext(),
                    a.getMessage(),
                    Toast.LENGTH_LONG);
            t.show();
            Log.d(LOG_SPEECH, a.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_SPEECH) {
            switch (resultCode) {
                case RESULT_OK: {
                    if (null != data) {

                        ArrayList<String> text = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                        gotoTranslate(text.get(0));

                    }
                    break;
                }

                case RecognizerIntent.RESULT_AUDIO_ERROR: {
                    alertInfo(getResources().getString(R.string.err_audio));
                    break;
                }

                case RecognizerIntent.RESULT_CLIENT_ERROR: {
                    alertInfo(getResources().getString(R.string.err_client));
                    break;
                }

                case RecognizerIntent.RESULT_NETWORK_ERROR: {
                    alertInfo(getResources().getString(R.string.err_network));
                    break;
                }

                case RecognizerIntent.RESULT_NO_MATCH: {
                    alertInfo(getResources().getString(R.string.err_no_match));
                    break;
                }

                case RecognizerIntent.RESULT_SERVER_ERROR: {
                    alertInfo(getResources().getString(R.string.err_server));
                    break;
                }

            }
        }

    }

    public void alertInfo(String mns) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mns)
                .setPositiveButton(getResources().getString(R.string.btn_OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });
        // Create the AlertDialog object and return it
        builder.show();
    }

    private void gotoTranslate(String mns) {
        new TranslateWS().execute(mns);
    }

    private void displayTranslate(String text) {
        //mostramos la traducción actual
        TextView tvShowTranslate = (TextView) findViewById(R.id.ShowTranslate);
        tvShowTranslate.setText(text);
    }


    public void gotoSpeak(View view) {
        String toSpeak = tv_trans.getText().toString();

        if (toSpeak != "") {
            Intent intent = new Intent(Activity_Traduccion.this, Activity_Speaker.class);
            intent.putExtra("text", toSpeak);
            Activity_Traduccion.this.startActivity(intent);
        } else
            alertInfo(getResources().getString(R.string.err_no_text_TTS));

    }

    public void gotoSpeak_txt_1(View view) {
        final String toSpeak = getResources().getString(R.string.txt_1);

        if (toSpeak != "") {

            //ttobj.setLanguage(new Locale("es", "ES"));
            ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            Intent intent = new Intent(Activity_Traduccion.this, Activity_Speaker.class);
            intent.putExtra("text", toSpeak);
            //TranslateActivity.this.startActivity(intent);
        } else
            alertInfo(getResources().getString(R.string.err_no_text_TTS));

    }

    public void gotoSpeak_txt_2(View view) {
        String toSpeak = getResources().getString(R.string.txt_2);

        if (toSpeak != "") {
            ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            Intent intent = new Intent(Activity_Traduccion.this, Activity_Speaker.class);
            intent.putExtra("text", toSpeak);
            //TranslateActivity.this.startActivity(intent);
        } else
            alertInfo(getResources().getString(R.string.err_no_text_TTS));

    }

    public void gotoSpeak_txt_3(View view) {
        String toSpeak = getResources().getString(R.string.txt_3);

        if (toSpeak != "") {
            ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            Intent intent = new Intent(Activity_Traduccion.this, Activity_Speaker.class);
            intent.putExtra("text", toSpeak);
            //TranslateActivity.this.startActivity(intent);
        } else
            alertInfo(getResources().getString(R.string.err_no_text_TTS));

    }

    public void gotoSpeak_txt_4(View view) {
        String toSpeak = getResources().getString(R.string.txt_4);

        if (toSpeak != "") {
            ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            Intent intent = new Intent(Activity_Traduccion.this, Activity_Speaker.class);
            intent.putExtra("text", toSpeak);
            //TranslateActivity.this.startActivity(intent);
        } else
            alertInfo(getResources().getString(R.string.err_no_text_TTS));

    }

    public void gotoSpeak_txt_5(View view) {
        String toSpeak = getResources().getString(R.string.txt_5);

        if (toSpeak != "") {
            ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            Intent intent = new Intent(Activity_Traduccion.this, Activity_Speaker.class);
            intent.putExtra("text", toSpeak);
            //TranslateActivity.this.startActivity(intent);
        } else
            alertInfo(getResources().getString(R.string.err_no_text_TTS));

    }

    public void gotoSpeak_txt_6(View view) {
        String toSpeak = getResources().getString(R.string.txt_6);

        if (toSpeak != "") {
            ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            Intent intent = new Intent(Activity_Traduccion.this, Activity_Speaker.class);
            intent.putExtra("text", toSpeak);
            //TranslateActivity.this.startActivity(intent);
        } else
            alertInfo(getResources().getString(R.string.err_no_text_TTS));

    }

    public void gotoSpeak_txt_7(View view) {
        String toSpeak = getResources().getString(R.string.txt_7);

        if (toSpeak != "") {
            ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            Intent intent = new Intent(Activity_Traduccion.this, Activity_Speaker.class);
            intent.putExtra("text", toSpeak);
            //TranslateActivity.this.startActivity(intent);
        } else
            alertInfo(getResources().getString(R.string.err_no_text_TTS));

    }

    public void gotoSpeak_txt_8(View view) {
        String toSpeak = getResources().getString(R.string.txt_8);

        if (toSpeak != "") {
            ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            Intent intent = new Intent(Activity_Traduccion.this, Activity_Speaker.class);
            intent.putExtra("text", toSpeak);
            //TranslateActivity.this.startActivity(intent);
        } else
            alertInfo(getResources().getString(R.string.err_no_text_TTS));

    }

    private class TranslateWS extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String _txtInput = strings[0];
            String _txtOutput = "";
            BufferedReader reader = null;
            InternetConnectionChecker connCheck = new InternetConnectionChecker();

            try {
                if (connCheck.checkInternetConnection(getApplicationContext())) {
                    //https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20140402T105859Z.4a17eb3e003ca70b.f0a5f0680f124bd00ae5ab4e781f4e1c16593dd2&
                    // lang=en-es&text=To+be,+or+not+to+be%3F&text=That+is+the+question.
                    //lang=es ->detecta automaticamente el idioma de entrada
                    List<NameValuePair> data = new ArrayList<NameValuePair>();
                    BasicNameValuePair pair = new BasicNameValuePair("key", "trnsl.1.1.20140402T105859Z.4a17eb3e003ca70b.f0a5f0680f124bd00ae5ab4e781f4e1c16593dd2");
                    data.add(pair);
                    pair = new BasicNameValuePair("lang", "es");
                    data.add(pair);
                    pair = new BasicNameValuePair("text", _txtInput);//luego pasarlo por parametro
                    data.add(pair);

                    URL url = new URL("https://translate.yandex.net/api/v1.5/tr.json/translate?" + URLEncodedUtils.format(data, "UTF-8"));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //con.setRequestMethod("GET"); //El metodo por defecto ya es GET
                    conn.setDoOutput(false);//NO POST
                    conn.setDoInput(true);//GET  vamos a recibir info
                    Log.d(LOG_TRANS, "parametros: " + data);

                    if (conn.getResponseCode() == 200) {

                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();

                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        Traducir response = gson.fromJson(reader, Traducir.class);
                        Log.d(LOG_TRANS, "Traduccion obtenida");

                        _txtOutput = response.getText();


                    } else
                        Log.d(LOG_TRANS, "Error in TranslateWS: " + conn.getResponseCode() + " : " + conn.getResponseMessage());

                } else {
                    Log.d(LOG_TRANS, "without internet connection");
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.sinConn), Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();//sin permisos en androidmanifest para acceso a internet ni red
                Log.d(LOG_TRANS, "exception: " + ex.getMessage());
            } finally {
                try {

                    reader.close();

                } catch (Exception ex) {
                    Log.d(LOG_TRANS, "finally: " + ex.getMessage());
                }
            }
            return _txtOutput;
        }


        @Override
        protected void onPostExecute(String text) {
            Log.d(LOG_TRANS, "En onPostExecute de TranslateWS: respuesta del servidor " + text);
            //alertInfo(text);
            displayTranslate(text);

        }


    }

}
