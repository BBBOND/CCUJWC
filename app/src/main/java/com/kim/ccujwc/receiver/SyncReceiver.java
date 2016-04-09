package com.kim.ccujwc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kim.ccujwc.service.SyncService;
import com.kim.ccujwc.view.utils.MySharedPreferences;

import java.util.Map;

public class SyncReceiver extends BroadcastReceiver {

    private static final String TAG = "SyncReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Sync start!");
//        MySharedPreferences msp = MySharedPreferences.getInstance(context);
//        Map<String, Object> map = msp.readLoginInfo();
//        if ((boolean) map.get("isAutoLogin"))
        context.startService(new Intent(context, SyncService.class));
        Log.d(TAG, "Sync started!");
    }
}
