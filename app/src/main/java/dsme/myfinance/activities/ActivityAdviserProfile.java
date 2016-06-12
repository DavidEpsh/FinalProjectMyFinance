package dsme.myfinance.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import dsme.myfinance.R;
import dsme.myfinance.models.ModelCloudDB;
import dsme.myfinance.models.User;

public class ActivityAdviserProfile extends AppCompatActivity {
    public static String ADVISER_EXTRA = "adviser_extra";

    FloatingActionButton addAdviser;
    ImageView profileImageView;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_adviser);
        //noinspection ConstantConditions
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModelCloudDB().new GetAdvisers() {
                    @Override
                    protected void onPostExecute(List advisers) {
                        if (advisers == null) {
                            setResult(MainActivity.RESULT_OK);
                            finish();
                        }else{

                        }
                    }
                }.execute();

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent() != null){
            String adviserTmp = getIntent().getStringExtra(ADVISER_EXTRA);
            Gson gson = new Gson();
            User.Adviser adviser = gson.fromJson(adviserTmp, User.Adviser.class);

            displayName = adviser.getDisplayName();
            profilePicture = adviser.getProfileImage();
            description = adviser.getDescription();
            id = adviser.getId();
            phoneNumber = adviser.getPhoneNumber();
            email = adviser.getEmail();

            Picasso.with(this).load(ModelCloudDB.API_URL_ADVISER_PIC + id + ".jpg").into(profileImageView);
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
