package com.shreyasbhandare.ruevents.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RUEventsApp.db";
    private static final String TABLE_NAME = "OrganizationInfo";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "Name" ;
    public static final String COLUMN_PHOTO =  "Photo";

    private static final String TABLE2_NAME = "TodaysEvent";
    private static final String COLUMN2_ID = "id";
    private static final String COLUMN2_NAME = "Name" ;
    private static final String COLUMN2_ORGANIZATION = "Organization";
    private static final String COLUMN2_DATE = "Date" ;
    private static final String COLUMN2_PHOTO = "Photo" ;

    public SQLiteDatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, 1);
    }

    // OnCreate method. Called when helper is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Note: do not use AUTO INCREMENT on a primary key integer
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID + " TEXT PRIMARY KEY NOT NULL, " +
                COLUMN_NAME + " TEXT, " + COLUMN_PHOTO + " TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE2_NAME + " (" + COLUMN2_ID + " TEXT PRIMARY KEY NOT NULL, " +
                COLUMN2_NAME + " TEXT, " +
                COLUMN2_ORGANIZATION + " TEXT, " +
                COLUMN2_DATE + " TEXT, " +
                COLUMN2_PHOTO + " TEXT);");
        System.out.println("Tables are created");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE2_NAME);
        onCreate(db);
    }

    // Custom method to insert stuff
    public void insertEntry(String id, String name, String photo){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(COLUMN_ID, id);
        content.put(COLUMN_NAME, name);
        content.put(COLUMN_PHOTO, photo);
        db.insert(TABLE_NAME,null,content);
    }

    // Custom method to return entries ascending
    public Cursor getEntry(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
    }

    public void insertEventEntry(String id, String name, String org, String date, String photo){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(COLUMN2_ID, id);
        content.put(COLUMN2_NAME, name);
        content.put(COLUMN2_ORGANIZATION, org);
        content.put(COLUMN2_DATE, date);
        content.put(COLUMN2_PHOTO, photo);
        db.insert(TABLE2_NAME,null,content);
    }

    public Cursor getEventEntry(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE2_NAME,null);
    }

    public void deleteEventEntry(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE2_NAME);
    }

    public int getNumberOfEvents(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE2_NAME,null);
        int result = c.getCount();
        c.close();
        return result;
    }
}
