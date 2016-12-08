//DatabaseHelper.java

package com.example.we00401.guntest2;

/**
 * Created by Joshua on 12/1/16.
 */

import android.app.ExpandableListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.provider.Settings;

import java.util.ArrayList;

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
    private static final String TABLE_NAME3 = "akfiles";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_IMAGEURL = "imageurl";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_TITLE = "title";


    // GunBroker
    private static final String TABLE_NAME4 = "gunbroker";

    // ArmsList
    private static final String TABLE_NAME5 = "armslist";

    // FALfiles
    private static final String TABLE_NAME6 = "falfiles";

    // GunsAmerica
    private static final String TABLE_NAME7 = "gunsamerica";

    // CalGuns
    private static final String TABLE_NAME8 = "calguns";



    SQLiteDatabase db;

    private static final String TABLE_CREATE = "create table contacts (id integer primary key AUTOINCREMENT, uname text not null, " +
            "name text not null , email text not null , pass text not null);";

    private static final String TABLE_CREATE2 = "create table savesearch (id integer primary key AUTOINCREMENT, namesave text not null, "
            + "category text not null, searchterm text not null, uname text not null, FOREIGN KEY (uname) REFERENCES contacts (uname));";

    private static final String TABLE_CREATE3 = "create table akfiles (id integer primary key AUTOINCREMENT, " +
            "url text not null , imageurl text not null, price text not null, title text not null, id2 integer not null,"
            + " FOREIGN KEY (id2) REFERENCES savesearch (id));";

    private static final String TABLE_CREATE4 = "create table gunbroker (id integer primary key AUTOINCREMENT, " +
            "url text not null , imageurl text not null, price text not null, title text not null, id2 integer not null,"
            + " FOREIGN KEY (id2) REFERENCES savesearch (id));";

    private static final String TABLE_CREATE5 = "create table armslist (id integer primary key AUTOINCREMENT, " +
            "url text not null , imageurl text not null, price text not null, title text not null, id2 integer not null,"
            + " FOREIGN KEY (id2) REFERENCES savesearch (id));";

    private static final String TABLE_CREATE6 = "create table falfiles (id integer primary key AUTOINCREMENT, " +
            "url text not null , imageurl text not null, price text not null, title text not null, id2 integer not null,"
            + " FOREIGN KEY (id2) REFERENCES savesearch (id));";

    private static final String TABLE_CREATE7 = "create table gunsamerica (id integer primary key AUTOINCREMENT, " +
            "url text not null , imageurl text not null, price text not null, title text not null, id2 integer not null,"
            + " FOREIGN KEY (id2) REFERENCES savesearch (id));";

    private static final String TABLE_CREATE8 = "create table calguns (id integer primary key AUTOINCREMENT, " +
            "url text not null , imageurl text not null, price text not null, title text not null, id2 integer not null,"
            + " FOREIGN KEY (id2) REFERENCES savesearch (id));";


    public DatabaseHelper(Context context) {
        super(context , DATABASE_NAME , null , DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_CREATE2);
        db.execSQL(TABLE_CREATE3);
        db.execSQL(TABLE_CREATE4);
        db.execSQL(TABLE_CREATE5);
        db.execSQL(TABLE_CREATE6);
        db.execSQL(TABLE_CREATE7);
        db.execSQL(TABLE_CREATE8);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String query2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;
        String query3 = "DROP TABLE IF EXISTS " + TABLE_NAME3;
        String query4 = "DROP TABLE IF EXISTS " + TABLE_NAME4;
        String query5 = "DROP TABLE IF EXISTS " + TABLE_NAME5;
        String query6 = "DROP TABLE IF EXISTS " + TABLE_NAME6;
        String query7 = "DROP TABLE IF EXISTS " + TABLE_NAME7;
        String query8 = "DROP TABLE IF EXISTS " + TABLE_NAME8;

        db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
        db.execSQL(query5);
        db.execSQL(query6);
        db.execSQL(query7);
        db.execSQL(query8);

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

    // insert values into the contact table
 /*   public void insertSaveSearch(Contact c) {
       db = this.getWritableDatabase();
       ContentValues values2 = new ContentValues();

        String query = " SELECT * FROM savesearch";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

       values2.put(COLUMN_NAMESAVE, c.getNameSave());
        values2.put(COLUMN_UNAME, c.getUname());

        db.insert(TABLE_NAME2, null, values2);
        db.close();    }*/

    public boolean createSave(String namesave, String uname, String category, String searchterm){
        db = this.getWritableDatabase();
        String query = "SELECT namesave FROM savesearch WHERE savesearch.uname LIKE '" + uname + "';";
        Cursor cursor = db.rawQuery(query, null);
        System.out.println(cursor.getCount());
        //System.out.println(cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                if(name.equals(namesave)){
                    return false;
                }
            }
            while(cursor.moveToNext());
        }


        try {
            String insterQuery = "INSERT INTO savesearch ('namesave', 'uname', 'category', 'searchterm') " +
                    "VALUES ('" + namesave + "', '" + uname + "', '" + category + "', '" + searchterm + "');";
            db.execSQL(insterQuery);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void insertSite(String url,String image, String price, String name, String site){
        String ID = "SELECT MAX(ID) AS \"HIGHEST\"\n" +
            "FROM savesearch;";
        String ID2;
        try{
            Cursor cursor = db.rawQuery(ID,null);
            cursor.moveToFirst();
            ID2 = cursor.getString(0);
            System.out.println(ID2);

            String insterQuery = "INSERT INTO "+site+" (url, imageurl, price, title, id2)"+
                    "VALUES ('"+url+"', '"+image+"', '" +price+"','" +name+ "','"+ID2+"');";

            db.execSQL(insterQuery);

        }catch (Exception e){
            System.out.println("FUCKING FAILED WHY FIX THIS YOU DUMBASS");
        }



    }
    public String [] getSearchTerm(String uname, String saveName){
        String[] sTerm = new String[2];
        db = this.getReadableDatabase();
        String cat = "SELECT category FROM savesearch WHERE savesearch.uname LIKE '" + uname + "' AND savesearch.namesave LIKE '"+saveName+"';";
        String st = "SELECT searchterm FROM savesearch WHERE savesearch.uname LIKE '" + uname + "' AND savesearch.namesave LIKE '"+saveName+"';";
        try {
            Cursor cursor = db.rawQuery(cat, null);
            cursor.moveToFirst();
            sTerm[1] = cursor.getString(0);
            System.out.println("WEW: " + sTerm[1]);

            cursor = db.rawQuery(st, null);
            cursor.moveToFirst();
            sTerm[0] = cursor.getString(0);
            System.out.println("WEW LADS: " + sTerm[0]);


        }catch (Exception e){
            System.out.println("FUCKING FAILED WHY FIX THIS YOU DUMBASS");
            System.out.println(e);
        }

        return sTerm;
    }
    public ArrayList<listings> getGunbroker(String uname, String saveName, String site){
        ArrayList<listings> ret = new ArrayList<listings>();
        db = this.getReadableDatabase();
        String ID = "SELECT ID FROM savesearch WHERE savesearch.uname LIKE '" + uname + "' AND savesearch.namesave LIKE '"+saveName+"';";
        try{
            Cursor cursor = db.rawQuery(ID,null);
            cursor.moveToFirst();
            String ID2 = cursor.getString(0);
            System.out.println("ID2: " + ID2);

            String query = "SELECT * FROM "+site+" WHERE "+site+".ID2 = " + ID2 +";";
            Cursor cursor2 = db.rawQuery(query, null);
            System.out.println("count:" +cursor2.getCount());
            if (cursor2.moveToFirst()) {
                do {
                    String name = cursor2.getString(cursor2.getColumnIndex("title"));
                    String url = cursor2.getString(cursor2.getColumnIndex("url"));;
                    String image = cursor2.getString(cursor2.getColumnIndex("imageurl"));;
                    String price = cursor2.getString(cursor2.getColumnIndex("price"));;
                    //System.out.println("name: " + name +"\nurl: " + url + "\nimage: " +image +"\nprice: " +price);
                    listings listing = new listings(image,name,price,url);


                    ret.add(listing);
                }
                while(cursor2.moveToNext());
            }


        }catch (Exception e){
            System.out.println("FUCKING FAILED WHY FIX THIS YOU DUMBASS");
        }
        return ret;
    }

    public ArrayList<String> getDatabase(String dataName, String uname){
        ArrayList<String> ret = new ArrayList<String>();
        db = this.getReadableDatabase();
        String query = "SELECT namesave FROM savesearch WHERE savesearch.uname LIKE '" + uname + "';";
        Cursor cursor = db.rawQuery(query, null);
        System.out.println("count:" +cursor.getCount());
        //System.out.println(cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            do {
                System.out.println("name:" + cursor.getString(0));
                ret.add(cursor.getString(0));
            }
            while(cursor.moveToNext());
        }
        return ret;
    }

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
