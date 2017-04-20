package com.example.tabish.belief;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tabish.belief.Trigger.HardwareTriggerReceiver;
import com.example.tabish.belief.Trigger.HardwareTriggerService;
import com.example.tabish.belief.cameraback.DemoCamService;
import com.example.tabish.belief.mycontactdb;
import com.example.tabish.belief.cameraback.HiddenCamera.HiddenCameraFragment;
import com.example.tabish.belief.sendmail.GMailSender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final int ACCESS_FINE_LOCATION = 2;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    mycontactdb my=new mycontactdb(this);
    boolean isEmpty=false;
//    String sms= "YOUR POSITION IS HERE";
    private HiddenCameraFragment mHiddenCameraFragment;
    HardwareTriggerReceiver htr;
    boolean emailSent=false,smsSent=false;
    String[] email;

    //handler
    int smstime=05;
    int recordtime=25;
    int phototime=55025;
    int compresstime=58030;
    int emailtime=63000;

    private MediaRecorder mediaRecorder;
    String voiceStoragePath;

    //sms
    String[] phone;
    String message;

    int i=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SMSListener smsListener=new SMSListener();
        startService(new Intent(this, HardwareTriggerService.class));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsListener,intentFilter);
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

    public void clickPhoto(Context context){
        if (mHiddenCameraFragment != null) {    //Remove fragment from container if present
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(mHiddenCameraFragment)
                    .commit();
            mHiddenCameraFragment = null;
        }

        startService(new Intent(context, DemoCamService.class));
    }

    public void getLocation(View view){
        Intent intent = new Intent(MainActivity.this,myLocation.class);
        startActivity(intent);
    }

    //public void recordAudio(){
    //    Intent intent=new Intent(MainActivity.this,RecordAudio.class);
     //   startActivity(intent);
    //}

    public void panic(View view){
        ImageButton button = (ImageButton)findViewById(R.id.PanicButton);
        ImageButton button2 = (ImageButton)findViewById(R.id.SafeButton);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        final AudioManager am = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        button.startAnimation(myAnim);
        Toast.makeText(getApplicationContext(), "Panic Button pressed", Toast.LENGTH_SHORT).show();
        button.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.VISIBLE);
        startInner();
        //htr.startInner();

        //clickPhoto();
        //if(sendMail()) {
//            Log.d("EMAIL","SEND");
//        }
//        else
//        {
//            Log.d("EMAIL","FAILED");
//        }
        //startService(new Intent(this,LocationHandler.class));
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
        final AudioManager am = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        button.startAnimation(myAnim);
        Toast.makeText(getApplicationContext(), "Safe Button pressed", Toast.LENGTH_SHORT).show();
        stop();
        button.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.VISIBLE);
    }


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

    @Override
    public void onBackPressed() {
        if (mHiddenCameraFragment != null) {    //Remove fragment from container if present
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(mHiddenCameraFragment)
                    .commit();
            mHiddenCameraFragment = null;
        } else { //Kill the activity
            super.onBackPressed();
        }
    }

    public void sendMail() {
        //final mycontactdb myemailhelper=new mycontactdb((com.example.tabish.belief.MainActivity.this));
        //emailSent=myemailhelper.dataemail();
        //if(emailSent)
        //{
//            Log.d("Sms","Sms send");
//        }
        //*******
        // Provide Required mail id and password of sender.
        String sender_mail = "beliefsafetyapp@gmail.com";
        String sender_password ="tastastas";
        //*******
        final GMailSender sender = new GMailSender(sender_mail,sender_password);
        String[] toArr = {"tabrezchowkar@gmail.com","tabrezshaikh7311@gmail.com"};
        sender.setTo(toArr);
        sender.setFrom(sender_mail);
        String latitude = "19.782203";
        String longitude = "72.785457";
        sender.setSubject("HELP! HELP! HELP!");
        //sender.setBody("I NEED YOUR HELP CHECK THE ATTACHMENTS\n My Location is : ("+"http://www.google.com/maps/place/" + latitude +","+ longitude+")");

        try {
            if(sender.send()) {
                Log.d("email","email send");
            } else {
                Log.d("email","email failed");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSMS(){
        //final mycontactdb mysmshelper=new mycontactdb(com.example.tabish.belief.MainActivity.this);
        String latitude = "19.782203";
        String longitude = "72.785457";
        message="Help me!!!I am in danger. My Location is : ("+"http://www.google.com/maps/place/" + latitude +","+ longitude+")";
        //smsSent=mysmshelper.datasms(message);
        //if(smsSent)
        //{
         //   Log.d("email","email send");
        //}
        SmsManager sms= SmsManager.getDefault();
        sms.sendTextMessage("+918412066320", null, message, null,null);

    }

    public void startInner()
    {
        final Handler sms = new Handler();
        sms.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendSMS();
                Log.d("SMS sent","<<<<<<<--------------SMS SMS SMS SMS senD-------------------->>>>>>>>>>");
                smstime+=900000;}
        },smstime);  //1 minutes

        final Handler recordAudio =new Handler();
        recordAudio.postDelayed(new Runnable() {
            @Override
            public void run() {
                hasSDCard();
                voiceStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                File audioVoice = new File(voiceStoragePath + File.separator + "Belief");
                if(!audioVoice.exists()){
                    audioVoice.mkdir();
                }
                voiceStoragePath = voiceStoragePath + File.separator + "Belief/" + "Audio"+i + ".3gpp";

                System.out.println("Audio path : " + voiceStoragePath);
                initializeMediaRecord();
                if(mediaRecorder == null){
                    initializeMediaRecord();
                }
                startAudioRecording();
                recordtime+=900000;
                Log.d("AUDIO RECORDING","<<<<<<-------------Audio recorded--------------->>>>");
            }
        },recordtime);      //1 minutes

        final Handler takePhoto =new Handler();
        takePhoto.postDelayed(new Runnable() {
            @Override
            public void run() {
                clickPhoto(MainActivity.this);
                Log.d("PHOTO","<<<<<<--------------photo taken--------------->>>>>>>>>>");
                phototime+=900000;
            }
        },phototime);      //1 minutes

        final Handler compress = new Handler();
        compress.postDelayed(new Runnable() {
            @Override
            public void run() {
                //final String files[]=new String[2];
                //files[0]=Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Belief/Audio"+i+".3gpp";
                //files[1]=Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Belief/Photo"+i+".jpeg";
                //Compress comp = new Compress(files,Environment.getExternalStorageDirectory().getAbsolutePath()+"/Belief/Belief"+i+".zip");
                //comp.zip();
                //System.out.println("--------------------FILES ZIPPED-----------------");
                compresstime+=900000;
            }
        }, compresstime);

        final Handler email = new Handler();
        email.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMail();
                System.out.println("--------------------EMAIL SENT-----------------");
                i++;
                emailtime+=900000;
            }
        }, emailtime);     //6 minutes
    }

    public void stop()
    {
        System.out.print("Trigger service stopped");
    }

    //record audio
    public void startAudioRecording(){
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CountDownTimer countDowntimer = new CountDownTimer(45000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                stopAudioRecording();
            }};countDowntimer.start();

    }

    public void stopAudioRecording(){
        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            Log.d("Recording ","<--------------STOPPED------------->");
        }
    }

    private void hasSDCard(){
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(isSDPresent)        {
            System.out.println("There is SDCard");
        }
        else{
            System.out.println("There is no SDCard");
        }
    }

    private void initializeMediaRecord(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(voiceStoragePath);
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
}

//String s= "My Location is : ("+"http://www.google.com/maps/place/" + latitude +","+ longitude+")";