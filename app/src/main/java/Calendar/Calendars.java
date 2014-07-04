package Calendar;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Calendars{

    private String username;
    private List<Agenda> agendas;
    private Agenda working;

    public Calendars(String data){
        agendas = new ArrayList<Agenda>();
        this.parseJson(data);
        this.working = null;
    }

    private void parseJson(String data){

        try {

            JSONObject jObject = new JSONObject(data);

            //username
            this.username = jObject.getString("username");

            //agendas...
            JSONArray agendasArray = jObject.getJSONArray("agendas");
            for (int i=0; i < agendasArray.length(); i++)
            {
                try {

                    this.agendas.add(new Agenda(agendasArray.getJSONObject(i)));

                } catch (JSONException e) {
                    Log.w(((Object) this).getClass().getName(), "Exception", e);
                }
            }
        } catch (JSONException e) {
            Log.w(((Object) this).getClass().getName(), "Exception", e);
        }
    }

    public void setAgendas(List<Agenda> agendas) {
        this.agendas = agendas;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Agenda getWorking() {
        return working;
    }

    public void setWorking(Agenda working) {
        this.working = working;
    }

    public List<Agenda> getAgendas() {
        return agendas;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        for (Agenda item : this.agendas){
            stringBuilder.append(item.toString());
        }

        return "Calendars{" +
                "username='" + username + '\'' +
                ", agendas=" + stringBuilder.toString() +
                '}';
    }
}


