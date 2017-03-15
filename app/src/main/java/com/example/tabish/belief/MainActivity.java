package com.example.tabish.belief;


import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.tabish.belief.Trigger.HardwareTriggerService;


public class MainActivity extends AppCompatActivity {

    public static final int ACCESS_FINE_LOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, HardwareTriggerService.class));

        }


    //for menu
    private int group1Id=1;
    int settingId=Menu.FIRST;
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(group1Id,settingId,settingId,"Settings");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(MainActivity.this, BassBoost.Settings.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getStarted(View view) {
        Intent myIntent = new Intent(view.getContext(), MainActivity.class);
        startActivity(myIntent);

    }
    public void fakeCall(View view){
        Intent intent = new Intent(MainActivity.this,FakeCall.class);
        startActivity(intent);
    }

    public void clickPhoto(View view){
        //Intent intent = new Intent(MainActivity.this,CaptureCameraImage.class);
        //startActivity(intent);
    }

    public void addData(View view){
        Intent intent = new Intent(MainActivity.this,contacts.class);
        startActivity(intent);
    }

    public void getLocation(View view){
        Intent intent = new Intent(MainActivity.this,myLocation.class);
        startActivity(intent);
    }

    public void siren(View view){

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
    }

    public void recordAudio(View view){
        Intent intent=new Intent(MainActivity.this,RecordAudio.class);
        startActivity(intent);
    }


}
