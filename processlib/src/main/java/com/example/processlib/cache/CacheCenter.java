package com.example.processlib.cache;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;

public class CacheCenter {
    private CacheCenter(){

    }

    private static volatile CacheCenter instance = new CacheCenter();

    public static CacheCenter getInstance(){
        return instance;
    }

    private HashMap<String,Class<?>> mClasses = new HashMap<>();

    private HashMap<Class<?>,HashMap<String,Method>> mMethods = new HashMap<>();

    private HashMap<String,Object> mObjects = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void register(Class<?> clazz){
        mClasses.put(clazz.getName(),clazz);

        registerMethod(clazz);
    }

    public Object getObject(String name){
        return mObjects.get(name);
    }

    public void putObject(String name,Object object){
        mObjects.put(name,object);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerMethod(Class<?> clazz){
        Method[] methods = clazz.getMethods();
        mMethods.putIfAbsent(clazz,new HashMap<String, Method>());
        HashMap<String,Method> map = mMethods.get(clazz);
        for (Method method : methods){
            map.put(method.getName(),method);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Method getMethod(String className, String name){
        Class clazz = getClassType(className);
        if (name != null) {
            Log.i("david","getMethod: 1======" + name);
            mMethods.putIfAbsent(clazz,new HashMap<String, Method>());
            HashMap<String,Method> methods = mMethods.get(clazz);
            Method method = methods.get(name);
            if (method != null){
                return method;
            }
        }
        return null;
    }

    public Class<?> getClassType(String name) {
        if (TextUtils.isEmpty(name)){
            return null;
        }
        Class<?> clazz = mClasses.get(name);
        if (clazz == null) {
            try {
                clazz = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }
}
