package com.sparshwadhwa.businessaccounts1;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

public class Authentication extends AppCompatActivity {

    private MaterialEditText etxtPhone;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private MaterialEditText etxtPhoneCode;
    private String mVerificationId;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mNameDatabaseReference;
    private DatabaseReference mPhnNo_databaseREference;

    public static String student_name="",student_phnNo="",user_email="";
    public  static String Uid;
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("Phone Number", etxtPhone.getText());
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        etxtPhone = (MaterialEditText) findViewById(R.id.phoneField);
        etxtPhoneCode = (MaterialEditText) findViewById(R.id.codeField);
        if (savedInstanceState != null) {
            CharSequence savedText = savedInstanceState.getCharSequence("Phone Number");
            etxtPhone.setText(savedText);
        }
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        mPhnNo_databaseREference = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(Authentication.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    public void requestCode(View view) {
        String phoneNumber = etxtPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber))
            return;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60, TimeUnit.SECONDS, Authentication.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        //Called if it is not needed to enter verification code
                        signInWithCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        //incorrect phone number, verification code, emulator, etc.
                        Toast.makeText(Authentication.this, "onVerificationFailed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("fb error hahah", e.getMessage() );
                        Log.i("hahaha", e.getMessage());
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        //now the code has been sent, save the verificationId we may need it
                        super.onCodeSent(verificationId, forceResendingToken);

                        mVerificationId = verificationId;
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String verificationId) {
                        //called after timeout if onVerificationCompleted has not been called
                        super.onCodeAutoRetrievalTimeOut(verificationId);
                        Toast.makeText(Authentication.this, "onCodeAutoRetrievalTimeOut :" + verificationId, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        etxtPhoneCode.setVisibility(View.VISIBLE);
        findViewById(R.id.signInBtn).setVisibility(View.VISIBLE);
    }

    private void signInWithCredential(final PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Authentication.this, "Success", Toast.LENGTH_SHORT).show();
                            student_phnNo = etxtPhone.getText().toString();
                            FirebaseUser user = task.getResult().getUser();
                            Uid= user.getUid();

                            mPhnNo_databaseREference.child("users").child(Uid).child("phone no:").setValue(student_phnNo);
//                            if (status == 1){
//                                mPhnNo_databaseREference.child("users").child(Uid).child("status").setValue("student");

                        } else {
                            Toast.makeText(Authentication.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(View view) {
        String code = etxtPhoneCode.getText().toString();
        if (TextUtils.isEmpty(code))
            return;

        signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId, code));
    }
}
