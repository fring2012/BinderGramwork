package com.example.processlib.connect;

import android.text.TextUtils;

import com.example.processlib.service.ProcessService;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProcessInvocationHandler implements InvocationHandler{
    private Class<?> clazz;
    private static final Gson gson = new Gson();
    public ProcessInvocationHandler(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String response = ProcessManager.getInstance().sendRequest(ProcessService.GET_METHOD,clazz,method,args);
        if (!TextUtils.isEmpty(response) && !"null".equals(response)) {
            Class userClass = method.getReturnType();
            Object o = gson.fromJson(response,userClass);
            return o;
        }
        return null;
    }
}
