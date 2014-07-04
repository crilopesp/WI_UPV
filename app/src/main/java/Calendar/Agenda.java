package Calendar;

import android.util.Log;


import net.fortuna.ical4j.model.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Agenda{

    private String uid;
    private String nombre;
    private String grupo;
    private String url;
    private List<Event> events;

    public Agenda(JSONObject element) {
        try {
            this.uid = element.getString("uid");
            this.nombre = element.getString("nombre");
            this.url = element.getString("url");
            this.grupo = element.getString("grupo");
            this.events = null;
        } catch (JSONException e) {
            Log.w(((Object) this).getClass().getName(), "Exception", e);
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "Agenda{" +
                "uid='" + uid + '\'' +
                ", nombre='" + nombre + '\'' +
                ", grupo='" + grupo + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
