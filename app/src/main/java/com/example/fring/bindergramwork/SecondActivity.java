package com.example.fring.bindergramwork;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.processlib.connect.ProcessManager;

public class SecondActivity extends Activity{
    IUserManager userManager;
    private static String TAG = "SecondActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ProcessManager.getInstance().connect(this);
    }

    public void userManager(View view){
        userManager = ProcessManager.getInstance()
                .getInstance(IUserManager.class);
        userManager.getPerson();
    }
    public void getUser(View view) {
        Log.e(TAG,userManager.getPerson().toString());
    }
}
