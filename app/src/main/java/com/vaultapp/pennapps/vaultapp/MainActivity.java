package com.vaultapp.pennapps.vaultapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.vaultapp.pennapps.vaultapp.googlecomm.PurchaseProvider;

import java.security.SecureRandom;
import java.util.Set;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    public static Context ctx = null;
    public static String appDataDir = "";
    private static SecureRandom rand = new SecureRandom();
    private static GoogleApiClient mGoogleApiClient = null;
    private final int RC_SIGN_IN = 9001;
    private PurchaseProvider billingProvider = null;
    private GoogleSignInResult signin_rslt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.ctx = MainActivity.this;

        final Intent billingIntent = new Intent(MainActivity.this, com.android.vending.billing.IInAppBillingService.class);
        bindService(billingIntent, (billingProvider = new PurchaseProvider()).getConn(), Context.BIND_AUTO_CREATE);

        final SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:

                        final OptionalPendingResult<GoogleSignInResult> pendingResult =
                                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
                        if (pendingResult.isDone()) {
                            // There's immediate result available.
                            Toast.makeText(MainActivity.ctx, (signin_rslt = pendingResult.get()).getStatus() + "", Toast.LENGTH_SHORT).show();
                        } else {
                            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                                @Override
                                public void onResult(@NonNull GoogleSignInResult result) {
                                    signin_rslt = result;
                                    Toast.makeText(MainActivity.ctx, result.getStatus() + "", Toast.LENGTH_LONG).show();
                                }
                            });
                        }


                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... params) {

                                try {
                                    while (!pendingResult.isDone()) {
                                        try {
                                            Thread.sleep(200);
                                        } catch (InterruptedException e) {
                                        }
                                        if (signin_rslt != null && signin_rslt.getStatus().isSuccess()) {
                                            Log.d(TAG, "Logged in quietly");
                                            return null;
                                        }
                                    }
                                    if (!mGoogleApiClient.isConnected()) {
                                        mGoogleApiClient.connect();
                                    }
                                    signIn();
                                } catch (Exception e) {
                                    Log.d(TAG, e.toString());
                                }
                                return null;
                            }
                        }.execute();
                        break;
                    // ...

                }
//                Bundle skusBundle = new Bundle();
//                skusBundle.putStringArrayList("ITEM_ID_LIST", new ArrayList<String>());
//                try {
//                    Bundle response = billingProvider.getService().getSkuDetails(3, "com.king.candycrushsaga", "inapp", skusBundle);
//                    Log.d(TAG, Arrays.toString(response.getStringArrayList(null).toArray()));
//                } catch (RemoteException e) {
//                    Log.d(TAG, e.toString());
//                }
            }
        });


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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (billingProvider != null) {
            unbindService(billingProvider.getConn());
        }
    }

    protected void SendDeposit(View v) {
        EnumAccountType acctType = null;
        String nickname = null;
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
        EditText text = (EditText) findViewById(R.id.amountEntry);
        double amount = Double.parseDouble(text.getText().toString());
        //   Communications.sendNessieDepositCall(amount, );


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
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (requestCode == RC_SIGN_IN) {
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            Log.d(TAG, "asdfsaf");
//            Log.d(TAG, result.getSignInAccount().toString());

//            Log.d(TAG, result.getSignInAccount().getIdToken());
//            if (result.isSuccess()) {
//                Toast.makeText(MainActivity.ctx, "Successfully signed in w/ Google", Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.ctx, result.getSignInAccount().getDisplayName(), Toast.LENGTH_LONG).show();
//
//            } else {
//                //Toast.makeText(MainActivity.ctx, result.getStatus().getStatusCode(), Toast.LENGTH_SHORT).show();
//            }
        }
    }

}
