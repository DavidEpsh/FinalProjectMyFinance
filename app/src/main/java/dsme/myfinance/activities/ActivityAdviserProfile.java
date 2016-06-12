package dsme.myfinance.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import dsme.myfinance.R;
import dsme.myfinance.models.ModelCloudDB;
import dsme.myfinance.models.User;

public class ActivityAdviserProfile extends AppCompatActivity {
    public static String ADVISER_EXTRA = "adviser_extra";
    public static String ADVISER_NAME = "subscribe";

    User.Adviser adviser;
    JSONObject adviserJson;

    ImageView profileImageView;
    TextView aboutMe;
    Button sendSms;
    Button sendEmail;
    Button openDialer;

    String displayName;
    String profilePicture;
    String description;
    String id;
    String email;
    String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_adviser_profile);
        setSupportActionBar(toolbar);

        profileImageView = (ImageView) findViewById(R.id.adviser_profile_image);
        aboutMe = (TextView) findViewById(R.id.adviser_about_me);
        sendEmail = (Button) findViewById(R.id.email_adviser);
        sendSms = (Button) findViewById(R.id.send_sms);
        openDialer = (Button) findViewById(R.id.call_adviser);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_adviser);
        //noinspection ConstantConditions
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new ModelCloudDB(). new AssociateAdviser() {
                    @Override
                    protected void onPostExecute(String result) {
                        if (result.equals("OK")) {
                            Intent intent = new Intent();
                            intent.putExtra(ADVISER_NAME, adviser.getDisplayName());
                            setResult(MainActivity.RESULT_OK, intent);
                            finish();
                        }else{

                        }
                    }
                }.execute(adviserJson);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {email});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Financial Advice");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hello " + displayName + ",");
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
            }
        });

        openDialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });

        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", phoneNumber);
                smsIntent.putExtra("sms_body","Hello " + displayName +",");
                startActivity(smsIntent);
            }
        });
        if(getIntent() != null){
            String adviserTmp = getIntent().getStringExtra(ADVISER_EXTRA);
            Gson gson = new Gson();
            adviser = gson.fromJson(adviserTmp, User.Adviser.class);

            try {
                adviserJson = new JSONObject(adviserTmp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            displayName = adviser.getDisplayName();
            profilePicture = adviser.getProfileImage();
            description = adviser.getDescription();
            id = adviser.getId();
            phoneNumber = adviser.getPhoneNumber();
            email = adviser.getEmail();

            setTitle(displayName);
            aboutMe.setText(description);

//            Picasso.with(this).load(ModelCloudDB.API_URL_ADVISER_PIC + id).into(profileImageView);
            Picasso.with(this).load("http://vignette1.wikia.nocookie.net/family-guy-fanverse/images/d/d7/Stewie-griffin.gif/revision/latest?cb=20140215140724").into(profileImageView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
