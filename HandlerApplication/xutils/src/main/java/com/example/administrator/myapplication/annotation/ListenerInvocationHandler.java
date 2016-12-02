package com.example.administrator.myapplication.annotation;

import android.content.Context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/1.
 */
public class ListenerInvocationHandler implements InvocationHandler {
    private Context context;
    private Map<String, Method> methosMap;

    public ListenerInvocationHandler(Context context, Map<String, Method> methosMap) {
        this.context = context;
        this.methosMap = methosMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
        String name = method.getName();
        Method method1 = methosMap.get(name);
        if (method1 == null) {
            return method.invoke(proxy, objects);
        } else {
            return method1.invoke(context, objects);
        }
    }
}
