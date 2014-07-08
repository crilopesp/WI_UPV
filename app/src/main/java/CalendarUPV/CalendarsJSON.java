package calendarupv;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CalendarsJSON {

    private String username;
    private List<DiaryJSON> diaries;

    public CalendarsJSON(String data) {
        diaries = new ArrayList<DiaryJSON>();
        this.parseJson(data);
    }

    private void parseJson(String data) {

        try {

            JSONObject jObject = new JSONObject(data);

            //username
            this.username = jObject.getString("username");
            Log.e("username", username);

            //agendas...
            JSONArray agendasArray = jObject.getJSONArray("agendas");
            for (int i = 0; i < agendasArray.length(); i++) {
                try {

                    this.diaries.add(new DiaryJSON(agendasArray.getJSONObject(i)));

                } catch (JSONException e) {
                    Log.w(((Object) this).getClass().getName(), "Exception", e);
                }
            }
        } catch (JSONException e) {
            Log.w(((Object) this).getClass().getName(), "Exception", e);
        }
    }

    public void setDiaries(List<DiaryJSON> diaries) {
        this.diaries = diaries;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<DiaryJSON> getDiaries() {
        return diaries;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        for (DiaryJSON item : this.diaries) {
            stringBuilder.append(item.toString());
        }

        return "Calendars{" +
                "username='" + username + '\'' +
                ", agendas=" + stringBuilder.toString() +
                '}';
    }
}


