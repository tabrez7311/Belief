package com.example.tabish.belief;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.CallLog;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Tabish on 15-Apr-17.
 */

public class LocationHandler extends Service {
    private LocationManager locationManager;
    protected LocationListener listener;
    RecordAudio recordAudio;
    mycontactdb my;
    String sms = "Help me! I'm in distress! Details have been sent to your email-id. Check immediately. \n" +
            " Belief App team";
    public String finalString;
    private MediaRecorder mediaRecorder;
    String voiceStoragePath;

    static final String AB = "abcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        hasSDCard();

        voiceStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(voiceStoragePath + File.separator + "voices");
        if(!audioVoice.exists()){
            audioVoice.mkdir();
        }
        voiceStoragePath = voiceStoragePath + File.separator + "voices/" + generateVoiceFilename(6) + ".3gpp";
        System.out.println("Audio path : " + voiceStoragePath);

        initializeMediaRecord();
        if(mediaRecorder == null){
            initializeMediaRecord();
        }
        startAudioRecording();

        final AudioManager am = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        turnOnDataConnection(true, LocationHandler.this);


        final Handler handler4 = new Handler();
        handler4.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                  //  listener = new GPSAsync();
                    // ((GPSAsync) listener).startInnerClass();
                    System.out.println("--------------------CALL+MESSAGE LOG OBTAINED-----------------");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, 25000);

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                final String files[] = new String[2];
                files[0] = Environment.getExternalStorageDirectory().getPath() + "/Belief/AUDIO.mp4";
                files[1] = Environment.getExternalStorageDirectory().getPath() + "/Belief/GPS.txt";
                Compress comp = new Compress(files, Environment.getExternalStorageDirectory().getPath() + "/Belief/Belief.zip");
                comp.zip();

                System.out.println("--------------------FILES ZIPPED AND DATA TURNED ON-----------------");
            }
        }, 45000);

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {

            @Override
            public void run() {
                try {

                    StrictMode.ThreadPolicy policy = new
                            StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String mailers = my.email1.toString();
                    GmailSender sender = new GmailSender("tabrezshaikh7311@gmail.com", "");
                    sender.sendMail("Belief App Team",
                            "This is an automatically generated mail... ",
                            "tabrezshaikh7311@gmail.com",
                            mailers);   //,svarun94@gmail.com,saienthan@gmail.com*/
                    System.out.println("--------------------MAIL COMPILED AND SENT-----------------");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        }, 50000);

        final Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {

            @Override
            public void run() {
                try {
                    sendSMS();
                } catch (Exception e) {
                    Log.e("Sms", e.getMessage(), e);
                }
            }
        }, 100000);
        return super.onStartCommand(intent, flags, startId);
    }

    public String generateVoiceFilename( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

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
        }

    }

    public void hasSDCard(){
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(isSDPresent)        {
            System.out.println("There is SDCard");
        }
        else{
            System.out.println("There is no SDCard");
        }
    }

    public void initializeMediaRecord(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(voiceStoragePath);
    }

    boolean turnOnDataConnection(boolean ON, Context context) {

        try {
            final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class<?> conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, ON);
            // }
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    public void sendSMS() {
        String phoneNo = my.cont1.toString();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, sms + finalString, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS failed, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}

