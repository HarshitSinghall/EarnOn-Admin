package com.developers.EarnOn_Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.developers.EarnOn_Admin.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;

public class loginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    EditText mobileNumber;
    Button signIn;
    String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mobileNumber = binding.mobileNoId;
        signIn = binding.signInId;


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = mobileNumber.getText().toString().trim();
                if (num.length() == 10) {
                    num = "+91" + num;
                    Intent intent = new Intent(loginActivity.this, otpVerification.class);
                    intent.putExtra("from", "login");
                    intent.putExtra("number", num);
                    startActivity(intent);
                } else {
                    Snackbar.make(binding.getRoot(), "Enter a valid number", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }


}