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
    private static final String PROPERTY_REG_ID = "PROPERTY_REG_ID";
    private static final String APP_UNIQUE_ID = "UNIQUE_ID_COUNT";


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

    public static void setGCMID(Context context, String id) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(PROPERTY_REG_ID, id);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static String getGCMID(Context context) {
        try {
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getString(PROPERTY_REG_ID, "");
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    public static boolean getCalendarAlerts(Context context) {
        try {
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getBoolean(APP_CALENDAR_ALERTS, false);
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return false;
    }

    public static boolean getForumAlerts(Context context) {
        try {
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getBoolean(APP_FORUM_ALERTS, false);
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return false;
    }

    public static void setForumAlerts(Context context, boolean id) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putBoolean(APP_FORUM_ALERTS, id);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static void setCalendarAlerts(Context context, boolean id) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putBoolean(APP_CALENDAR_ALERTS, id);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static void setUniqueID(Context context, int id) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putInt(APP_UNIQUE_ID, id);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static int getUniqueID(Context context) {
        try {
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getInt(APP_UNIQUE_ID, 0);
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return -1;
    }
}
