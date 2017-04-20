package com.example.tabish.belief;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.tabish.belief.sendmail.GMailSender;

public class mycontactdb extends SQLiteOpenHelper{


    private static final String SQL_DELETE_ENTRIES ="DROP TABLE IF EXISTS " + mycontactdb.TABLE_NAME;
    private static final String DATABASE_NAME = "contact.db";
    private static final String TABLE_NAME = "contact_table";
    private static final String TABLE_NAME2 = "email_table";
    private static final String col_1 = "_ID";
    public static final String cont1="contact_one";
    public static final String email1="email_one";
    //sms
    String[] phone;
    String message;

    //email
    String[] email;
    Boolean ismailsent=false;
    Boolean issmssent=false;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + mycontactdb.TABLE_NAME + " (" +
                    mycontactdb.col_1 + " INTEGER PRIMARY KEY autoincrement, " +
                    mycontactdb.cont1 + " TEXT unique not null" +" )";

    private static final String SQL_Email=
            "CREATE TABLE " + mycontactdb.TABLE_NAME2 + " (" +
                    mycontactdb.col_1 + " INTEGER PRIMARY KEY autoincrement, " +
                    mycontactdb.email1 + " TEXT unique not null" +" )";


    public mycontactdb(Context context) {
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
        Cursor datacursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME,null);
        return datacursor;
    }

    public Cursor getAllEmail(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT  * FROM " + TABLE_NAME2,null);
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

    /*public boolean datasms(String message)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        issmssent=false;
        String data="SELECT * FROM "+ TABLE_NAME;
        Cursor cursor=db.rawQuery(data,null);
        for (int i = 0; i < cursor.getCount(); i++) {
            while (cursor.moveToNext()) {
                phone[i] = cursor.getString(1);
            }
            SmsManager sms= SmsManager.getDefault();
            sms.sendTextMessage(phone[i], null, message, null,null);
            issmssent=true;
        }
        cursor.close();
        return issmssent;
    }*/

    public boolean deleteNumber(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        String where = col_1 + " = " + id;
        result = db.delete(TABLE_NAME,where,null);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean deleteEmail(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        String where = col_1 + " = " + id;
        result = db.delete(TABLE_NAME2,where,null);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

/*
    public boolean dataemail()
    {
        ismailsent=false;
        Cursor cursor=getAllEmail();
        if(cursor.getCount()==0)
        {
            ismailsent=false;
            Log.d("Data","not found");
            //Toast.makeText(getApplicationContext(),"Data not found",Toast.LENGTH_SHORT).show();

        }
        for(int i=0;i<cursor.getCount();i++)
            while(cursor.moveToNext())
            {
                Log.d("email","found");
                email[i]=cursor.getString(1);

            }

        //*******
        // Provide Required mail id and password of sender.
        String sender_mail = "beliefsafetyapp@gmail.com";
        String sender_password ="tastastas";
        //*******
        final GMailSender sender = new GMailSender(sender_mail,sender_password);
        //String[] toArr = {"tabrezchowkar@gmail.com"};
        sender.setTo(email);
        sender.setFrom(sender_mail);
        sender.setSubject("HELP! HELP! HELP!");
        sender.setBody("I NEED YOUR HELP CHECK THE ATTACHMENTS");

        try {
            if(sender.send()) {
               Log.d("email","email send");
                ismailsent=true;
            } else {
                ismailsent=false;
                Log.d("email","email failed");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    return ismailsent;
    }
    */
}

