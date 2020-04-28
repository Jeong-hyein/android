package com.example.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "smsReciver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onreceive 실행됨");
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = parseSmsMessage(bundle);
        if (messages.length > 0) {
            String sender = messages[0].getDisplayOriginatingAddress();
            Log.d(TAG, "sender : " + sender);
            String contents = messages[0].getMessageBody().toString();
            Log.d(TAG, "contents : " + contents);
            Date receivedDate = new Date(messages[0].getTimestampMillis());
            Log.d(TAG, "receivedDate : " + receivedDate);

            sendToActivity(context, sender, contents, receivedDate);
        }

    }
    private SmsMessage[] parseSmsMessage(Bundle bundle){
        Object[] obj =( Object[] ) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[obj.length];
        for(int i=0; i<obj.length; i++){
            String format = bundle.getString("format");
            messages[i] = SmsMessage.createFromPdu((byte[]) obj[i], format);
        }
        return messages;
    }
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm");
    private void sendToActivity(Context context, String sender, String contents, Date receivedDate){
        Intent intent = new Intent(context, SmsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                |Intent.FLAG_ACTIVITY_SINGLE_TOP
                |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("sender", sender);
        intent.putExtra("contents", contents);
        intent.putExtra("receivedDate", format.format(receivedDate));
        context.startActivity(intent);
    }

}
