package com.sparshwadhwa.businessaccounts1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sparshwadhwa.businessaccounts1.Adapters.customersAdapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.sparshwadhwa.businessaccounts1.MainActivity.UID;


/**
 * A simple {@link Fragment} subclass.
 */
public class Customers extends Fragment {

    public static ArrayList<String> customerList = new ArrayList<>();
    private ArrayList<String> customerDuesList = new ArrayList<>();
    private RecyclerView customersRecycleView;
    customersAdapter madapetr;
    private DatabaseReference customerListDBR;
    public static int totalReceivedAmmount = 0;
    private DatabaseReference saveDBR;
    public Customers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_customers, container, false);
       saveDBR = FirebaseDatabase.getInstance().getReference().child(UID);
       saveDBR.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.child("totalreceivedammount").getValue()!=null)
          totalReceivedAmmount = Integer.valueOf(dataSnapshot.child("totalreceivedammount").getValue().toString());
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

        customersRecycleView = view.findViewById(R.id.customer_recyler_view);
        customerListDBR = FirebaseDatabase.getInstance().getReference().child(UID).child("customers");
        customerListDBR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                customerList.clear();
                customerDuesList.clear();
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    customerList.add(dsp.getKey());
                    customerDuesList.add(dsp.child("dues").getValue().toString());
                }
                madapetr.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(customerList.size()==0) {
            customerList.add("No Customers");
            customerDuesList.add("00");
        }
//        madapter=new customersAdapter(customerList, this);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext());
        customersRecycleView.setLayoutManager(mLayoutManager2);
        madapetr = new customersAdapter(customerList ,customerDuesList, getContext());
        customersRecycleView.setAdapter(madapetr);
        madapetr.notifyDataSetChanged();


        return view;
    }

}
