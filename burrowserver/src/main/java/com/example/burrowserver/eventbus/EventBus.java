package com.example.burrowserver.eventbus;

import com.example.burrowserver.eventbus.anno.Subscribe;
import com.example.burrowserver.utils.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventBus {
    private static final String TAG = "EventBus";
    private static List<Object> sSubscribers = new ArrayList<>();
    public static void subscribe(Object obj){
        if (obj == null) {
            return;
        }

        sSubscribers.add(obj);
    }

    public static void unSubscribe(Object obj){
        if(obj == null) return;

        sSubscribers.remove(obj);
    }

    public static void post(Object obj){
        for (Object subscriber : sSubscribers) {
            if (subscriber == null) {
                continue;
            }

            Class<?> clz = subscriber.getClass();
            Method[] methods = clz.getMethods();
            for(Method m:methods){
                Subscribe annotationAnno = m.getAnnotation(Subscribe.class);
                if(annotationAnno == null){
                    continue;
                }

                Class paramClass = annotationAnno.value();
                if(paramClass != obj.getClass()) continue;
                int parameterCount = m.getParameterCount();

                Class<?>[] parameterTypes = m.getParameterTypes();
                if(parameterCount != 1) continue;   // 只能有一个参数

                if(parameterTypes[0] != paramClass)
                    throw new IllegalStateException("annotation value must eq with paramter type");

                try {
                    m.invoke(subscriber,obj);
                } catch (Exception e) {
                    Log.e(TAG,"error when post event"+e.getMessage());
                }
            }
        }
    }
}
