package com.example.tabish.belief;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void fakeCall(View view){
Intent intent = new Intent(MainActivity.this,FakeCall.class);
        startActivity(intent);
    }
}
