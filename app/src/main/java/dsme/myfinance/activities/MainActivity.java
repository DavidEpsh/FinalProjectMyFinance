package dsme.myfinance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import dsme.myfinance.R;
import dsme.myfinance.fragments.ChatFragment;
import dsme.myfinance.fragments.ExpenseListFragment;
import dsme.myfinance.fragments.OverviewFragment;
import dsme.myfinance.fragments.adviserListFragment;
import dsme.myfinance.models.Model;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final static public String EXPENSE_ID = "EXPENSE_ID";
    final static public int ACTIVITY_SIGN_IN= 1000;
    final static public int ACTIVITY_SUBSCRIBE= 1001;
    final static public int ACTIVITY_ADD_EXPENSE= 1002;
    final static public int RESULT_OK= 1020;
    FloatingActionButton fab;
    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TransactionEditActivity.class);
                startActivityForResult(intent, ACTIVITY_ADD_EXPENSE);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View myAccount = navigationView.getHeaderView(0);
        if(Model.instance().getCustomer() != null) {
            ((TextView) myAccount.findViewById(R.id.nav_email)).setText(Model.instance().getCustomer().getDisplayName());
            ((TextView) myAccount.findViewById(R.id.nav_display_name)).setText(Model.instance().getCustomer().getEmail());
        }
        navigationView.setNavigationItemSelectedListener(this);

        Intent keyboardIntent = getIntent();
        if (keyboardIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String name = keyboardIntent.getStringExtra(Intent.EXTRA_TEXT);
            Intent intentNew = new Intent(MainActivity.this, TransactionEditActivity.class);
            intentNew.putExtra("name", name);
            startActivity(intentNew);
        }

        if(Model.instance().getCustomer() == null) {
            Model.instance().logOutUser();
            Intent intent = new Intent(MainActivity.this, LoginActivityApp.class);
            startActivityForResult(intent, ACTIVITY_SIGN_IN);
        }else{
            openFragment(new OverviewFragment());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, LoginActivityApp.class);
            startActivityForResult(intent, ACTIVITY_SIGN_IN);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_overview) {
            openFragment(new OverviewFragment());
        } else if (id == R.id.nav_expense_list) {
            openFragment(new ExpenseListFragment());

        } else if (id == R.id.nav_chat) {
//            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
//            startActivity(intent);
            openFragment(new ChatFragment());

        } else if (id == R.id.nav_adviser_list) {
            openFragment(new adviserListFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(final Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commitAllowingStateLoss();

        if (fragment instanceof ChatFragment){
            fab.setVisibility(View.INVISIBLE);
        }else if(fab.getVisibility() == View.INVISIBLE){
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ACTIVITY_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                openFragment(new OverviewFragment());
            }
        }else if(requestCode == ACTIVITY_SUBSCRIBE){
            if (resultCode == RESULT_OK){
                openFragment(new OverviewFragment());
                mView = findViewById(R.id.drawer_layout);
                if (data.getStringExtra(ActivityAdviserProfile.ADVISER_NAME) != null)
                    Snackbar.make(mView,
                            "Subscribed to " + data.getStringExtra(ActivityAdviserProfile.ADVISER_NAME) + " successfully",
                            Snackbar.LENGTH_LONG).show();
            }
        }else if(requestCode == ACTIVITY_ADD_EXPENSE){
            if(resultCode == RESULT_OK) {
                mView = findViewById(R.id.drawer_layout);
                if (data.getStringExtra(TransactionEditActivity.RETURN_EXPENSE_NAME) != null)
                    Snackbar.make(mView,
                            "\"" + data.getStringExtra(TransactionEditActivity.RETURN_EXPENSE_NAME) + "\"" + " was added to your expenses",
                            Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
