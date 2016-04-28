package dsme.myfinance.utils;

import android.content.Context;

public class SharedPreferences {

    public static final String PREFS_NAME = "DSME_APP";
    public static final String FAVORITES = "Product_Favorite";

    public void saveFavorites(Context context, String newCategory) {
        android.content.SharedPreferences settings;
        android.content.SharedPreferences.Editor editor;
        String[] categories;

        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        editor = settings.edit();


        editor.commit();
    }
}
