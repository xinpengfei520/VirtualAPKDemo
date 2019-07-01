package com.xpf.android.virtualapkplugin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xpf.android.virtualapkplugin.R;
import com.xpf.android.virtualapkplugin.SecondActivity;

public class PluginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        findViewById(R.id.btnClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PluginActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }
}
