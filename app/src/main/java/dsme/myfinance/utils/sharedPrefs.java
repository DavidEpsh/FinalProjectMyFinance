package dsme.myfinance.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    public static String isSubscribedToAdviser = "is_subscribed";
    public static String USER_ID = "user_id";
    public static String USER_EMAIL = "user_email";

    public static void writeStringToPrefs(Activity activity, String name, String value){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static String readStringFromPrefs(Activity activity, String name){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String value = sharedPref.getString(name, "");

        return value;
    }

    public static void writeIntToPrefs(Activity activity, String name, int value){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public static int readIntFromPrefs(Activity activity, String name){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int value = sharedPref.getInt(name, 0);

        return value;
    }
}
