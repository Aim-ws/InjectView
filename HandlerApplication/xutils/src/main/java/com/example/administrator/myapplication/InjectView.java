package com.example.administrator.myapplication;

import android.app.Activity;
import android.view.View;

import com.example.administrator.myapplication.annotation.ContentView;
import com.example.administrator.myapplication.annotation.EventBus;
import com.example.administrator.myapplication.annotation.ListenerInvocationHandler;
import com.example.administrator.myapplication.annotation.ViewInject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/1.
 */
public class InjectView {
    private static final String TAG = InjectView.class.getSimpleName();

    public static void inject(Activity context) {
        injectLayout(context);
        injectView(context);
        injectClick(context);
    }

    private static void injectClick(Activity context) {
        Class<?> clazz = context.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<?> annotationType = annotation.annotationType();
                EventBus evenBus = annotationType.getAnnotation(EventBus.class);
                if (evenBus == null) {
                    continue;
                }
                String listenerSetter = evenBus.listenerSetter();
                Class<?> listenerType = evenBus.listenerType();
                String callBackMethod = evenBus.callBackMethod();

                Map<String, Method> methodMap = new HashMap<>();
                methodMap.put(callBackMethod, method);

                try {
                    Method valueMethod = annotationType.getDeclaredMethod("value");
                    int[] viewIds = (int[]) valueMethod.invoke(annotation);
                    for (int id : viewIds) {
                        Method findViewById = clazz.getMethod("findViewById", int.class);
                        View view = (View) findViewById.invoke(context, id);
                        if (view == null) {
                            continue;
                        }
                        Method setListener = view.getClass().getMethod(listenerSetter, listenerType);
                        ListenerInvocationHandler handler = new ListenerInvocationHandler(context, methodMap);
                        Object proxy = Proxy.newProxyInstance(
                                listenerType.getClassLoader(),
                                new Class[]{listenerType},
                                handler);
                        setListener.invoke(view, proxy);
                    }

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private static void injectView(Activity context) {
        Class<?> clazz = context.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject == null) {
                continue;
            }
            int id = viewInject.value();
            try {
                Method findViewById = clazz.getMethod("findViewById", int.class);
                findViewById.setAccessible(true);
                Object view = findViewById.invoke(context, id);
                field.setAccessible(true);
                field.set(context, view);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param context
     */
    private static void injectLayout(Activity context) {
        Class<?> clazz = context.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        int layoutId = contentView.value();

        try {
            Method method = clazz.getMethod("setContentView", int.class);
            method.setAccessible(true);
            method.invoke(context, layoutId);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
