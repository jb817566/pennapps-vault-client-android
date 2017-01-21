package com.vaultapp.pennapps.vaultapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static Context ctx = null;
    public static String appDataDir = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.ctx = MainActivity.this;
        appDataDir = MainActivity.ctx.getFilesDir().getAbsolutePath();
    }

    protected void SendDeposit(View v) {
//        Communications.sendNessieDepositCall(19.95d);
    }

    protected void Login(View v) {
    }

}
