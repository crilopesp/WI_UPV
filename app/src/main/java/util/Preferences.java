package util;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Preferences {

    private static final String USER_FILE = "USER_PREFERENCES";
    private static final String USER = "USER";
    private static final String PASS = "PASS";

    public static String getUser(Context context){
        try{
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getString(USER, "");
        }
        catch (Exception e){
            Log.d(Preferences.class.getSimpleName(), "Exception" , e);
        }
        return null;
    }

    public static String getPass(Context context){
        try{
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getString(PASS, "");
        }
        catch (Exception e){
            Log.d(Preferences.class.getSimpleName(), "Exception" , e);
        }
        return null;
    }

    public static void setPass(Context context, String pass){
        try{
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(PASS, pass);
            editor.commit();
        }
        catch (Exception e){
            Log.d(Preferences.class.getSimpleName(), "Exception" , e);
        }
    }

    public static void setUser(Context context, String user){
        try{
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(USER, user);
            editor.commit();
        }
        catch (Exception e){
            Log.d(Preferences.class.getSimpleName(), "Exception" , e);
        }
    }

}
