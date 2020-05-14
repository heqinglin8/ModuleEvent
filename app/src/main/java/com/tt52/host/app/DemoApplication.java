package com.tt52.host.app;

import android.app.Application;

import com.tt52.moduleevent.LiveEventBus;

/**
 * Created by liaohailiang on 2019/3/26.
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LiveEventBus
                .config()
                .lifecycleObserverAlwaysActive(true);
    }
}
