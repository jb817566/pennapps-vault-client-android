package com.vaultapp.pennapps.vaultapp.googlecomm;

/**
 * Created by hhbhagat on 1/22/2017.
 */

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.android.vending.billing.IInAppBillingService;
import com.vaultapp.pennapps.vaultapp.util.IabHelper;
import com.vaultapp.pennapps.vaultapp.util.IabResult;
import com.vaultapp.pennapps.vaultapp.util.Inventory;


public class PurchaseProvider {
    final ServiceConnection mServiceConn;
    IInAppBillingService mService;

    public PurchaseProvider() {
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

        };
    }

    public ServiceConnection getConn() {
        return mServiceConn;
    }

    public IInAppBillingService getService() {
        return mService;
    }
}

class InvFinishedListener implements IabHelper.QueryInventoryFinishedListener {

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
    }
}
