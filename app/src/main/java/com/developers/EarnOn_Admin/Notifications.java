package com.developers.EarnOn_Admin;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.developers.EarnOn_Admin.Adaptors.notificationAdaptor;
import com.developers.EarnOn_Admin.Models.notificationModel;
import com.developers.EarnOn_Admin.databinding.ActivityNotificationsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity {

    ActivityNotificationsBinding binding;
    RecyclerView recyclerView;
    ArrayList<notificationModel> notificationList = new ArrayList<>();
    notificationAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getNotifications();
        recyclerView = binding.NotificationListId;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void getNotifications() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Admin");
        ArrayList<notificationModel> notificationList = new ArrayList<>();
        reference.child("Notification").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()){
                    String Description = snap.child("Description").getValue(String.class);
                    String Image = snap.child("Image").getValue(String.class);
                    String Title = snap.child("Title").getValue(String.class);
                    String large_Image = snap.child("large_Image").getValue(String.class);
                    notificationList.add(new notificationModel(Description, Image, Title, large_Image, snap.getKey()));
                }

                adaptor = new notificationAdaptor(getApplicationContext(), notificationList);
                recyclerView.setAdapter(adaptor);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void backClick(View view) {
        finish();
    }


    public void notificationDetails(View view) {
        startActivity(new Intent(Notifications.this, notificationDetail.class));
    }
}