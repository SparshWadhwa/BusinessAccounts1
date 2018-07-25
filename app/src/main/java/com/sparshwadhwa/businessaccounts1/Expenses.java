package com.sparshwadhwa.businessaccounts1;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sparshwadhwa.businessaccounts1.Adapters.expensesAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.sparshwadhwa.businessaccounts1.MainActivity.UID;


/**
 * A simple {@link Fragment} subclass.
 */
public class Expenses extends Fragment {
    private RecyclerView expensesRV;
    private ArrayList<String> expenseList = new ArrayList<>();
    expensesAdapter madapetr;
    private FloatingActionButton addExpense;
    private DatabaseReference DBR;


    public Expenses() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_expenses, container, false);
        expensesRV = view.findViewById(R.id.expenses_rv);
        addExpense = view.findViewById(R.id.add_expense);
DBR = FirebaseDatabase.getInstance().getReference();
        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(getContext());
                final View promptsView = li.inflate(R.layout.addexpnse_dialog_box, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setView(promptsView);
                // alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
              final EditText itemName = promptsView.findViewById(R.id.item_name_et);
                final EditText itemprice = promptsView.findViewById(R.id.itemprice_et);
                final EditText total = promptsView.findViewById(R.id.itemTotal_et);

                alertDialogBuilder
                        .setTitle("Add Expense")
                        .setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String nameTemp, priceTemp, totalTemp ;
                                nameTemp = itemName.getText().toString();
                                priceTemp = itemprice.getText().toString();
                                totalTemp = total.getText().toString();
                                if(nameTemp.equals(""))
                                    Toast.makeText(getContext(), "Enter Item", Toast.LENGTH_SHORT).show();
                                else if(priceTemp.equals(""))
                                    Toast.makeText(getContext(), "Enter Price", Toast.LENGTH_SHORT).show();
                                else if(totalTemp.equals(""))
                                    Toast.makeText(getContext(), "Enter Total Price", Toast.LENGTH_SHORT).show();
                                else
                               DBR.child(UID).child("expenses").push().setValue("asxa");

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


        expenseList.add("sdx");
        expenseList.add("222");
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext());
        expensesRV.setLayoutManager(mLayoutManager2);
        madapetr = new expensesAdapter(expenseList, getContext());
        expensesRV.setAdapter(madapetr);
        madapetr.notifyDataSetChanged();

        return view;
    }

}
