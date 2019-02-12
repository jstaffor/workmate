package jstaffor.android.jobsight.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.DatabaseModel;

public class DatabaseModelHelper extends SQLiteOpenHelper
{
    private static final String TAG = "DatabaseModelHelper";

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "jobsight";

    public DatabaseModelHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {

        //**************************
        //PRIMARY TABLES
        //**************************
        db.execSQL(DatabaseModel.SQL_CREATE_TABLE_USER);
        db.execSQL(DatabaseModel.SQL_CREATE_TABLE_PARENT);
        db.execSQL(DatabaseModel.SQL_CREATE_TABLE_CHILD);
        db.execSQL(DatabaseModel.SQL_CREATE_TABLE_TEMPLATE);
        db.execSQL(DatabaseModel.SQL_CREATE_TABLE_INVOICE);

        //**************************
        //DATA TABLES
        //**************************
        db.execSQL(DatabaseModel.SQL_CREATE_TABLE_TEXT);
        db.execSQL(DatabaseModel.SQL_CREATE_TABLE_PHOTO);
        db.execSQL(DatabaseModel.SQL_CREATE_TABLE_LOCATION);
        db.execSQL(DatabaseModel.SQL_CREATE_TABLE_SKETCH);
        db.execSQL(DatabaseModel.SQL_CREATE_TABLE_AUDIO_RECORDING);
        db.execSQL(DatabaseModel.SQL_CREATE_TABLE_VIDEO_RECORDING);
        db.execSQL(DatabaseModel.SQL_CREATE_TABLE_FILE_RECORDING);

        //Default Values
        db.execSQL(DatabaseModel.SQL_CREATE_USER_DEFAULT_VALUES);
        db.execSQL(DatabaseModel.SQL_CREATE_PARENT_DEFAULT_VALUES);
        db.execSQL(DatabaseModel.SQL_CREATE_TEMPLATE_DEFAULT_VALUES);
        db.execSQL(DatabaseModel.SQL_CREATE_INVOICE_DEFAULT_VALUES);

 //       if(AppSettings.DEBUG_MODE) {
            Log.d(TAG, "DatabaseModel.SQL_CREATE_TABLE_TEXT | " + DatabaseModel.SQL_CREATE_TABLE_TEXT);
            Log.d(TAG, "DatabaseModel.SQL_CREATE_TABLE_PARENT | " + DatabaseModel.SQL_CREATE_TABLE_PARENT);
            Log.d(TAG, "DatabaseModel.SQL_CREATE_TABLE_CHILD | " + DatabaseModel.SQL_CREATE_TABLE_CHILD);
            Log.d(TAG, "DatabaseModel.SQL_CREATE_TABLE_TEMPLATE | " + DatabaseModel.SQL_CREATE_TABLE_TEMPLATE);
            Log.d(TAG, "DatabaseModel.SQL_CREATE_TABLE_INVOICE | " + DatabaseModel.SQL_CREATE_TABLE_INVOICE);
            Log.d(TAG, "DatabaseModel.SQL_CREATE_TABLE_USER | " + DatabaseModel.SQL_CREATE_TABLE_USER);
            Log.d(TAG, "DatabaseModel.SQL_CREATE_TABLE_PHOTO | " + DatabaseModel.SQL_CREATE_TABLE_PHOTO);
            Log.d(TAG, "DatabaseModel.SQL_CREATE_TABLE_LOCATION | " + DatabaseModel.SQL_CREATE_TABLE_LOCATION);
            Log.d(TAG, "DatabaseModel.SQL_CREATE_TABLE_SKETCH | " + DatabaseModel.SQL_CREATE_TABLE_SKETCH);
            Log.d(TAG, "DatabaseModel.SQL_CREATE_TABLE_AUDIO_RECORDING | " + DatabaseModel.SQL_CREATE_TABLE_AUDIO_RECORDING);
            Log.d(TAG, "DatabaseModel.SQL_CREATE_TABLE_VIDEO_RECORDING | " + DatabaseModel.SQL_CREATE_TABLE_VIDEO_RECORDING);
            Log.d(TAG, "DatabaseModel.SQL_CREATE_TABLE_FILE_RECORDING | " + DatabaseModel.SQL_CREATE_TABLE_FILE_RECORDING);
            Log.d(TAG, "DatabaseModel.SQL_CREATE_PARENT_DEFAULT_VALUES | " + DatabaseModel.SQL_CREATE_PARENT_DEFAULT_VALUES);
            Log.d(TAG, "DatabaseModel.SQL_CREATE_TEMPLATE_DEFAULT_VALUES | " + DatabaseModel.SQL_CREATE_TEMPLATE_DEFAULT_VALUES);
            Log.d(TAG, "DatabaseModel.SQL_CREATE_INVOICE_DEFAULT_VALUES | " + DatabaseModel.SQL_CREATE_INVOICE_DEFAULT_VALUES);
  //      }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
//        db.execSQL(SQL_DELETE_ENTRIES);
//        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
