package co.edu.udea.compumovil.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


/**
 * Created by Romero-Pc on 22/03/2016.
 */
public class ConfigDbManager {

    public static final String TABLE_NAME = "config";
    public static final String CONFIG_CODE ="config_code";
    public static final String COUNTER = "counter";
    public static final String SHORT = "shortRest";
    public static final String LONG = "longRest";
    public static final String VOLUME = "volume";
    public static final String VIBRATE = "vibrate";
    public static final String DEBUG = "debug";



    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + CONFIG_CODE + " text primary key,"
            + COUNTER + " text not null,"
            + SHORT + " text not null,"
            + LONG + " text not null,"
            +VOLUME+" text not null,"
            +VIBRATE+" TEXT NOT NULL,"
            +DEBUG+ " text not null);";

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public ConfigDbManager(Context context) {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    private ContentValues generateContentValues(String counter, String shortRest, String longRest, String volume, String vibrate, String debug) {
        ContentValues valores = new ContentValues();
        valores.put(CONFIG_CODE, "Value");
        valores.put(COUNTER, counter);
        valores.put(SHORT, shortRest);
        valores.put(LONG, longRest);
        valores.put(VOLUME, volume);
        valores.put(VIBRATE, vibrate);
        valores.put(DEBUG, debug);
        return valores;
    }

    public void insertConfig(String counter, String shortRest, String longRest, String volume, String vibrate, String debug) {
        if (!configExists()) {
            db.insert(TABLE_NAME, null, generateContentValues(counter,shortRest,longRest,volume,vibrate,debug));
        }
        else
            db.replace(TABLE_NAME,null,generateContentValues(counter,shortRest,longRest,volume,vibrate, debug));
    }

    public boolean configExists() {
        boolean flag=false;
        Cursor cursor = null;
        String flagEmpty="";
        try{
            cursor = db.rawQuery("SELECT * FROM config", null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                flagEmpty = cursor.getString(cursor.getColumnIndex("config_code"));
            }
            if (!flagEmpty.equals("")){flag=true;}
            return flag;
        }finally {
            cursor.close();
        }
    }

    public ArrayList<String> getAllConfig() {
        Cursor cursor = null;
        ArrayList<String> allConfig= new ArrayList<String>();
        try{
            cursor = db.rawQuery("SELECT * FROM config", null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                allConfig.add(cursor.getString(cursor.getColumnIndex("counter")));
                allConfig.add(cursor.getString(cursor.getColumnIndex("shortRest")));
                allConfig.add(cursor.getString(cursor.getColumnIndex("longRest")));
                allConfig.add(cursor.getString(cursor.getColumnIndex("volume")));
                allConfig.add(cursor.getString(cursor.getColumnIndex("vibrate")));
                allConfig.add(cursor.getString(cursor.getColumnIndex("debug")));
            }
        }finally {
            cursor.close();
        }
        return allConfig;
    }


    public void updateCount(String count) {
        String userPassword = "";
        db.rawQuery("UPDATE config SET counter =? WHERE config_code=?;", new String[] {count + "", "Value"});
    }

}