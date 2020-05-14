package com.tt52.moduleevent.ipc.encode;

import android.content.Intent;

/**
 * Created by liaohailiang on 2019/3/25.
 */
public interface IEncoder {

    void encode(Intent intent, Object value) throws EncodeException;
}
