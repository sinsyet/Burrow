package com.example.burrowserver.eventbus;

import com.example.burrowserver.eventbus.anno.Subscribe;
import com.example.burrowserver.eventbus.bean.Event;
import com.example.burrowserver.utils.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {
    private static final String TAG = "EventBus";
    // private static List<Object> sSubscribers = new ArrayList<>();
    private static Map<Class,Object> sSubscribers = new HashMap<>();
    public static void subscribe(Object obj){
        if (obj == null) {
            return;
        }

        sSubscribers.put(obj.getClass(),obj);

    }

    public static void unSubscribe(Object obj){
        if(obj == null) return;

        sSubscribers.remove(obj);
    }

    public static void post(Object obj){
        Collection<Object> values = sSubscribers.values();
        for (Object subscriber : values) {
            if (subscriber == null) {
                continue;
            }

           findMethod2Post(subscriber,obj);
        }
    }

    public static void post(Event event){
        Class to = event.to;
        if(to == null) throw new IllegalArgumentException("to Class can't be null");

        Object toObj = sSubscribers.get(to);
        if(toObj == null) throw new IllegalArgumentException("no this subscriber");

        findMethod2Post(toObj,event.obj);
    }

    private static void findMethod2Post(Object toObj,Object paramObj){
        Class<?> clz = toObj.getClass();
        Method[] methods = clz.getMethods();
        for(Method m:methods){
            Subscribe annotationAnno = m.getAnnotation(Subscribe.class);
            if(annotationAnno == null){
                continue;
            }

            Class paramClass = annotationAnno.value();
            if(paramClass != paramObj.getClass()) continue;

            Class<?>[] parameterTypes = m.getParameterTypes();
            if(parameterTypes.length != 1) continue;   // 只能有一个参数

            if(parameterTypes[0] != paramClass)
                throw new IllegalStateException("annotation value must eq with paramter type");

            try {
                m.invoke(toObj,paramObj);
            } catch (Exception e) {
                Log.e(TAG,"error when post event"+e.getMessage());
            }
        }
    }
}
