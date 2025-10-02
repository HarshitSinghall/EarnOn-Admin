package com.developers.EarnOn_Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.developers.EarnOn_Admin.Adaptors.QueryAdaptor;
import com.developers.EarnOn_Admin.Models.queryModel;
import com.developers.EarnOn_Admin.databinding.ActivityQueryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Query extends AppCompatActivity {

    ActivityQueryBinding binding;
    RecyclerView recyclerView;
    ArrayList<queryModel> helpList = new ArrayList<>();
    ArrayList<String> Codes = new ArrayList<>();
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    QueryAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQueryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDatabase = FirebaseDatabase.getInstance();

        recyclerView = binding.recycler;
        getCode();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void getCode() {
        mReference = mDatabase.getReference("users");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()){
                    Codes.add(snap.getKey());
                }
                getData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getData() {
        mReference = mDatabase.getReference("users");
        for (String code: Codes){
            mReference.child(code).child("query").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap: snapshot.getChildren()){
                        String img = snap.child("img").getValue(String.class);
                        String message = snap.child("message").getValue(String.class);
                        String rply = snap.child("rply").getValue(String.class);
                        String status = snap.child("status").getValue(String.class);

                        if (status.equalsIgnoreCase("open")){
                            helpList.add(new queryModel(message, rply, img, status, code, snap.getKey()));
                        }

                    }
                    adaptor = new QueryAdaptor(Query.this, helpList);
                    recyclerView.setAdapter(adaptor);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}