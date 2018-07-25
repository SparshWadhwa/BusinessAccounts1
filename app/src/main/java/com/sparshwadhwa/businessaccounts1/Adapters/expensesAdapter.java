package com.sparshwadhwa.businessaccounts1.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.sparshwadhwa.businessaccounts1.R;

import java.util.ArrayList;

public class expensesAdapter extends RecyclerView.Adapter<expensesAdapter.ViewHolder> {
    private ArrayList<String> expensesList = new ArrayList<>();
    Context context;
    public expensesAdapter(ArrayList<String> expensesList, Context context) {
        this.expensesList = expensesList;
        this.context = context;
    }

    @NonNull
    @Override
    public expensesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.expanes_adapter, parent, false);

        // set the view's size, margins, paddings and layout parameters
        expensesAdapter.ViewHolder vh = new expensesAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull expensesAdapter.ViewHolder holder, int position) {
        holder.itemName.setText(expensesList.get(position));

    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName , itemDate , itemPrice , totalPrice;
        public ViewHolder(View v) {
            super(v);
            itemName = v.findViewById(R.id.item_name);
            itemDate = v.findViewById(R.id.item_date);
            itemPrice = v.findViewById(R.id.item_price);
            totalPrice = v.findViewById(R.id.total_ammount);

        }
    }
}
