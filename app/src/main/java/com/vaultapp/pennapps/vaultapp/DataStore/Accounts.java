package com.vaultapp.pennapps.vaultapp.DataStore;

import com.reimaginebanking.api.nessieandroidsdk.models.Account;
import com.vaultapp.pennapps.vaultapp.EnumAccountType;

import java.util.HashMap;

/**
 * Created by hhbhagat on 1/21/2017.
 */

public class Accounts {

    private static HashMap<String, Account> AccountStore = new HashMap<String, Account>();

    public static void addAccount(String acctRef, Account account, EnumAccountType acctType) {
        if (acctType == EnumAccountType.REGULAR) {
            AccountStore.put(acctRef + "_" + EnumAccountType.REGULAR.toString(), account);
        } else if (acctType == EnumAccountType.CHARITY) {
            AccountStore.put(acctRef + "_" + EnumAccountType.CHARITY.toString(), account);
        }
    }

    public static EnumAccountType containsAccount(String acctRef) {
        if (AccountStore.containsKey(acctRef + "_REGULAR")) {
            return EnumAccountType.REGULAR;
        } else if (AccountStore.containsKey(acctRef + "_CHARITY")) {
            return EnumAccountType.CHARITY;
        } else {
            return EnumAccountType.NONE;
        }
    }


}
