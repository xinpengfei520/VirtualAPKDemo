package com.xpf.android.virtualapkplugin.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.xpf.android.virtualapkplugin.utils.MyConstants;


public class MessengerService extends Service {

    private static final String TAG = "MessengerService";

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MyConstants.MSG_FROM_CLIENT) {
                Log.i(TAG, "receive msg from Client:" + msg.getData().getString("msg"));
                Messenger client = msg.replyTo;
                Message replyMessage = Message.obtain(null, MyConstants.MSG_FROM_SERVICE);
                Bundle bundle = new Bundle();
                bundle.putString("reply", "嗯，你的消息我已经收到，稍后会回复你。");
                replyMessage.setData(bundle);
                try {
                    client.send(replyMessage);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

}
