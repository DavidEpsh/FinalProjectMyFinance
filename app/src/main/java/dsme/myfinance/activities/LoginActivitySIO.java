package dsme.myfinance.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import dsme.myfinance.R;
import dsme.myfinance.models.Model;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * A login screen that offers login via username.
 */
public class LoginActivitySIO extends Activity {

    private EditText mUsernameView;

    private String mUsername;

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            mSocket = IO.socket("http://chat.socket.io/");
        } catch (URISyntaxException e) {}

        mUsername = Model.instance().getCustomer().getFirstName();
        //attemptLogin();
        mSocket.on("login", onLogin);
        mSocket.connect();

        Intent intent = new Intent();
        intent.putExtra("username", mUsername);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.off("login", onLogin);
    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        mSocket.emit("add user", Model.instance().getCustomer().getDisplayName());
    }

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];

            int numUsers;
            try {
                numUsers = data.getInt("numUsers");
            } catch (JSONException e) {
                return;
            }

            Intent intent = new Intent();
            intent.putExtra("username", mUsername);
            intent.putExtra("numUsers", numUsers);
            setResult(RESULT_OK, intent);
            finish();
        }
    };
}



