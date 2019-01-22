package xyz.adamsteel.easyjournal;

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

public class MainActivity extends AppCompatActivity implements EntriesFragment.OnFragmentInteractionListener, SettingsFragment.OnListFragmentInteractionListener {

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

    }

        @Override
        public void onFragmentInteraction(Uri uri){

        }

        @Override
        public void onListFragmentInteraction(DummyContent.DummyItem item) {
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

}
