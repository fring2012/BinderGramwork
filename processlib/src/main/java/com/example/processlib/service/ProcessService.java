package com.example.processlib.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.example.processlib.ProcessInterface;
import com.example.processlib.been.RequestBean;
import com.example.processlib.been.RequestParameter;
import com.example.processlib.cache.CacheCenter;
import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProcessService extends Service{
    //得到单例
    public static final int GET_INSTANCE = 1;
    //调用方法
    public static final int GET_METHOD = 2;

    Gson gson = new Gson();

    CacheCenter cacheCenter = CacheCenter.getInstance();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessInterface.Stub() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public String send(String request) throws RemoteException {
                RequestBean requestBean = gson.fromJson(request,RequestBean.class);
                int type = requestBean.getType();
                switch (type) {
                    case GET_INSTANCE:
                        //UserManager n
                        Method method = cacheCenter.getMethod(requestBean.getClassName(),"getInstance");
                        Object[] mParameter =  makeParameterObject(requestBean);
                        try {
                            Object object = method.invoke(null,mParameter);
                            cacheCenter.putObject(requestBean.getClassName(),object);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        break;
                    case GET_METHOD:
                        Object mObject = cacheCenter.getObject(requestBean.getClassName());
                        Method getPerson = cacheCenter.getMethod(requestBean.getClassName(),requestBean.getMethodName());
                        Object[] mParameters = makeParameterObject(requestBean);
                        try {
                            Object person = getPerson.invoke(mObject,mParameters);
                            String data = gson.toJson(person);
                            return data;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return null;
            }
        };
    }

    private Object[] makeParameterObject(RequestBean requestBean){
        Object[] mParameters = null;
        RequestParameter[] requestParameters = requestBean.getRequestParameters();
        if (requestParameters != null && requestParameters.length > 0) {
            for (int i = 0; i < requestParameters.length; i++) {
                RequestParameter requestParameter = requestParameters[i];
                Class<?> clazz = cacheCenter.getClassType(requestParameter.getParameterClassName());
                mParameters[i] = gson.fromJson(requestParameter.getParameterValue(),clazz);
            }
        } else {
            mParameters = new Object[0];
        }
        return mParameters;
    }
}
