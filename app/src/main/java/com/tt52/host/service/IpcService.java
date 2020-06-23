package com.tt52.host.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.tt52.module1_export.Module1EventsManager;
import com.tt52.module1_export.event.HelloWorldEvent;
import com.tt52.moduleevent.LiveEventBus;

/**
 * Created by liaohailiang on 2019/3/26.
 */
public class IpcService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        LiveEventBus
                .get("key_test_broadcast", String.class)
                .observeForever(observer);

        Module1EventsManager.EVENT1().observeForever(observer2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LiveEventBus
                .get("key_test_broadcast", String.class)
                .removeObserver(observer);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Observer<String> observer = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            Toast.makeText(IpcService.this, s, Toast.LENGTH_SHORT).show();
        }
    };

    private Observer<HelloWorldEvent> observer2 = new Observer<HelloWorldEvent>() {
        @Override
        public void onChanged(@Nullable HelloWorldEvent helloWorldEvent) {
            Toast.makeText(IpcService.this, "跨进程接收信息："+helloWorldEvent.name, Toast.LENGTH_SHORT).show();
        }
    };
}
