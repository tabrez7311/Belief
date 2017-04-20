package com.example.tabish.belief.Trigger;

/**
 * Created by Tabish on 12-Mar-17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.tabish.belief.MainActivity;
import com.example.tabish.belief.R;
import com.example.tabish.belief.RecordAudio;
import com.example.tabish.belief.cameraback.DemoCamService;
import com.example.tabish.belief.cameraback.HiddenCamera.HiddenCameraFragment;
import com.example.tabish.belief.cameraback.HiddenCamera.HiddenCameraService;
import com.example.tabish.belief.mycontactdb;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;

import com.example.tabish.belief.Compress;

public class HardwareTriggerReceiver extends BroadcastReceiver {
    public static final int ALERT_CONFIRMATION_VIBRATION_DURATION = 500;

    private static final String TAG = HardwareTriggerReceiver.class.getName();
    //    private MultiClickEvent multiClickEvent;
    protected MultiClickEvent multiClickEvent;

    //send sms
    String[] phone;
    String message;

    //record audio
    private MediaRecorder mediaRecorder;
    String voiceStoragePath;
    static final String AB = "abcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();
    int goIN=1;
    int i=1;

    //handler
    int smstime=05;
    int recordtime=25;
    int phototime=55025;
    int compresstime=56030;
    int emailtime=60000;

    //take photo
    private HiddenCameraFragment mHiddenCameraFragment;
    protected MainActivity main=new MainActivity();

    //MainActvity Panic button


    public HardwareTriggerReceiver() {
        resetEvent();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(">>>>>>>", "in onReceive of HWReceiver");
        String action = intent.getAction();
        if (!isCallActive(context) && (action.equals(ACTION_SCREEN_OFF) || action.equals(ACTION_SCREEN_ON))) {
            multiClickEvent.registerClick(System.currentTimeMillis());
            if(multiClickEvent.skipCurrentClick()){
                Log.e("*****", "skipped click");
                multiClickEvent.resetSkipCurrentClickFlag();
            }

            else if(multiClickEvent.canStartVibration()){
                Log.e("*****", "vibration started");
                if(goIN==1) {
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(ALERT_CONFIRMATION_VIBRATION_DURATION);
                    startInner(context);
                    goIN=0;
                    Log.d("HARDWARE TRIGGERED","<<<<<<<-----------------ACTIVATED---------------->>>>>>>>>>");
                }
                else {
                    stop();
                    goIN=1;
                    Log.d("HARDWARE TRIGGERED","<<<<<<<-----------------DEACTIVATED---------------->>>>>>>>>>");
                }
                }
                //PanicAlert panicAlert = getPanicAlert(context);
                //panicAlert.vibrate();
            }

            else if (multiClickEvent.isActivated()) {
                Log.e("*****", "alerts activated");
                onActivation(context);
                resetEvent();
            }
        }


    public void startInner(final Context context)
    {
        final Handler sms = new Handler();
        sms.postDelayed(new Runnable() {
            @Override
            public void run() {
                //main.sendSMS();
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

                //main.clickPhoto(context);
                //Log.d("PHOTO","<<<<<<--------------photo taken--------------->>>>>>>>>>");
                phototime+=900000;
            }
        },phototime);      //1 minutes

        final Handler compress = new Handler();
        compress.postDelayed(new Runnable() {
            @Override
            public void run() {
                //final String files[]=new String[2];
                //files[0]=Environment.getExternalStorageDirectory().getAbsolutePath()+ "/BELIEF/Audio"+i+".3gpp";
                //files[1]=Environment.getExternalStorageDirectory().getAbsolutePath()+ "/BELIEF/Photo"+i+".jpeg";
                //Compress comp = new Compress(files,Environment.getExternalStorageDirectory().getAbsolutePath()+"/BELIEF/BELIEF"+i+".zip");
                //comp.zip();
                //System.out.println("--------------------FILES ZIPPED-----------------");
                //compresstime+=900000;
            }
        }, compresstime);

        final Handler email = new Handler();
        email.postDelayed(new Runnable() {
            @Override
            public void run() {
                main.sendMail();
                System.out.println("--------------------EMAIL SENT-----------------");
                emailtime+=900000;
            }
        }, emailtime);     //6 minutes
    }

    public void stop()
    {
        System.out.print("Trigger service stopped");
    }

    protected void onActivation(Context context) {
        Log.e(">>>>>>>", "in onActivation of HWReceiver");
        //activateAlert(getPanicAlert(context));
    }

  /*  void activateAlert(PanicAlert panicAlert) {
//        panicAlert.start();
        panicAlert.activate();
    }
*/
    protected void resetEvent() {
        multiClickEvent = new MultiClickEvent();
    }

    /*protected PanicAlert getPanicAlert(Context context) {
        return new PanicAlert(context);
    }
*/
    private boolean isCallActive(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return manager.getMode() == AudioManager.MODE_IN_CALL;
    }
    //record audio
    private String generateVoiceFilename( int len ){
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


}

