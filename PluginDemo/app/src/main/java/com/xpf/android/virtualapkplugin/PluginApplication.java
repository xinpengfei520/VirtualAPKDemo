package com.xpf.android.virtualapkplugin;

import android.app.Application;
import android.os.Process;
import android.util.Log;

import com.xpf.android.virtualapkplugin.utils.MyUtils;

/**
 * Created by x-sir on 2019-06-29 :)
 * Function:
 */
public class PluginApplication extends Application {

    private static final String TAG = "PluginApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = MyUtils.getProcessName(getApplicationContext(), Process.myPid());
        Log.d(TAG, "PluginApplication start, process name:" + processName);
    }
}
