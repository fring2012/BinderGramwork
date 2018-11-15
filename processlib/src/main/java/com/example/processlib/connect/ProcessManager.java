package com.example.processlib.connect;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.example.processlib.ProcessInterface;
import com.example.processlib.annotion.ClassId;
import com.example.processlib.been.RequestBean;
import com.example.processlib.been.RequestParameter;
import com.example.processlib.cache.CacheCenter;
import com.example.processlib.service.ProcessService;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProcessManager {
    private static final ProcessManager ourInstance = new ProcessManager();
    CacheCenter cacheCenter = CacheCenter.getInstance();
    private Gson gson = new Gson();
    ProcessInterface processInterface;

    public static ProcessManager getInstance() {
        return ourInstance;
    }

    private ProcessManager() {
    }
    //  注册
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void regist(Class<?> clazz){
        cacheCenter.register(clazz);

    }

    public void connect(Context context){
        bind(context,null,ProcessService.class);
    }
    public void connect(Context context, String packageName) {
        bind(context,packageName,ProcessService.class);
    }
    public void bind(Context context, String packageName, Class<? extends ProcessService> service) {
        ProcessConnection connection = new ProcessConnection();
        Intent intent;
        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent(context,service);
        } else {
            intent = new Intent();
            intent.setPackage(packageName);
            intent.setAction(service.getName());
        }
        //启动服务
        context.bindService(intent,connection,Context.BIND_AUTO_CREATE);
    }
    public <T> T  getInstance(Class<?> clazz,Object... parameters) {
        String responce = sendRequest(ProcessService.GET_INSTANCE,clazz,null,parameters);
        ClassLoader classLoader = clazz.getClassLoader();
        T proxy = (T)Proxy.newProxyInstance(classLoader,new Class<?>[]{clazz},new ProcessInvocationHandler(clazz));
        return proxy;
    }
    //getPerson()(Person person)
    public String sendRequest(int type, Class<?> clazz, Method method, Object[] parameters) {
        RequestParameter[] requestParameters = null;
        if (parameters != null && parameters.length > 0){
            requestParameters = new RequestParameter[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                String parameterClassName = parameter.getClass().getName();
                String parameterValue = gson.toJson(parameter);
                RequestParameter requestParameter = new RequestParameter(parameterClassName,parameterValue);
                requestParameters[i] = requestParameter;
            }
        }
        String className = clazz.getAnnotation(ClassId.class).value();
        String methodName = method == null ? "getInstance" : method.getName();
        RequestBean requestBean = new RequestBean(type,className,methodName,requestParameters);
        String request = gson.toJson(requestBean);
        try {
            String responce = processInterface.send(request);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class ProcessConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            processInterface = ProcessInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("david", "onServiceDisconnected:" + name.getClassName());
        }
    }
}
