package com.vaultapp.pennapps.vaultapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static Context ctx = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.ctx = MainActivity.this;

    }

    protected void SendDeposit(View v){
        Communications.sendNessieDepositCall(19.95d);
    }

}
