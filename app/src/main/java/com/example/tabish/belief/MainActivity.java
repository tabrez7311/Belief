package com.example.tabish.belief;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tabish.belief.Trigger.HardwareTriggerService;
import com.example.tabish.belief.mycontactdb;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final int ACCESS_FINE_LOCATION = 2;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    mycontactdb my=new mycontactdb(this);
    boolean isEmpty=false;
//    String sms= "YOUR POSITION IS HERE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, HardwareTriggerService.class));
        dataCheck();
        checkAndRequestPermissions();
    }

    //Permission method
    private  boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int sms = ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS);
        int callog = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALL_LOG);
        int wake_lock = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WAKE_LOCK);
        int vibrate = ContextCompat.checkSelfPermission(this, android.Manifest.permission.VIBRATE);
        int rellog = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_LOGS);
        int rec_aud = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (sms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
        }
        if (callog != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_CALL_LOG);
        }
        if (rellog != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_LOGS);
        }
        if (rec_aud != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECORD_AUDIO);
        }
        if (wake_lock != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WAKE_LOCK);
        }

        if (vibrate != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.VIBRATE);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);

        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_contacts was selected
            case R.id.action_contacts:
                Intent intent = new Intent(MainActivity.this,contacts.class);
                startActivity(intent);
                break;
            // action with ID action_help was selected
            case R.id.action_help:
                Intent intent1 = new Intent(MainActivity.this,HelpMain.class);
                startActivity(intent1);
                break;

            case R.id.action_fakecall:
                Intent intent2=new Intent(MainActivity.this,FakeCall.class);
                startActivity(intent2);
                break;

            case R.id.action_aboutus:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.about);
                alertDialogBuilder.setMessage(R.string.about_message);
                alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                alertDialogBuilder.show();
                return true;

            default:
                break;
        }

        return true;
    }

    public void getStarted(View view) {
        Intent myIntent = new Intent(view.getContext(), MainActivity.class);
        startActivity(myIntent);

    }

    public void clickPhoto(View view){
        //Intent intent = new Intent(MainActivity.this,CaptureCameraImage.class);
        //startActivity(intent);
    }

    public void getLocation(View view){
        Intent intent = new Intent(MainActivity.this,myLocation.class);
        startActivity(intent);
    }

   /* public void siren(View view){

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sirensound);
        mp.start();

        findViewById(R.id.SirenButton).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if(!mp.isPlaying())
                {
                    mp.start();
                }
                else{
                    mp.seekTo(0);
                    mp.pause();

                }
            }
        });
    }*/

    public void recordAudio(){
        Intent intent=new Intent(MainActivity.this,RecordAudio.class);
        startActivity(intent);
    }



    public void panic(View view){
        ImageButton button = (ImageButton)findViewById(R.id.PanicButton);
        ImageButton button2 = (ImageButton)findViewById(R.id.SafeButton);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);
        Toast.makeText(getApplicationContext(), "Panic Button pressed", Toast.LENGTH_SHORT).show();
        startService(new Intent(this,LocationHandler.class));
        button.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.VISIBLE);
        //sendSMS();
        //sendEmail();
    }

    public void safe(View view){
        ImageButton button = (ImageButton)findViewById(R.id.SafeButton);
        ImageButton button2 = (ImageButton)findViewById(R.id.PanicButton);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);
        Toast.makeText(getApplicationContext(), "Safe Button pressed", Toast.LENGTH_SHORT).show();
        stopService(new Intent(this,LocationHandler.class));
        button.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.VISIBLE);
    }

    /*
    public void sendEmail(){
        String email=my.email1.toString();
    }
    public void sendSMS() {
        String phoneNo = my.cont1.toString();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, sms, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    };*/

    public void dataCheck()
    {
        isEmpty= my.checkEmpty();
        if(isEmpty)
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("No Contacts")
                    .setMessage("You have not added a single contact.Do you want to add contacts?")
                    .setPositiveButton("Add now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // add
                            Intent intent = new Intent(MainActivity.this,contacts.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

}
