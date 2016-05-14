package dsme.myfinance.utils;

import android.app.Application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatApplication extends Application{

    public static final String CHAT_SERVER_URL = "https://myfinance-mean.herokuapp.com/socket.io/";

        private Socket mSocket;
        {
            try {
                mSocket = IO.socket(CHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        public Socket getSocket() {
            return mSocket;
        }
}
