package com.sparshwadhwa.businessaccounts1.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.sparshwadhwa.businessaccounts1.R;

import java.util.ArrayList;

public class CustomerAccountDetailsAdapter extends RecyclerView.Adapter<CustomerAccountDetailsAdapter.ViewHolder> {
    ArrayList<String> customerAccDetails = new ArrayList<>();
    Context context;
    public CustomerAccountDetailsAdapter(ArrayList<String> customerAccDetails, Context context) {
        this.customerAccDetails= customerAccDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomerAccountDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.cust_acc_detailsadapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        CustomerAccountDetailsAdapter.ViewHolder vh = new CustomerAccountDetailsAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] temp = customerAccDetails.get(position).split("imrich");
        if(temp.length==4)
        {
            holder.dueRemaining.setText(temp[0]);
            holder.itemName.setText(temp[1]);
            holder.itemCost.setText(temp[2]);
            holder.itemDatenTime.setText(temp[3]);
        }
//holder.itemName.setText(customerAccDetails.get(position));
//        holder.itemName.setText("123");
    }



    @Override
    public int getItemCount() {
        return customerAccDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView itemName , itemCost , dueRemaining , itemDatenTime;
        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_detail);
            itemCost = itemView.findViewById(R.id.item_cost);
            dueRemaining = itemView.findViewById(R.id.payment_due);
            itemDatenTime = itemView.findViewById(R.id.item_date);
        }
    }
}
