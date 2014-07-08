package util;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Preferencias {
    private static final String APP_FILE = "APP_PREFERENCES";
    private static final String USER_FILE = "USER_PREFERENCES";
    private static final String USER_DNI = "DNI";
    private static final String USER_PIN = "PIN";
    private static final String APP_LENGUAGE = "LENGUAGE";
    private static final String APP_CALENDAR_ALERTS = "CALENDAR_ALERTS";
    private static final String APP_FORUM_ALERTS = "FORUM_ALERTS";
    private static final String USER_USERNAME = "USERNAME";


    //USER PREFERENCES
    public static String getDNI(Context context) {
        try {
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getString(USER_DNI, "");
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    public static String getPIN(Context context) {
        try {
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getString(USER_PIN, "");
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    public static void setPIN(Context context, String pass) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(USER_PIN, pass);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static void setDNI(Context context, String user) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(USER_DNI, user);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static String getUsername(Context context) {
        try {
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getString(USER_USERNAME, "");
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    public static void setUsername(Context context, String username) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(USER_USERNAME, username);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }
}
