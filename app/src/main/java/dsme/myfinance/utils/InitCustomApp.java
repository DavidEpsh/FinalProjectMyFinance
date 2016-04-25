package dsme.myfinance.utils;

import android.app.Application;
import android.provider.Settings;

import dsme.myfinance.models.Model;

public class InitCustomApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Model.instance().init(getApplicationContext());
    }
}