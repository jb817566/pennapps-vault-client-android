package com.vaultapp.pennapps.vaultapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.reimaginebanking.api.nessieandroidsdk.constants.AccountType;
import com.reimaginebanking.api.nessieandroidsdk.models.Account;
import com.vaultapp.pennapps.vaultapp.security.SecurityStoreSingleton;

import java.security.SecureRandom;
import java.util.Set;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    public static Context ctx = null;
    public static String appDataDir = "";
    private static SecureRandom rand = new SecureRandom();
    private static GoogleApiClient mGoogleApiClient = null;
    private final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        if (!mGoogleApiClient.isConnected()) {
                            mGoogleApiClient.connect();
                        }
                        signIn();
                        break;
                    // ...
                }
            }
        });
        MainActivity.ctx = MainActivity.this;
        appDataDir = MainActivity.ctx.getFilesDir().getAbsolutePath();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestEmail().requestIdToken(getResources().getString(R.string.glclientidweb))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
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
//        String d = SecurityStoreSingleton.getInstance().Retreive();
//        //Communications.getAllAccountsForCustomer(d);
//        String acctNum = (System.currentTimeMillis() + "").substring(0, 13) + RandomString(3);
//        Communications.createNessieAccountForCustomer(d,
//                new Account.Builder()
//                        .balance(0)
//                        .type(AccountType.SAVINGS)
//                        .nickname(nickname)
//                        .rewards(0)
//                        .accountNumber(acctNum)
//                        .build(), acctType);
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

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //For Google SignON
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Set<String> datas = data.getExtras().keySet();
        String ext = data.getStringExtra("googleSignInStatus");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        Toast.makeText(MainActivity.ctx, "before", Toast.LENGTH_SHORT).show();
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        Toast.makeText(MainActivity.ctx, "after", Toast.LENGTH_SHORT).show();
        if (requestCode == RC_SIGN_IN) {
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Toast.makeText(MainActivity.ctx, "Successfully signed in w/ Google", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(MainActivity.ctx, result.getStatus().getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
