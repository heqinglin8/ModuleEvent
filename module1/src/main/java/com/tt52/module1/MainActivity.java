package com.tt52.module1;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tt52.moduleevent.LiveEventBus;
import com.tt52.module1_export.event.HelloWorldEvent;
import com.tt52.module1_export.event.Module1EventsManager;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_TEST_IN_APP_MSG = "key_test_in_app_msg";

    private Observer<Object> observer = new Observer<Object>() {
        @Override
        public void onChanged(@Nullable Object s) {
            Toast.makeText(MainActivity.this, s.toString()+"", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LiveEventBus.get(KEY_TEST_IN_APP_MSG).observe(this,observer);


        Button send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveEventBus.get(KEY_TEST_IN_APP_MSG).post("app 内的消息");
            }
        });
        Button sendacrossapp = findViewById(R.id.sendacrossapp);
        sendacrossapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Module1EventsManager.EVENT1().postAcrossApp(new HelloWorldEvent("给隔壁app发消息",null));
            }
        });
    }

}
