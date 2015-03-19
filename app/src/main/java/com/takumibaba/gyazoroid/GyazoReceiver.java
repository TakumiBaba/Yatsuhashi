package com.takumibaba.gyazoroid;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by takumi on 2015/03/16.
 */
public class GyazoReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, GyazoService.class);
        i.putExtra("data", intent.getData());
        context.startService(i);
    }
}
