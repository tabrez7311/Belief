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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class contacts extends Activity {

    private static final int RESULT_PICK_CONTACT = 85500;
    public TextView textView1;
    EditText editText1;
    EditText editText2;
    Button addEmail,addNumber;
    mycontactdb mydb;
    private LinearLayout mLayout;
    public int n=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
        mLayout=(LinearLayout)findViewById(R.id.phoneLayout);
        mydb = new mycontactdb(this);

        addEmail = (Button) findViewById(R.id.addEmail);
        addNumber=(Button) findViewById(R.id.addNumber);
        editText1 = (EditText) findViewById(R.id.ph_no1);
        editText2 = (EditText) findViewById(R.id.email1);
        AddContact();
        addEmail();
    }


    public void AddContact() {
        addNumber.setOnClickListener(
                    new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = mydb.insertData(editText1.getText().toString());
                        if (isInserted) {
                            Toast.makeText(contacts.this, "Phone number added", Toast.LENGTH_LONG).show();
                           // mLayout.addView(createNewTextView(editText1.getText().toString()));

                        } else {
                            Toast.makeText(contacts.this, "Error occured", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void addEmail(){
        addEmail.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = mydb.insertEmail(editText2.getText().toString());
                        if (isInserted) {
                            Toast.makeText(contacts.this, "Email Added", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(contacts.this, "Error occured", Toast.LENGTH_LONG).show();

                        }
                    }
                }
        );

    }

  /*  private TextView createNewTextView(String text) {

        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        lparams.topMargin=3;
        textView.setLayoutParams(lparams);
        textView.setText(text);
        textView.setId(n);
        addButton.setText("-");
        addButton.setId(n);
        n++;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(15);
        params.topMargin=3;
        addButton.setOnClickListener(listener);
        mLayout.addView(addButton,params);
        return textView;
    }

    public View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            
        }
    };
   /* private Button removeButton()
    {
        Button addButton =new Button(this);
        addButton.setText("-");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW,R.id.addNumber);
        params.addRule(RelativeLayout.ALIGN_START,R.id.addNumber);
        mLayout.addView(addButton,params);
        return addButton;
    }*/




/*
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
                            buffer.append("Name : " + res.getString(1) + "\n");
                            buffer.append("Mob. : " + res.getString(2) + "\n");
                            buffer.append("Email : " + res.getString(3) + "\n");
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
    /*
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
    */
}


