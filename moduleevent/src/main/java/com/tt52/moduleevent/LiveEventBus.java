package com.tt52.moduleevent;

import com.tt52.moduleevent.core.Config;
import com.tt52.moduleevent.core.LiveEventBusCore;
import com.tt52.moduleevent.core.Observable;

/**
 * _     _           _____                _  ______
 * | |   (_)         |  ___|              | | | ___ \
 * | |    ___   _____| |____   _____ _ __ | |_| |_/ /_   _ ___
 * | |   | \ \ / / _ \  __\ \ / / _ \ '_ \| __| ___ \ | | / __|
 * | |___| |\ V /  __/ |___\ V /  __/ | | | |_| |_/ / |_| \__ \
 * \_____/_| \_/ \___\____/ \_/ \___|_| |_|\__\____/ \__,_|___/
 * <p>
 * <p>
 * <p>
 * Created by liaohailiang on 2019/1/21.
 */

public final class LiveEventBus {

    /**
     * get observable by key with type
     *
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public static <T> Observable<T> get(String key, Class<T> type) {
        return LiveEventBusCore.get().with(key, type);
    }

    /**
     * get observable by moduleName、key with type
     * @param moduleName
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public static <T> Observable<T> get(String moduleName, String key, Class<T> type) {
        return LiveEventBusCore.get().with(moduleName, key, type);
    }

    /**
     * get observable by key
     *
     * @param key
     * @return
     */
    public static Observable<Object> get(String key) {
        return get(key, Object.class);
    }

    /**
     * get observable by moduleName,key
     *
     * @param key
     * @return
     */
    public static Observable<Object> get(String moduleName, String key) {
        return get(moduleName, key, Object.class);
    }

    /**
     * use the inner class Config to set params
     * first of all, call config to get the Config instance
     * then, call the method of Config to config LiveEventBus
     * call this method in Application.onCreate
     */
    public static Config config() {
        return LiveEventBusCore.get().config();
    }
}
