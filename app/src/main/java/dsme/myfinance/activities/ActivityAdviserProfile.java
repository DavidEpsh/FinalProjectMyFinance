package dsme.myfinance.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import dsme.myfinance.R;
import dsme.myfinance.models.ModelCloudDB;

public class ActivityAdviserProfile extends AppCompatActivity {

    FloatingActionButton addAdviser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_adviser_profile);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_adviser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModelCloudDB().new GetAdvisers() {
                    @Override
                    protected void onPostExecute(List advisers) {
                        if (advisers == null) {

                        }else{
                        }
                    }
                }.execute();

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
