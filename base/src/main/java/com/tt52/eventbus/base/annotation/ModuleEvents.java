package com.tt52.eventbus.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * packageName 是包名
 * moduleName 组件名，一般用组件所在包名
 * busName 业务名字用来调用，可以不写，默认用类的名字_Bus当作调用名
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ModuleEvents {
    String packageName();
    String moduleName();
    String busName() default "";
}
