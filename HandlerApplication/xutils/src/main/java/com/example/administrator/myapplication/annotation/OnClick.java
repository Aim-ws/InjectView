package com.example.administrator.myapplication.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2016/12/1.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBus(listenerSetter = "setOnClickListener",
        listenerType = View.OnClickListener.class,
        callBackMethod = "onClick")
public @interface OnClick {
    int[] value();
}
