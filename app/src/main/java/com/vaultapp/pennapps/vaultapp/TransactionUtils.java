package com.vaultapp.pennapps.vaultapp;

import com.reimaginebanking.api.nessieandroidsdk.models.Deposit;

import java.util.List;

/**
 * Created by hhbhagat on 1/21/2017.
 */

public class TransactionUtils {

    //used to calculated the average transaction amount to accounts designated for charity
    public static String calculateAverageDonationAmount(List<Deposit> deposits) {
        double total = 0;
        for (Deposit deposit : deposits) {
            total += deposit.getAmount();
        }
        return String.format("%.2f", total / deposits.size());
    }


}
