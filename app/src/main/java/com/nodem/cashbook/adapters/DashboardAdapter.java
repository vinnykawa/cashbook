package com.nodem.cashbook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.vmk.cashbook.R;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder>{

    private Context context;
    private List<String> accounts;
    private List<Integer> totals;

    public DashboardAdapter(Context context, List<String> accounts, List<Integer> totals) {
        this.context=context;
        this.accounts =accounts;
        this.totals=totals;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_list,null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.transaction.setText(accounts.get(position));
        holder.categoryImg.setImageResource(R.drawable.backup_icon);
        holder.amount.setText(totals.get(position));

    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView transaction,amount;
        ImageView categoryImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            transaction = itemView.findViewById(R.id.transactionsTXT);
            categoryImg = itemView.findViewById(R.id.categoryImg);
            amount = itemView.findViewById(R.id.amountTXT);

        }
    }


}
