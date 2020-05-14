package com.tt52.moduleevent.ipc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.tt52.moduleevent.LiveEventBus;
import com.tt52.moduleevent.ipc.IpcConst;
import com.tt52.moduleevent.ipc.decode.IDecoder;
import com.tt52.moduleevent.ipc.decode.ValueDecoder;
import com.tt52.moduleevent.ipc.json.JsonConverter;

/**
 * Created by liaohailiang on 2019/3/26.
 */
public class LebIpcReceiver extends BroadcastReceiver {

    private IDecoder decoder;

    public LebIpcReceiver(JsonConverter jsonConverter) {
        this.decoder = new ValueDecoder(jsonConverter);
    }

    public void setJsonConverter(JsonConverter jsonConverter) {
        this.decoder = new ValueDecoder(jsonConverter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (IpcConst.ACTION.equals(intent.getAction())) {
            try {
                String module = intent.getStringExtra(IpcConst.MODULE_KEY);
                String key = intent.getStringExtra(IpcConst.KEY);
                Object value = decoder.decode(intent);
                if (key != null) {
                    if(TextUtils.isEmpty(module)){
                        LiveEventBus
                                .get(key)
                                .post(value);
                    }else{
                        LiveEventBus
                                .get(module,key)
                                .post(value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
