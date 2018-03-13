package limited.it.planet.incomingcallrecordapp.database;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import limited.it.planet.incomingcallrecordapp.constant.Constants;
import limited.it.planet.incomingcallrecordapp.util.MyPhoneReceiver;
import limited.it.planet.incomingcallrecordapp.util.ViewLogModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Tarikul on 2/26/2018.
 */

public class DataHelper {
    // db version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "incoming_call_tracking";
    private static final String DATABASE_TABLE_CALL_TRACKING = "table_call_tracking";
    private DataHelper.DBHelper dbhelper;
    private final Context context;
    private SQLiteDatabase database;

    // insert row
    public static final String KEY_ROWID = "id";
    public static final String KEY_MOBILE_NUMBER = "mobile_number";
    public static final String KEY_DATE = "date";
    //public static final String KEY_VOICE_FILE_PATH = "voice_file_path";
    public static final String KEY_SMS_TEXT = "sms_text";
    public static final String KEY_SYNC_STATUS = "sync_status";
    public static final String KEY_TIME = "time";

    //to use print
    public static final String INSERT_LOG = Constants.LOG_TAG_INSERT;
    public static final String UPDATE_LOG = Constants.LOG_TAG_UPDATE;


    private static class DBHelper extends SQLiteOpenHelper {

        @SuppressLint("NewApi")
        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            // create table to store msgs
            db.execSQL(" CREATE TABLE " + DATABASE_TABLE_CALL_TRACKING + " ("
                    + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_MOBILE_NUMBER + " TEXT, "
                    + KEY_SMS_TEXT + " TEXT, "
                    + KEY_DATE + " TEXT, "
                    + KEY_TIME + " TEXT, "
                    + KEY_SYNC_STATUS + " TEXT );");


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_CALL_TRACKING);


            onCreate(db);
        }

    }
    // constructor
    public DataHelper(Context c) {
        context = c;
    }

    // open db
    public DataHelper open() {
        dbhelper = new  DBHelper(context);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    // close db
    public void close() {
        dbhelper.close();
    }


    public long saveUserTrackingInfo(String mobNumber,String smsText,String date,String time,String syncStatus){
        ContentValues cv = new ContentValues();
        cv.put(KEY_MOBILE_NUMBER, mobNumber);
        cv.put(KEY_SMS_TEXT, smsText);
        cv.put(KEY_DATE, date);
        cv.put(KEY_TIME, time);
        cv.put(KEY_SYNC_STATUS, syncStatus);
        long dbInsert = database.insert(DATABASE_TABLE_CALL_TRACKING, null, cv);

        if(dbInsert != -1) {
            Log.d(INSERT_LOG,String.valueOf(dbInsert));
            Toast.makeText(context, "New row added in Basic Information, row id: " + dbInsert, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
        }



        return dbInsert;
    }

    public void updateSyncStatus(String syncResult){
        ContentValues cv = new ContentValues();
        cv.put(KEY_SYNC_STATUS, syncResult);
        long dbUpdate =  database.update(DATABASE_TABLE_CALL_TRACKING, cv, "id=" + KEY_ROWID, null);
        if(dbUpdate != -1) {
            Log.d(UPDATE_LOG,String.valueOf(dbUpdate));
        }
    }

    public  String selectRowID (String mobileNumber){
        String rowId = "";
        String select_query = "SELECT  id FROM " + DATABASE_TABLE_CALL_TRACKING +
                " WHERE " + KEY_MOBILE_NUMBER + " = '" + mobileNumber + "'" ;
        Cursor cursor = database.rawQuery(select_query,null);
        int iRowId = cursor.getColumnIndex(KEY_ROWID);

        for (cursor.moveToLast(); ! cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            rowId = cursor.getString(iRowId);

        }

        return  rowId;
    }


    public ArrayList getInputData(){

        ArrayList<ViewLogModel> viewLogList = new ArrayList<>();
        String select_query = "SELECT  * FROM " + DATABASE_TABLE_CALL_TRACKING ;


        Cursor cursor = database.rawQuery(select_query,null);

        // if(cursor != null && cursor.moveToFirst()){
        //int iDbId = cursor.getColumnIndex(KEY_ROWID);
        int iUserNumber = cursor.getColumnIndex(KEY_MOBILE_NUMBER);
        int iSMSText = cursor.getColumnIndex(KEY_SMS_TEXT);
        int iDate = cursor.getColumnIndex(KEY_DATE);
        int iTime = cursor.getColumnIndex(KEY_TIME);
        int iSyncStatus = cursor.getColumnIndex(KEY_SYNC_STATUS);

        for (cursor.moveToLast(); ! cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            //    for (cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()) {

            ViewLogModel viewLogModel = new ViewLogModel();
            viewLogModel.setUserNumber(cursor.getString(iUserNumber));
            viewLogModel.setSmsText(cursor.getString(iSMSText));
            viewLogModel.setDate(cursor.getString(iDate));
            viewLogModel.setTime(cursor.getString(iTime));
            viewLogModel.setSyncStatus(cursor.getString(iSyncStatus));

            viewLogList.add(viewLogModel);


        }
        cursor.close();
        return viewLogList;
    }

    public void clearLog(){
        database.execSQL("delete from "+ DATABASE_TABLE_CALL_TRACKING);
    }

    public Cursor selectALlRecords() {

        Cursor c = null;
        try {
            c = database.rawQuery("Select * from "
                    + DATABASE_TABLE_CALL_TRACKING, null);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return c;
    }

    //Check Failled Data to database
    public ArrayList checkFailedDataFromTable(String syncStatus){

        ArrayList<ViewLogModel> viewLogList = new ArrayList<>();
        String select_query = "SELECT * FROM " + DATABASE_TABLE_CALL_TRACKING + " WHERE " + KEY_SYNC_STATUS + " = '" + syncStatus + "'";

        Cursor cursor = database.rawQuery(select_query,null);
        //int iDbId = cursor.getColumnIndex(KEY_ROWID);
        int iUserNumber = cursor.getColumnIndex(KEY_MOBILE_NUMBER);
        int iSMSText = cursor.getColumnIndex(KEY_SMS_TEXT);
        int iDate = cursor.getColumnIndex(KEY_DATE);
        int iTime = cursor.getColumnIndex(KEY_TIME);
        int iSyncStatus = cursor.getColumnIndex(KEY_SYNC_STATUS);

        for (cursor.moveToLast(); ! cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            //    for (cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()) {

            ViewLogModel viewLogModel = new ViewLogModel();
            viewLogModel.setUserNumber(cursor.getString(iUserNumber));
            viewLogModel.setSmsText(cursor.getString(iSMSText));
            viewLogModel.setDate(cursor.getString(iDate));
            viewLogModel.setTime(cursor.getString(iTime));
            viewLogModel.setSyncStatus(cursor.getString(iSyncStatus));

            viewLogList.add(viewLogModel);

        }
        cursor.close();
        return viewLogList;
    }


}
