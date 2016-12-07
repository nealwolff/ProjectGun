//DatabaseHelper.java

package com.example.we00401.guntest2;

/**
 * Created by Joshua on 12/1/16.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // contacts table
    private static final String DATABASE_NAME = "guns.db";
    private static final String TABLE_NAME = "contacts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_UNAME = "uname";
    private static final String COLUMN_PASS = "pass";

    // savaSearch table
    private static final String TABLE_NAME2 = "savesearch";
    private static final String COLUMN_NAMESAVE = "namesave";
    //private static final String COLUMN_USER = "uname";


    // AKFiles
    private static final String TABLE_NAME3 = "AKFiles";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_IMAGEURL = "imageurl";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_TITLE = "title";




    SQLiteDatabase db;

    private static final String TABLE_CREATE = "create table contacts (id integer primary key not null , " +
            "name text not null , email text not null , uname text not null , pass text not null);";

    private static final String TABLE_CREATE2 = "create table savesearch (namesave text primary key not null , " +
            "uname text foreign key not null);";

    private static final String TABLE_CREATE3 = "create table akfiles (id integer primary key not null , " +
            "url text not null , imageurl text not null, price text not null, title text not null);";


    public DatabaseHelper(Context context) {
        super(context , DATABASE_NAME , null , DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_CREATE2);
        db.execSQL(TABLE_CREATE3);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String query2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;
        String query3 = "DROP TABLE IF EXISTS " + TABLE_NAME3;

        db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(query3);

        this.onCreate(db);
    }

    // insert values into the contact table
    public void insertContact(Contact c) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "SELECT * FROM contacts";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_NAME, c.getName());
        values.put(COLUMN_EMAIL , c.getEmail());
        values.put(COLUMN_UNAME, c.getUname());
        values.put(COLUMN_PASS, c.getPass());


        db.insert(TABLE_NAME, null, values);
        db.close();
    }

//    // insert values into the contact table
//    public void insertSaveSearch(Contact c) {
//        db = this.getWritableDatabase();
//        ContentValues values2 = new ContentValues();
//
//        String query = " SELECT * FROM savesearch";
//        Cursor cursor = db.rawQuery(query, null);
//        int count = cursor.getCount();
//
//        values2.put(COLUMN_NAMESAVE, c.getNameSave());
//        values2.put(COLUMN_UNAME, c.getUname());
//
//        db.insert(TABLE_NAME2, null, values2);
//        db.close();
//    }

    public String searchPass(String uname) {
        db = this.getReadableDatabase();
        String query = "SELECT uname, pass FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String a, b;
        b = "not found";
        if (cursor.moveToFirst()) {
            do {
                a = cursor.getString(0);

                if (a.equals(uname)) {
                    b = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        return b;
    }

    public String checkUser(String uname) {
        db = this.getReadableDatabase();
        String query = "SELECT uname FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String a;
        String b = "already exists";

        if (cursor.moveToFirst()) {
            do {
                a = cursor.getString(0);

                if (a.equals(uname)) {
                    return b;
                }
            }
            while(cursor.moveToNext());
        }
        return uname;
    }


}
