package com.cbooy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenhao on 2016/6/29.
 */
public final class InstanceFactory {

    private static final Map<String, Object> instanceMap = new HashMap<String, Object>(1024);
    private static final Object mutxLock = new Object();


    public static <T> T getSingleInstance(Class<T> clazz) throws Exception {
        String name = clazz.getName();
        T instance = (T)instanceMap.get(name);
        if (instance == null) {
            synchronized (mutxLock) {
                instance = (T)instanceMap.get(name);
                if (instance == null) {
                    instance = clazz.newInstance();
                    if (instance == null) {
                        throw new NullPointerException("new instance failed");
                    }
                    instanceMap.put(name, instance);
                }
            }
        }
        return instance;
    }
}