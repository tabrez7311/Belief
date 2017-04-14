package com.example.tabish.belief;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class mycontactdb extends SQLiteOpenHelper{


    private static final String SQL_DELETE_ENTRIES ="DROP TABLE IF EXISTS " + mycontactdb.TABLE_NAME;
    private static final String DATABASE_NAME = "contact.db";
    private static final String TABLE_NAME = "contact_table";
    private static final String TABLE_NAME2 = "email_table";
    private static final String col_1 = "_ID";
    public static final String cont1="contact_one";
    public static final String email1="email_one";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + mycontactdb.TABLE_NAME + " (" +
                    mycontactdb.col_1 + " INTEGER PRIMARY KEY, " +
                    mycontactdb.cont1 + " VARCHAR(15) not null" +" )";

    private static final String SQL_Email=
            "CREATE TABLE " + mycontactdb.TABLE_NAME2 + " (" +
                    mycontactdb.col_1 + " INTEGER PRIMARY KEY, " +
                    mycontactdb.email1 + " VARCHAR(30) not null" +" )";


    mycontactdb(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_Email);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    boolean insertEmail(String email_one)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(email1,email_one);
        long result = db.insert(TABLE_NAME2,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    boolean insertData(String contact_one){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(cont1,contact_one);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME,null);
        return res;
    }

    public boolean checkEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM "+ TABLE_NAME;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0) {
//leave
            return false;
        } else {
            return true;
//populate table
        }
    }

}

