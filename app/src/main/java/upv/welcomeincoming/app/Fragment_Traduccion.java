package upv.welcomeincoming.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import util.Traductor.ApiKeys;
import util.Traductor.detect.Detect;
import util.Traductor.language.Language;
import util.Traductor.translate.Translate;


/**
 * Created by Marcos on 3/05/14.
 */
public class Fragment_Traduccion extends Fragment {

    TextView txTrad;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_traduccion, container, false);
        Translate.setKey(ApiKeys.YANDEX_API_KEY);
        Button btn_speech = (Button) view.findViewById(R.id.btn_speech);
        final EditText editText = (EditText) view.findViewById(R.id.etTrad);
        txTrad = (TextView) view.findViewById(R.id.tvTrad);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().equals("")) {
                    new TraductorTask(editText.getText().toString(), null, Language.ENGLISH, true).execute();
                }
            }
        });
        return view;
    }

    private class TraductorTask extends AsyncTask<String, Void, Void> {
        private String textoAtraducir;
        private Language idiomaOrigen;
        private Language idiomaDestino;
        private boolean detectarIdioma;
        private String textoTraducido;

        private TraductorTask(String texto, Language idiomaOrigen, Language idiomaDestino, boolean detectarIdioma) {
            textoAtraducir = texto;
            this.idiomaDestino = idiomaDestino;
            this.idiomaOrigen = idiomaOrigen;
            this.detectarIdioma = detectarIdioma;
        }

        private Exception exception;

        @Override
        protected void onPostExecute(Void v) {

            txTrad.setText(textoTraducido);

        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                if (detectarIdioma) {
                    Language idiomaDetectado = Detect.execute(textoAtraducir);
                    textoTraducido = Translate.execute(textoAtraducir, idiomaDetectado, idiomaDestino);
                } else {
                    textoTraducido = Translate.execute(textoAtraducir, idiomaOrigen, idiomaDestino);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}