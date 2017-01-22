package com.vaultapp.pennapps.vaultapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.reimaginebanking.api.nessieandroidsdk.models.Account;

import java.util.List;

/**
 * Created by hhbhagat on 1/22/2017.
 */


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Account> accounts;
    private Context ctx = null;

    public RecyclerViewAdapter(Context ctx, List<Account> listAccts) {
        this.accounts = listAccts;
        this.ctx = ctx;
    }

    private Context getContext() {
        return ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View accountView = /*inflater.inflate(R.layout.item_contact, parent, false);*/ null;

        ViewHolder viewHolder = new ViewHolder(accountView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Account acct = this.accounts.get(position);
        //set vis of overlay to true
        //scrollview of transaction
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //row items
        public TextView nameTextView;
        public Button messageButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.acctrowText);
            messageButton = (Button) itemView.findViewById(R.id.acctrowButton);
        }
    }
}
