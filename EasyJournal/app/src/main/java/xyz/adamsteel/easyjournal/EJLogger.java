package xyz.adamsteel.easyjournal;

import android.util.Log;

//Just making writing to LogCat a little easier.
public class EJLogger {

    public static void ejLog(String message){

        if(BuildConfig.DEBUG) {
            Log.d("EJLogs", message);
        }
    }
}
