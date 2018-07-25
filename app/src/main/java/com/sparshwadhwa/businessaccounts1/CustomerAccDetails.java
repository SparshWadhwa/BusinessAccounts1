package com.sparshwadhwa.businessaccounts1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sparshwadhwa.businessaccounts1.Adapters.CustomerAccountDetailsAdapter;
import com.sparshwadhwa.businessaccounts1.Adapters.customersAdapter;

import java.util.ArrayList;

import static com.sparshwadhwa.businessaccounts1.MainActivity.UID;

public class CustomerAccDetails extends AppCompatActivity {
    private RecyclerView custDetailsRv;
    CustomerAccountDetailsAdapter madapetr1;
    private ArrayList<String> accDetails = new ArrayList<>();
    private DatabaseReference dBR;
    private TextView custName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_acc_details);
        custDetailsRv = findViewById(R.id.cust_acc_details_rv);

        String data= getIntent().getStringExtra("customerName");
        custName = findViewById(R.id.cust_name);
        custName.setText(data);
        dBR = FirebaseDatabase.getInstance().getReference();
        dBR.child(UID).child("accounts").child(data).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accDetails.clear();
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    accDetails.add(dsp.getValue().toString());
                }
                madapetr1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        custDetailsRv.setLayoutManager(mLayoutManager2);
        madapetr1 = new CustomerAccountDetailsAdapter(accDetails, getApplicationContext());
        custDetailsRv.setAdapter(madapetr1);
        madapetr1.notifyDataSetChanged();
    }
}
