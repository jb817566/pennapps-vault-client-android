package com.vaultapp.pennapps.vaultapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.reimaginebanking.api.nessieandroidsdk.models.Account;
import com.vaultapp.pennapps.vaultapp.DataStore.Accounts;

import java.util.Map;

public class AccountSummary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected void DeleteAcct(View v) {

        boolean success = false;
        if (success) {
            TextView currentNick = (TextView) findViewById(R.id.acctInfoNickname);
            Map.Entry<String, Account> entry = Accounts.GetAccountByNickname(currentNick.getText().toString());
            EnumAccountType type = null;
            if (entry.getKey().contains(EnumAccountType.CHARITY.toString())) {
                type = EnumAccountType.CHARITY;
            } else if (entry.getKey().contains(EnumAccountType.REGULAR.toString())) {
                type = EnumAccountType.REGULAR;
            }
            Accounts.deleteAccount(entry.getValue().getId(), type);
        }
    }
}
