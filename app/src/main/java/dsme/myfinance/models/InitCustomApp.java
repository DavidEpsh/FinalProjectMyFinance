package dsme.myfinance.models;

import android.app.Application;
import android.provider.Settings;

public class InitCustomApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Model.instance().init(getApplicationContext());

        Settings.System.putInt(null,"rape", 12);
    }
}