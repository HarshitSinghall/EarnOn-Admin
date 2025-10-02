package com.developers.EarnOn_Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.developers.EarnOn_Admin.databinding.ActivityMainBinding;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Button notificationButton, LeadBtn;
    FirebaseDatabase database;

    public static String error = "Error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notificationButton = binding.NotificationButton;
        LeadBtn = binding.LeadsButton;
        database = FirebaseDatabase.getInstance();


        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Notifications.class));
            }
        });

        LeadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Leads.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.errorMSG.setText(error);
    }

    public void taskScreen(View view) {
        startActivity(new Intent(MainActivity.this, taskDetails.class));
    }

    public void RedeemScreen(View view) {
        startActivity(new Intent(MainActivity.this, Redeem.class));
    }

    public void referralScreen(View view) {
        startActivity(new Intent(MainActivity.this, refferalActivity.class));
    }

    public void Query(View view) {
        startActivity(new Intent(MainActivity.this, Query.class));
    }

    public void deleteTask(View view) {
        startActivity(new Intent(MainActivity.this, deleteTask.class));
    }
}