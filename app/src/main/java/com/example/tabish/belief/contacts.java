package com.example.tabish.belief;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class contacts extends Activity {

    private static final int RESULT_PICK_CONTACT = 85500;
    public TextView textView1;
    EditText editText1;
    Button save_contact,viewAll;
    mycontactdb mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
        mydb = new mycontactdb(this);
        save_contact = (Button) findViewById(R.id.save_c);
        viewAll = (Button) findViewById(R.id.viewA);
        textView1 = (TextView) findViewById(R.id.name1);
        editText1 = (EditText) findViewById(R.id.ph_no1);
        AddData();
        viewAll();
    }

    public void AddData() {
        save_contact.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = mydb.insertData(editText1.getText().toString());
                        if (isInserted) {
                            Toast.makeText(contacts.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(contacts.this, "Error occured", Toast.LENGTH_LONG).show();

                        }
                    }
                }
        );
    }


    public void viewAll() {
        viewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = mydb.getAllData();
                        if (res.getCount() == 0) {
                            showMessage("Error", "Nothing found");
                            return;
                        }
                        StringBuilder buffer = new StringBuilder();
                        while (res.moveToNext()) {
                            buffer.append("ID    :" + res.getString(0) + "\n");
                            buffer.append("First : " + res.getString(1) + "\n");
                        }
                        showMessage("Data", buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    public void pickContact(View v)
    {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     * @param data

     */

    private void contactPicked(Intent data) {
        Cursor cursor;
        try {
            String phoneNo;
            String name ;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);

            textView1.setText(name);
            editText1.setText(phoneNo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


