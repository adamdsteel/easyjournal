package xyz.adamsteel.easyjournal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;
import android.net.Uri;

import static xyz.adamsteel.easyjournal.EJLogger.ejLog;

public class MainActivity extends AppCompatActivity implements EntriesFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, DeleteDialogFragment.DeleteDialogListener {

    private TextView mTextMessage;
    private android.support.v7.app.ActionBar mainActionBar;

    //Our fragments:
    Fragment eFragment;
    Fragment sFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ejLog("onCreate");

        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        mainActionBar = getSupportActionBar();
        mainActionBar.setTitle(R.string.bar_title_main);

        mainActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099ff")));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Fragments:
        eFragment = new EntriesFragment();
        sFragment = new SettingsFragment();

        LoadFragment(eFragment);

        //First-run popup:
        if(!hasRunBefore()){
            onFirstRun();
        }

    }

    @Override
    protected void onStart(){
        super.onStart();
        ejLog("onStart()");
    }

    @Override
    protected void onResume(){
        super.onResume();
        ejLog("onResume");
    }

    @Override
    protected void onStop(){
        super.onStop();
        ejLog("onStop");
    }

    @Override
    protected void onPause(){
        super.onPause();
        ejLog("onPause");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ejLog("onDestroy");
    }


        @Override
        public void onFragmentInteraction(Uri uri){

        }

    @Override
    public void longPress(int id) {

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
                    //ejLog( "Changed to home tab");

                    return true;

                case R.id.navigation_settings:
                    //When user switches to Settings tab:
                    mainActionBar.setTitle(R.string.bar_title_settings);

                    LoadFragment(sFragment);
                    //ejLog( "Changed to settings tab");
                    return true;
            }
            return false;
        }
    };


    private void LoadFragment(Fragment fragment){
        //ejLog( "LoadFragment");
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
        ejLog("App first run");

        //Add the 'has run before' boolean to the shared preferences.
        SharedPreferences prefs = getSharedPreferences("xyz.adamsteel.EasyJournal", MODE_PRIVATE);
        prefs.edit().putBoolean("appHasRunBefore", true).commit();


        //Showing first-run introduction popup:
        FirstDialogFragment fdFragment = new FirstDialogFragment();
        fdFragment.show(getSupportFragmentManager(), "firstDialog");
    }

    public void emailDeveloper(View view){
        Intent contactIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getResources().getString(R.string.dev_email), null));
        contactIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.email_subject);
        contactIntent.putExtra(Intent.EXTRA_TEXT, R.string.email_contents);

        startActivity(contactIntent);
    }


    //Implementing DeleteDialogFragment.DeleteDialogListener
    //The interface for the delete dialog buttons:
    @Override
    public void onDeleteConfirm(int dbID){
        ((EntriesFragment)eFragment).deleteEntry(dbID);
    }

    public void onDeleteCancel(){

    }

}
