package pl.tajchert.wearbank.codes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSReceiver extends BroadcastReceiver {
    private SharedPreferences prefs;
    private String number;
    private String before;
    private String after;


    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        prefs = context.getSharedPreferences("pl.tajchert.wearbank.codes", Context.MODE_PRIVATE);
        number = prefs.getString(MyActivity.KEY_NUMBER, "");
        before = prefs.getString(MyActivity.KEY_BEFORE, "");
        after = prefs.getString(MyActivity.KEY_AFTER, "");
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    if(!phoneNumber.equals(number)){
                        return;
                    }
                    String message = currentMessage.getDisplayMessageBody();
                    if(!before.equals("") && !after.equals("")){
                        Pattern p = Pattern.compile(before + "(.*?)" + after);
                        Matcher m = p.matcher(message);
                        if (m.find()) {
                            makeNotification(context, number, m.group(1));
                        }
                    } else if( before.equals("") && !after.equals("")){
                        makeNotification(context, number, message.substring(0, message.indexOf(after)));
                    } else if( !before.equals("") && after.equals("")){
                        makeNotification(context, number, message.substring((message.indexOf(before)+before.length()), message.length()));
                    }
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

    private void makeNotification(Context context, String number, String code){
        Intent mIntent = new Intent(context, UpdateService.class);
        mIntent.putExtra(Tools.SERVICE_KEY_TEXT, code);
        context.startService(mIntent);
    }
}
