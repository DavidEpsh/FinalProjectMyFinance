package dsme.myfinance.utils;

import android.app.Application;
import android.content.ContextWrapper;
import android.provider.Settings;

import com.pixplicity.easyprefs.library.Prefs;

import java.net.URISyntaxException;

import dsme.myfinance.models.Model;
import io.socket.client.IO;
import io.socket.client.Socket;

public class InitCustomApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Model.instance().init(getApplicationContext());
    }
}