package xyz.adamsteel.easyjournal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.net.Uri;

import xyz.adamsteel.easyjournal.dummy.DummyContent;
import xyz.adamsteel.easyjournal.SettingsFragment;

public class MainActivity extends AppCompatActivity implements EntriesFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;
    private android.support.v7.app.ActionBar mainActionBar;

    //Our fragments:
    Fragment eFragment;
    Fragment sFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        mainActionBar = getSupportActionBar();
        mainActionBar.setTitle(R.string.bar_title_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Fragments:
        eFragment = new EntriesFragment();
        sFragment = new SettingsFragment();
        Log.d("EJLogs", "oncreate");

        LoadFragment(eFragment);

        //First-run popup:
        if(!hasRunBefore()){
            onFirstRun();
        }

    }


        @Override
        public void onFragmentInteraction(Uri uri){

        }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                //When user switches to Journal tab:
                case R.id.navigation_home:
                    mainActionBar.setTitle(R.string.bar_title_main);

                    LoadFragment(eFragment);
                    Log.d("EJLOGS", "Changed to home tab");

                    return true;

                case R.id.navigation_settings:
                    //When user switches to Settings tab:
                    mainActionBar.setTitle(R.string.bar_title_settings);

                    LoadFragment(sFragment);
                    Log.d("EJLOGS", "Changed to settings tab");
                    return true;
            }
            return false;
        }
    };


    private void LoadFragment(Fragment fragment){
        Log.d("EJLogs", "LoadFragment");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Checks if the app has run before.
    private boolean hasRunBefore(){
        SharedPreferences prefs = getSharedPreferences("xyz.adamsteel.EasyJournal", MODE_PRIVATE);
        return prefs.getBoolean("appHasRunBefore", false); //Return false if value is false or if key doesn't exist.
    }

    //Anything we want to do the first time the app is run, such as the instructions\intro:
    private void onFirstRun(){
        Log.d("EJLogs", "App first run");

        //Add the 'has run before' boolean to the shared preferences.
        SharedPreferences prefs = getSharedPreferences("xyz.adamsteel.EasyJournal", MODE_PRIVATE);
        prefs.edit().putBoolean("appHasRunBefore", true).commit();


        //Showing first-run introduction popup:
        //TODO: Implement this.

    }
}
