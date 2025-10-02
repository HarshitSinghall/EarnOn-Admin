package com.developers.EarnOn_Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class splashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null){
            startActivity(new Intent(this, loginActivity.class));
        }else{
            startActivity(new Intent(this, MainActivity.class));
        }

    }


}