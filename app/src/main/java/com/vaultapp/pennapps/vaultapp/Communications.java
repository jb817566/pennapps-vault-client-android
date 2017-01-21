package com.vaultapp.pennapps.vaultapp;

import com.reimaginebanking.api.nessieandroidsdk.NessieError;
import com.reimaginebanking.api.nessieandroidsdk.NessieResultsListener;
import com.reimaginebanking.api.nessieandroidsdk.constants.TransactionMedium;
import com.reimaginebanking.api.nessieandroidsdk.models.Customer;
import com.reimaginebanking.api.nessieandroidsdk.models.Deposit;
import com.reimaginebanking.api.nessieandroidsdk.models.PostResponse;
import com.reimaginebanking.api.nessieandroidsdk.requestclients.NessieClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by hhbhagat on 1/21/2017.
 */

public class Communications {

    private static NessieClient client = NessieClient.getInstance(MainActivity.ctx.getResources().getString(R.string.apiKey));
    private static DateFormat dformat = new SimpleDateFormat("yyyy-mm-dd");

    public static void sendButtonCall() {

    }

    public static String createNessieAccount(Customer account){

    }

    public static String sendNessieDepositCall(double originalAmt, string account_ID) {
        String desc = "";
        double depositAmt = Math.ceil(originalAmt) - originalAmt;
        Deposit deposit = new Deposit.Builder()
                .amount(depositAmt)
                .transactionDate(dformat.format(new Date()))
                .medium(TransactionMedium.BALANCE)
                .description(desc = String.format("Spent $%.2f, deposited $%.2f", originalAmt, depositAmt)).build();
        client.DEPOSIT.createDeposit(account_ID, deposit, new NessieResultsListener() {
            @Override
            public void onSuccess(Object result) {
                PostResponse<Deposit> response = (PostResponse<Deposit>) result;
            }

            @Override
            public void onFailure(NessieError error) {

            }
        });
        return desc;
    }
}
