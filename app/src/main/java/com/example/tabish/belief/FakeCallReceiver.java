package com.example.tabish.belief;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FakeCallReceiver extends BroadcastReceiver{
    private static final int DURATION = 63;
    private static final int HANA_UP_AFTER = 15;
    String duration;
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("FAKENAME");
        String number = intent.getStringExtra("FAKENUMBER");
        String hangUpAfter = Integer.toString(HANA_UP_AFTER);
        duration=Integer.toString(DURATION);

        Intent intentObject = new Intent(context.getApplicationContext(), FakeRingingActivity.class);
        intentObject.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentObject.putExtra("myfakename", name);
        intentObject.putExtra("myfakenumber", number);
        intentObject.putExtra("duration", Integer.parseInt(duration));
        intentObject.putExtra("hangUpAfter", Integer.parseInt(hangUpAfter));
        context.startActivity(intentObject);

    }

}