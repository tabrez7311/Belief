package com.example.tabish.belief;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.io.IOException;
import java.util.Random;


public class RecordAudio extends AppCompatActivity {

    private Button recordingButton;

    private MediaRecorder mediaRecorder;
    String voiceStoragePath;

    static final String AB = "abcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hasSDCard();

        voiceStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(voiceStoragePath + File.separator + "voices");
        if(!audioVoice.exists()){
            audioVoice.mkdir();
        }
        voiceStoragePath = voiceStoragePath + File.separator + "voices/" + generateVoiceFilename(6) + ".3gpp";
        System.out.println("Audio path : " + voiceStoragePath);

        recordingButton = (Button)findViewById(R.id.recordingButton);

        initializeMediaRecord();
        if(mediaRecorder == null){
            initializeMediaRecord();
        }
        startAudioRecording();

        /*recordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaRecorder == null){
                    initializeMediaRecord();
                }
                startAudioRecording();
            }
        });
        */
    }

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
        recordingButton.setEnabled(false);
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
        recordingButton.setEnabled(true);
        Intent intent=new Intent(RecordAudio.this,MainActivity.class);
        startActivity(intent);
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

     /*stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudioRecording();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playLastStoredAudioMusic();
                mediaPlayerPlaying();
            }
        });
    }

    private void playLastStoredAudioMusic(){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(voiceStoragePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        recordingButton.setEnabled(true);
        playButton.setEnabled(false);
    }

    private void stopAudioPlay(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

      private void mediaPlayerPlaying(){
        if(!mediaPlayer.isPlaying()){
            stopAudioPlay();
        }
    }
    */

}