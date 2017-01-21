package com.vaultapp.pennapps.vaultapp.DataStore;

import com.reimaginebanking.api.nessieandroidsdk.models.Account;

import java.util.HashMap;

/**
 * Created by hhbhagat on 1/21/2017.
 */

public class Accounts {

    private static HashMap<String, Account> AccountStore = new HashMap<String, Account>();

    public static void addAccount(String acctRef, Account account) {
        AccountStore.put(acctRef, account);
    }

    public static boolean containsAccount(String acctRef) {
        return AccountStore.containsKey(acctRef);
    }
}
