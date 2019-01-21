package xyz.adamsteel.easyjournal;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObservable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class ESQLiteHelper extends SQLiteOpenHelper {


    //Database version:
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EntriesDB";
    private static final String MAIN_TABLE_NAME = "Entries";
    //private static final String

    public ESQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase eDBase){
        Log.d("EJLogs", "ESQLiteHelper oncreate running");


        String createTable = "CREATE TABLE Entries(_id INTEGER PRIMARY KEY, entry TEXT, timestamp TEXT );";

        eDBase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase eDBase, int oldVer, int newVer){

        Log.d("EJLogs", "onUpgrade called ");

        if(newVer > oldVer){
            switch (newVer){
                case 2:
                    //Upgrade stuff?
                    break;
            }
        }

    }

    public void addEntry(String entry){
        Log.d("EJLogs", "Attempting to add entry");
        String toInsert = "Test entry";
        SQLiteDatabase eDBase = this.getWritableDatabase();

        ContentValues cValues = new ContentValues();
        cValues.put("entry", entry);
        cValues.put("timestamp", "CURRENT_TIMESTAMP");

        eDBase.insert(MAIN_TABLE_NAME, null, cValues);
    }

    //Retrives the most recent entries in the database. Count = how many.
    public ArrayList<String> retrieveLastEntries(int count){

        ArrayList<String> lastEntries = new ArrayList<String>(count);

        SQLiteDatabase eDBase = this.getReadableDatabase();

        Cursor eCursor = eDBase.query("Entries", null, null, null, null, null, "_id DESC", Integer.toString(count)); //Gets everything in the Entries table.

        eCursor.moveToLast(); //Flipped it to go backwards as the descending order of the selection makes them come out backwards.

        Log.d("EJLogs", "Attempting to retrieve entries... count: " + eCursor.getCount());

        do {
            Log.d("EJLogs", eCursor.getString(1));

            lastEntries.add(eCursor.getString(1));
        }
        while(eCursor.moveToPrevious());


        return lastEntries;
    }


    //Dumps the entire database to logcat. Only for testing.
    public void dumpEntries(){


        SQLiteDatabase eDBase = this.getReadableDatabase();

        Cursor eCursor = eDBase.query("Entries", null, null, null, null, null, null); //Gets everything in the Entries table.

        eCursor.moveToFirst();

        Log.d("EJLogs", "Attempting to dump entries... count: " + eCursor.getCount());

        while(eCursor.moveToNext()){
            Log.d("EJLogs", eCursor.getString(0) + eCursor.getString(1) + eCursor.getString(2));
        }

    }
}
