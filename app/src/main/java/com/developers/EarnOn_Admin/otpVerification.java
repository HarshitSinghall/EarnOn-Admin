package com.developers.EarnOn_Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.developers.EarnOn_Admin.databinding.ActivityOtpVerificationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class otpVerification extends AppCompatActivity {

    String number;
    ActivityOtpVerificationBinding binding;
    Button continueBtn;
    PinView pinView;
    TextView numberText;
    String otpId;
    FirebaseAuth mAuth;
    String Name, email, refer_code;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        continueBtn = binding.continueId;
        pinView = binding.PinView;
        numberText = binding.mobileNoId;
        mAuth = FirebaseAuth.getInstance();

        Intent i = getIntent();
        from = i.getStringExtra("from");
        if (from == "login"){
        }else{
            Name = i.getStringExtra("name");
            email = i.getStringExtra("email");
            refer_code = i.getStringExtra("raferal");
        }
        number = i.getStringExtra("number");

        numberText.setText(number);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinView.length() != 6){
                    Snackbar.make(binding.getRoot(), "Enter a valid otp", Snackbar.LENGTH_SHORT).show();
                }else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId, pinView.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        genrateOtp();

    }

    public void genrateOtp(){
        PhoneAuthProvider.getInstance(mAuth).verifyPhoneNumber(
                number,
                100,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        pinView.setText(phoneAuthCredential.getSmsCode());
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.d("harshit", "onVerificationFailed: " + e.getLocalizedMessage());
                        Snackbar.make(binding.getRoot(), e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Snackbar.make(binding.getRoot(), "OTP sent successfully", Snackbar.LENGTH_SHORT).show();
                        otpId = s;
                    }
                });


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Snackbar.make(binding.getRoot(), "Welcome!!", Snackbar.LENGTH_SHORT).show();
                            startActivity(new Intent(otpVerification.this, MainActivity.class));
                        }else{
                            Snackbar.make(binding.getRoot(), task.getException().getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void closeScreen(View view) {
        finish();
    }
}