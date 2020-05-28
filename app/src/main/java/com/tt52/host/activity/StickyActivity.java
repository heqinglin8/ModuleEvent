package com.tt52.host.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.tt52.host.LiveEventBusDemo;
import com.tt52.host.R;
import com.tt52.host.databinding.ActivityStickyDemoBinding;
import com.tt52.moduleevent.LiveEventBus;


public class StickyActivity extends AppCompatActivity {

    private ActivityStickyDemoBinding binding;
    private Observer<String> observer = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            binding.tvSticky2.setText("observeStickyForever注册的观察者收到消息: " + s);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sticky_demo);
        binding.setLifecycleOwner(this);
        LiveEventBus
                .get(LiveEventBusDemo.KEY_TEST_STICKY, String.class)
                .observeSticky(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        binding.tvSticky1.setText("observeSticky注册的观察者收到消息: " + s);
                    }
                });
        LiveEventBus
                .get(LiveEventBusDemo.KEY_TEST_STICKY, String.class)
                .observeStickyForever(observer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveEventBus
                .get(LiveEventBusDemo.KEY_TEST_STICKY, String.class)
                .removeObserver(observer);
    }
}
