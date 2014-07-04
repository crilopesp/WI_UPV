package Intranet;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by juanan on 27/04/14.
 */
public class IntranetCalendars extends AsyncTask<List<String>, Void, InputParamsIntranetConnection> {

    private InputParamsIntranetConnection inputParamsIntranetConnection;
    private Observer observer;

    public IntranetCalendars(Observer observer){
        this.observer = observer;
    }

    @Override
    protected void onPreExecute(){
        this.inputParamsIntranetConnection = new InputParamsIntranetConnection();
        this.inputParamsIntranetConnection.addObserver(this.observer);
    }

    @Override
    protected InputParamsIntranetConnection doInBackground(List<String>... params) {

        String urlPath = String.format("https://www.upv.es/pls/soalu/est_intranet.NI_app?p_idioma=%1$s&p_cua=%2$s", "c", "icalmovil");

        try {

            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory
                    .getDefault();

            URL url = new URL(urlPath);
            HttpsURLConnection request = (HttpsURLConnection) url.openConnection();

            request.setSSLSocketFactory(sslsocketfactory);

            request.setUseCaches(false);
            request.setRequestMethod("GET");

            request.setFollowRedirects(true);
            request.setInstanceFollowRedirects(true);

            request.setRequestProperty("User-Agent","Mozilla/5.0");
            request.setRequestProperty("Host", "www.upv.es");

            for (String cookie : params[0]) {
                request.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }

            request.setRequestProperty("Referer", "http://www.upv.es");

            request.setDoOutput(true);
            request.setDoInput(true);

            inputParamsIntranetConnection.setCodeResponse(request.getResponseCode());

            if(request.getHeaderFields().get("Set-Cookie")!=null){
                params[0].addAll(request.getHeaderFields().get("Set-Cookie"));
            }

            inputParamsIntranetConnection.setCookieList(params[0]);

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(request.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            this.inputParamsIntranetConnection.setResult(response.toString());

            //content debug...
            Log.d( ((Object)this).getClass().getName(), "Code response: " + this.inputParamsIntranetConnection.getCodeResponse());

        } catch (Exception e) {
            inputParamsIntranetConnection.setException(e);
        }

        return inputParamsIntranetConnection;
    }

    @Override
    protected void onPostExecute(InputParamsIntranetConnection inputParamsIntranetConnection){
        inputParamsIntranetConnection.notifyObservers("calendars");
    }

}