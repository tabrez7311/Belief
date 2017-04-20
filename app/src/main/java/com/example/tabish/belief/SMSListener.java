package com.example.tabish.belief;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;

import java.util.Objects;

/**
 * Created by Tabish on 20-Apr-17.
 */

public class SMSListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            String msgBody=null;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        msgBody = msgs[i].getMessageBody();
                    }
                }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                }
                String latitude = "19.782203";
                String longitude = "72.785457";
                String template="Help me!!!I am in danger. My Location is : ("+"http://www.google.com/maps/place/" + latitude +","+ longitude+")";
                if(Objects.equals(template,msgBody))
                {
                    final MediaPlayer mp = MediaPlayer.create(context, R.raw.sirensound);
                    mp.start();
                    CountDownTimer countDowntimer = new CountDownTimer(20000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }
                        public void onFinish() {
                            mp.stop();
                        }};countDowntimer.start();
                }
            }
        }
    }
}
