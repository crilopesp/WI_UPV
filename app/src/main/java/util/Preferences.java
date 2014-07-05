package util;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Preferences {
    private static final String APP_FILE = "APP_PREFERENCES";
    private static final String USER_FILE = "USER_PREFERENCES";
    private static final String USER_DNI = "DNI";
    private static final String USER_PIN = "PIN";
    private static final String APP_LENGUAGE = "LENGUAGE";
    private static final String APP_CALENDAR_ALERTS = "CALENDAR_ALERTS";
    private static final String APP_FORUM_ALERTS = "FORUM_ALERTS";


    //USER PREFERENCES
    public static String getUser(Context context) {
        try {
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getString(USER_DNI, "");
        } catch (Exception e) {
            Log.d(Preferences.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    public static String getPass(Context context) {
        try {
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getString(USER_PIN, "");
        } catch (Exception e) {
            Log.d(Preferences.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    public static void setPass(Context context, String pass) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(USER_PIN, pass);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferences.class.getSimpleName(), "Exception", e);
        }
    }

    public static void setUser(Context context, String user) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(USER_DNI, user);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferences.class.getSimpleName(), "Exception", e);
        }
    }

}
