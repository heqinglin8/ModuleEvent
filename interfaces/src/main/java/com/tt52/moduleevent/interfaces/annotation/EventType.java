package com.tt52.moduleevent.interfaces.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @PackageName com.tt52.eventbus.base.annotation
 * @ClassName EventType
 * @Author heqinglin
 * @Date 2020/5/12 下午7:33
 * @Description TODO
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface EventType {
    Class value() default Object.class;
}
