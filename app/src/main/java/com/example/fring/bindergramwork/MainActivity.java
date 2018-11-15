package com.example.fring.bindergramwork;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.processlib.connect.ProcessManager;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProcessManager.getInstance().regist(UserManager.class);
        UserManager.getInstance().setPerson(new Person("david","123456"));
    }

    public void change(View view){
        startActivity(new Intent(this,SecondActivity.class));
    }
}
