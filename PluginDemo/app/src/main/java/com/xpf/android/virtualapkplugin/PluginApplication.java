package com.xpf.android.virtualapkplugin;

import android.app.Application;
import android.os.Process;
import android.util.Log;

import com.xpf.android.virtualapkplugin.binderpool.BinderPool;
import com.xpf.android.virtualapkplugin.utils.MyUtils;
import com.xpf.android.virtualapkplugin.utils.RunUtil;

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

        //doWorkInBackground();
    }

    private void doWorkInBackground() {
        // init binder pool
        RunUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                BinderPool.getInstance(getApplicationContext());
            }
        });
    }
}
