package dsme.myfinance.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dsme.myfinance.R;
import dsme.myfinance.activities.MainActivity;
import dsme.myfinance.models.Model;
import dsme.myfinance.models.ModelCloudDB;
import dsme.myfinance.models.User;

public class fragmentSignUp extends Fragment {

    public fragmentSignUp() {
        // Required empty public constructor
    }

    View mRootView;
    EditText email;
    EditText firstName;
    EditText lastName;
    EditText phone;
    EditText userName;
    EditText password;
    Button signUp;
    ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);

        email = (EditText)mRootView.findViewById(R.id.input_email);
        firstName = (EditText)mRootView.findViewById(R.id.input_first_name);
        lastName = (EditText)mRootView.findViewById(R.id.input_last_name);
        userName = (EditText)mRootView.findViewById(R.id.input_username);
        phone = (EditText)mRootView.findViewById(R.id.input_phone);
        password = (EditText)mRootView.findViewById(R.id.input_password);
        signUp = (Button)mRootView.findViewById(R.id.btn_sign_up);

        email.setText("2@1.com");
        firstName.setText("dave6");
        lastName.setText("dave6");
        userName.setText("dave6");
        phone.setText("0500000000");
        password.setText("A1a1a1a1!!");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });

        return mRootView;
    }

    public void signUpUser(){
        boolean validInput = true;

        if(email.getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError("Please enter a valid email address");
            validInput = false;
        }else if(firstName.getText().toString().isEmpty()){
            firstName.setError("Please enter your first name");
            validInput = false;
        }else if(lastName.getText().toString().isEmpty()){
            lastName.setError("Please enter your last name");
            validInput = false;
        }else if(userName.getText().toString().isEmpty()){
            userName.setError("Please enter a user name");
            validInput = false;
        }else if(phone.getText().toString().length() < 10){
            phone.setError("Please enter a valid phone number");
            validInput = false;
        }else if(password.getText().length() < 6){
            password.setError("Password must be atleast 6 characters long");
        }else{
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Signing Up...");
            progressDialog.show();
        }

        if(validInput){
            final User.Customer user = new User.Customer(null,
                    null,
                    userName.getText().toString(),
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    email.getText().toString(),
                    phone.getText().toString(),
                    null,
                    password.getText().toString(),
                    null,
                    null);

            new ModelCloudDB(). new SignUp(){
                @Override
                protected void onPostExecute(String result){
                    if (result.equals("OK")) {
                        progressDialog.dismiss();
                        closeActivity();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Unable to sign up, please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(user);
        }
    }

    public void closeActivity(){
        getActivity().setResult(MainActivity.RESULT_OK);
        getActivity().finish();
    }

}
