package com.tt52.host.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tt52.host.LiveEventBusDemo;
import com.tt52.host.R;
import com.tt52.host.databinding.ActivityObserverActiveLevelDemoBinding;
import com.tt52.moduleevent.LiveEventBus;


public class ObserverActiveLevelActivity extends AppCompatActivity {

    private ActivityObserverActiveLevelDemoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_observer_active_level_demo);
        binding.setLifecycleOwner(this);
        binding.setHandler(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void sendMsgToPrevent() {
        LiveEventBus
                .get(LiveEventBusDemo.KEY_TEST_ACTIVE_LEVEL)
                .post("Send Msg To Observer Stopped");
    }
}
