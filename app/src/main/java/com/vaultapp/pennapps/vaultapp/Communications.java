package com.vaultapp.pennapps.vaultapp;

import android.widget.Toast;

import com.reimaginebanking.api.nessieandroidsdk.NessieError;
import com.reimaginebanking.api.nessieandroidsdk.NessieResultsListener;
import com.reimaginebanking.api.nessieandroidsdk.constants.TransactionMedium;
import com.reimaginebanking.api.nessieandroidsdk.models.Account;
import com.reimaginebanking.api.nessieandroidsdk.models.Customer;
import com.reimaginebanking.api.nessieandroidsdk.models.Deposit;
import com.reimaginebanking.api.nessieandroidsdk.models.PostResponse;
import com.reimaginebanking.api.nessieandroidsdk.requestclients.NessieClient;
import com.vaultapp.pennapps.vaultapp.DataStore.Accounts;
import com.vaultapp.pennapps.vaultapp.security.Hashing;
import com.vaultapp.pennapps.vaultapp.security.SecurityStoreSingleton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hhbhagat on 1/21/2017.
 */

public class Communications {

    private static NessieClient client = NessieClient.getInstance(MainActivity.ctx.getResources().getString(R.string.apiKey));
    private static DateFormat dformat = new SimpleDateFormat("yyyy-mm-dd");

    ///CUSTOMER -> ACCOUNT -> DEPOSIT

    public static void sendButtonCall() {

    }

    //Account Operations
    public static List<Deposit> getAllDepositsForAccount(Account acct) {
        final ArrayList<List<Deposit>> deposits = new ArrayList<List<Deposit>>();
        client.DEPOSIT.getDeposits(acct.getId(), new NessieResultsListener() {
            @Override
            public void onSuccess(Object result) {
                deposits.add((List<Deposit>) result);
            }

            @Override
            public void onFailure(NessieError error) {
                Toast.makeText(MainActivity.ctx, error.getMessage(), Toast.LENGTH_SHORT);
            }
        });
        return deposits.get(0);
    }


    //acc

    //Customer Operations

    //returns dictionary key with alias
    public static String getAllAccountsForCustomer(String acctID) {
        String accountHash = null;
        client.ACCOUNT.getAccount(acctID, new NessieResultsListener() {
            @Override
            public void onSuccess(Object result) {
                List<Account> accts = (List<Account>) result;
                for (Account acct : accts) {
                    Accounts.addAccount(
                            Hashing.SHA1(acct.getAccountNumber()),
                            acct);
                }
            }

            @Override
            public void onFailure(NessieError error) {
                Toast.makeText(MainActivity.ctx, error.getMessage(), Toast.LENGTH_SHORT);
            }
        });
        return accountHash;
    }

    public static String createNessieCustomer(Customer account) {
        client.CUSTOMER.createCustomer(account, new NessieResultsListener() {
            @Override
            public void onSuccess(Object result) {
                PostResponse<Customer> response = (PostResponse<Customer>) result;
                SecurityStoreSingleton.getInstance().Store(response.getObjectCreated().getId());
                Toast.makeText(MainActivity.ctx, MainActivity.ctx.getResources().getString(R.string.AccountCreated), Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(NessieError error) {
                Toast.makeText(MainActivity.ctx, error.getMessage(), Toast.LENGTH_SHORT);
            }
        });
        return "";
    }

    public static String sendNessieDepositCall(double originalAmt, String account_ID) {
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
