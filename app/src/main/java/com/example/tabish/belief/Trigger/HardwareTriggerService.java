package com.example.tabish.belief.Trigger;

/**
 * Created by Tabish on 12-Mar-17.
 */
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class HardwareTriggerService extends Service {
    private HardwareTriggerReceiver hardwareTriggerReceiver;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(">>>>>>", "HardwareTriggerService CREATED.");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        hardwareTriggerReceiver = new HardwareTriggerReceiver();
        registerReceiver(hardwareTriggerReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(">>>>>>", "HardwareTriggerService DESTROYED.");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        unregisterReceiver(hardwareTriggerReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

