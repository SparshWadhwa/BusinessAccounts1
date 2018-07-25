package com.sparshwadhwa.businessaccounts1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DatabaseReference saveDBR;
    private FirebaseAuth mfirebaseAuth;
    private FirebaseAuth.AuthStateListener mfirebaseAuthstateListner;
    public static String UID = "";



    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseApp.initializeApp(this);
mfirebaseAuth = FirebaseAuth.getInstance();
if(mfirebaseAuth.getCurrentUser()==null)
{
    Intent loginIntent = new Intent(MainActivity.this, Authentication.class);
    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(loginIntent);
    Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
}
else
{
    UID = mfirebaseAuth.getCurrentUser().getUid();
    Toast.makeText(getApplicationContext(), "not null", Toast.LENGTH_SHORT).show();
    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    mViewPager = (ViewPager) findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    saveDBR = FirebaseDatabase.getInstance().getReference();


    fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LayoutInflater li = LayoutInflater.from(MainActivity.this);
            final View promptsView = li.inflate(R.layout.customerdetailsdialogbox, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setView(promptsView);
            // alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
            final TextView custName = promptsView.findViewById(R.id.customer_name);
            final TextView custPrevDue = promptsView.findViewById(R.id.custom_prevDues);
            alertDialogBuilder
                    .setTitle("Add new Customer")
                    .setCancelable(false)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            boolean temp = false;
                            for(int i=0;i<Customers.customerList.size();i++)
                            {
                                if(Customers.customerList.get(i).equals(custName.getText().toString())) {
                                    temp = true;
                                break;
                                }
                            }


                            if(custName.getText().toString().equals(""))
                                Toast.makeText(MainActivity.this, "Enter a Valid Name.", Toast.LENGTH_SHORT).show();
                            else if(temp)
                            {
                                Toast.makeText(MainActivity.this, "This name already Exist.", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                int dueTemp = 0;
                                if(custPrevDue.getText().toString().equals(""))
                                    dueTemp = 0;
                                else
                                    dueTemp = Integer.valueOf(custPrevDue.getText().toString());
                                SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                String format = s.format(new Date());
                                saveDBR.child(UID).child("customers").child(custName.getText().toString()).child("dues").setValue(dueTemp);
                                saveDBR.child(UID).child("accounts").child(custName.getText().toString()).push().setValue(dueTemp+"imrichPrev Dues"+"imrich"+dueTemp+"imrich"+format);
                            }
                            dialog.cancel();

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
}

mfirebaseAuthstateListner= new FirebaseAuth.AuthStateListener() {
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(mfirebaseAuth.getCurrentUser()==null) {

            Intent loginIntent = new Intent(MainActivity.this, Authentication.class);
            startActivity(loginIntent);
            Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
        }
        else
        {

        }
    }
};


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            mfirebaseAuth.signOut();
            startActivity(new Intent(this , Authentication.class));
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = new Customers();
                    break;
                case 1:
                    fragment = new Expenses();
                    break;
                case 2:
                    fragment = new AboutYou();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
