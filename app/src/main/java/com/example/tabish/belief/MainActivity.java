package com.example.tabish.belief;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tabish.belief.Trigger.HardwareTriggerService;


public class MainActivity extends AppCompatActivity {

    public static
    final int ACCESS_FINE_LOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, HardwareTriggerService.class));
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

    public void recordAudio(View view){
        Intent intent=new Intent(MainActivity.this,RecordAudio.class);
        startActivity(intent);
    }



    public void panic(View view){
        ImageButton button = (ImageButton)findViewById(R.id.PanicButton);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);
        Toast.makeText(getApplicationContext(), "Panic Button pressed", Toast.LENGTH_SHORT).show();
    }

}
