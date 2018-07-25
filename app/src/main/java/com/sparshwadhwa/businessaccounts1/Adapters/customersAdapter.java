package com.sparshwadhwa.businessaccounts1.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sparshwadhwa.businessaccounts1.CustomerAccDetails;
import com.sparshwadhwa.businessaccounts1.Customers;
import com.sparshwadhwa.businessaccounts1.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.sparshwadhwa.businessaccounts1.MainActivity.UID;

public class customersAdapter extends RecyclerView.Adapter<customersAdapter.ViewHolder> {
    private ArrayList<String> customerNames , customerDues;
    private Context context;
    private DatabaseReference saveDBR;

    public customersAdapter(ArrayList<String> customerNames,ArrayList<String> customerDues, Context context) {
        this.customerNames = customerNames;
        this.customerDues = customerDues;
        this.context = context;
    }

    @NonNull

    @Override
    public customersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.custmoersadapter, parent, false);
        saveDBR = FirebaseDatabase.getInstance().getReference();
        // set the view's size, margins, paddings and layout parameters
        customersAdapter.ViewHolder vh = new customersAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull customersAdapter.ViewHolder holder, final int position) {
        if(customerDues.size()>0) {
            holder.username.setText(customerNames.get(position));
//        Log.i("size frm adapter hahaha", customerNames.size()+"and"+customerDues.size()+"");
            holder.dues.setText(customerDues.get(position));

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater li = LayoutInflater.from(context);
                    final View promptsView = li.inflate(R.layout.add_item_dialogbox, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setView(promptsView);
                    final EditText itemName = promptsView.findViewById(R.id.item_name);
                    final EditText itemCost = promptsView.findViewById(R.id.item_cost);
                    // alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
                    alertDialogBuilder
                            .setTitle("Add in the acc of " + customerNames.get(position))
                            .setCancelable(false)
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int temp = Integer.valueOf(customerDues.get(position)) + Integer.valueOf(itemCost.getText().toString());
                                    SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    String format = s.format(new Date());
                                    saveDBR.child(UID).child("accounts").child(customerNames.get(position)).push().setValue(temp + "imrich" + itemName.getText().toString() + "imrich" + itemCost.getText().toString() + "imrich" + format);
                                    saveDBR.child(UID).child("customers").child(customerNames.get(position)).child("dues").setValue(temp);

                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
            holder.payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "PAYMENT" + customerNames.get(position), Toast.LENGTH_SHORT).show();
                    LayoutInflater li = LayoutInflater.from(context);
                    final View promptsView = li.inflate(R.layout.addamountdialogbox, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    final EditText receivedPayment = promptsView.findViewById(R.id.payment);
                    alertDialogBuilder.setView(promptsView);
                    // alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
                    alertDialogBuilder
                            .setTitle("Payment Received from " + customerNames.get(position))
                            .setCancelable(false)
                            .setPositiveButton("Received", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    int temp = Integer.valueOf(customerDues.get(position)) - Integer.valueOf(receivedPayment.getText().toString());
                                    Date currentTime = Calendar.getInstance().getTime();
                                    SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    String format = s.format(currentTime);
                                    saveDBR.child(UID).child("accounts").child(customerNames.get(position)).push().setValue(temp + "imrich" + "Payment Received :" + "imrich" + receivedPayment.getText().toString() + "imrich" + format);
                                    saveDBR.child(UID).child("customers").child(customerNames.get(position)).child("dues").setValue(temp);
                                    saveDBR.child(UID).child("totalreceivedammount").setValue(Customers.totalReceivedAmmount + Integer.valueOf(receivedPayment.getText().toString()));

                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
            holder.username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CustomerAccDetails.class);
                    intent.putExtra("customerName", customerNames.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return customerNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username , dues;
        private ImageButton add, payment;
        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.customer_name);
            dues = itemView.findViewById(R.id.textview_dues);
            add = itemView.findViewById(R.id.ADD);
            payment = itemView.findViewById(R.id.payment_collected);

        }
    }
}
