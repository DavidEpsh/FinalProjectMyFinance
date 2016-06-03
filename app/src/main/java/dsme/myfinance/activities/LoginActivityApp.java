package dsme.myfinance.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dsme.myfinance.R;
import dsme.myfinance.fragments.fragmentSignUp;
import dsme.myfinance.models.ModelCloudDB;

public class LoginActivityApp extends AppCompatActivity {
    private static final String TAG = "LoginActivityApp";
    private static final int REQUEST_SIGNUP = 0;
    ProgressDialog progressDialog;

    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button loginButton;
    Button signUp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_local);
        ButterKnife.inject(this);

        progressDialog = new ProgressDialog(LoginActivityApp.this, R.style.AppTheme_Dark_Dialog);
        signUp = (Button)findViewById(R.id.btn_sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging In...");
        progressDialog.show();


        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        new ModelCloudDB().new LogIn() {
            @Override
            protected void onPostExecute(JSONObject json) {
                if (json == null) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivityApp.this, "Could not log in, please try again later", Toast.LENGTH_SHORT);
                }else{
                    getData();
                }
            }
        }.execute(email, password);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() ){ //|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Please enter a valid email address");
            valid = false;

        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Password ust be longer than 4 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void getData(){
        new ModelCloudDB().new
                GetAllData(){
                    @Override
                    protected void onPostExecute(String result){
                        if (result.equals("Success!")) {
                            progressDialog.dismiss();
                            finish();
                        }
                    }
                }.execute();
    }

    public void openFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_activity_container, new fragmentSignUp())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            moveTaskToBack(true);
        }
    }
}