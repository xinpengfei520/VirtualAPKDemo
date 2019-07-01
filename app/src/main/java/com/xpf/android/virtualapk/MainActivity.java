package com.xpf.android.virtualapk;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.didi.virtualapk.PluginManager;
import com.didi.virtualapk.internal.LoadedPlugin;
import com.didi.virtualapk.internal.PluginContentResolver;
import com.xpf.android.virtualapk.download.DownloadService;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String PLUGIN_PACKAGE_NAME = "com.xpf.android.virtualapkplugin";
    private static final int PERMISSION_REQUEST_CODE_STORAGE = 20190520;
    private static final String DOWNLOAD_PLUGIN_URL = "https://raw.githubusercontent.com/xinpengfei520/VirtualAPKDemo/master/plugin/plugin.apk";
    private DownloadService.DownloadBinder downloadBinder;
    private Button btnJump;
    private Button btnDownload;
    private Button btnTestCP;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected():");
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected():");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnJump = findViewById(R.id.btnJump);
        btnDownload = findViewById(R.id.btnDownload);
        btnTestCP = findViewById(R.id.btnTestCP);

        initListener();

        Intent intent = new Intent(this, DownloadService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);

        if (hasPermission()) {
            loadPlugin();
        } else {
            requestPermission();
        }
    }

    private void initListener() {
        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToPluginActivity();
            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadBinder != null) {
                    downloadBinder.startDownload(DOWNLOAD_PLUGIN_URL);
                }
            }
        });
        btnTestCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testContentProvider();
            }
        });
    }

    private void testContentProvider() {
        Uri bookUri = Uri.parse("content://" + PLUGIN_PACKAGE_NAME + ".book.provider/book");
        LoadedPlugin plugin = PluginManager.getInstance(this).getLoadedPlugin(PLUGIN_PACKAGE_NAME);
        bookUri = PluginContentResolver.wrapperUri(plugin, bookUri);

        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
        if (bookCursor != null) {
            while (bookCursor.moveToNext()) {
                int bookId = bookCursor.getInt(0);
                String bookName = bookCursor.getString(1);
                Log.d(TAG, "query book:" + bookId + ", " + bookName);
            }
            bookCursor.close();
        }
    }

    private void loadPlugin() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "Sdcard was NOT MOUNTED!", Toast.LENGTH_SHORT).show();
        }

        PluginManager pluginManager = PluginManager.getInstance(this);
        String pluginPath = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/plugin.apk");
        File plugin = new File(pluginPath);

        if (plugin.exists()) {
            try {
                pluginManager.loadPlugin(plugin);
                Log.d(TAG, "Plugin loaded success~");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "onError:" + e.getMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PERMISSION_REQUEST_CODE_STORAGE == requestCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {
                loadPlugin();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private boolean hasPermission() {
        Log.d(TAG, "hasPermission");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestPermission() {
        Log.d(TAG, "requestPermission");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE_STORAGE);
        }
    }

    public void jumpToPluginActivity() {
        if (PluginManager.getInstance(this).getLoadedPlugin(PLUGIN_PACKAGE_NAME) == null) {
            Toast.makeText(this, "Plugin not loaded!", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent();
            intent.setClassName(PLUGIN_PACKAGE_NAME, PLUGIN_PACKAGE_NAME + ".PluginActivity");
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
