package com.vaultapp.pennapps.vaultapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.reimaginebanking.api.nessieandroidsdk.constants.AccountType;
import com.reimaginebanking.api.nessieandroidsdk.models.Account;
import com.usebutton.sdk.Button;
import com.vaultapp.pennapps.vaultapp.security.SecurityStoreSingleton;

import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {

    public static Context ctx = null;
    public static String appDataDir = "";
    private static SecureRandom rand = new SecureRandom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.ctx = MainActivity.this;
        appDataDir = MainActivity.ctx.getFilesDir().getAbsolutePath();
        if (BuildConfig.DEBUG) {
            Button.getButton(this).enableDebugLogging();
        }
        Button.getButton(this).start();
    }

    protected void SendDeposit(View v) {
        EnumAccountType acctType = null;
        String nickname = null;
//        Communications.sendNessieDepositCall(19.95d);
//        Customer customer = new Customer.Builder().firstName("Spongebob")
//                .lastName("Squarepants").address(new Address.Builder()
//                        .city("46")
//                        .state("PA")
//                        .zip("11111")
//                        .streetName("4564")
//                        .streetNumber((new Random().nextInt() % 1100) + "")
//                        .build()).
//                        build();
//        Communications.createNessieCustomer(customer);
        String d = SecurityStoreSingleton.getInstance().Retreive();
        //Communications.getAllAccountsForCustomer(d);
        String acctNum = (System.currentTimeMillis() + "").substring(0, 13) + RandomString(3);
        Communications.createNessieAccountForCustomer(d,
                new Account.Builder()
                        .balance(0)
                        .type(AccountType.SAVINGS)
                        .nickname(nickname)
                        .rewards(0)
                        .accountNumber(acctNum)
                        .build(), acctType);
    }

    private String RandomString(int n) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            builder.append(rand.nextInt() % 9);
        }
        return builder.toString();
    }

    protected void Login(View v) {
    }

}
