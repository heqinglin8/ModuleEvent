package com.tt52.module1_export.event;


import com.tt52.moduleevent.interfaces.annotation.EventType;
import com.tt52.moduleevent.interfaces.annotation.ModuleEvents;

/**
 * Created by liaohailiang on 2019-08-30.
 */
@ModuleEvents(packageName = "com.tt52.module1_export",moduleName = "com.tt52.module1", busName = "Module1EventsManager")
public class Module1Events {

    //不指定消息类型，那么消息的类型默认为Object
    @EventType(HelloWorldEvent.class)
    public static final String EVENT1 = "event1";

    //指定消息类型为自定义Bean
    @EventType(TestEventBean.class)
    public static final String EVENT2 = "event2";

    //指定消息类型为java原生类型
    @EventType(String.class)
    public static final String EVENT3 = "event3";

    //不指定消息类型，那么消息的类型默认为Object
    public static final String EVENT4 = "event4";

}
