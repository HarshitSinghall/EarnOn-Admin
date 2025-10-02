package com.developers.EarnOn_Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.developers.EarnOn_Admin.Adaptors.leadAdaptor;
import com.developers.EarnOn_Admin.Models.leadModel;
import com.developers.EarnOn_Admin.databinding.ActivityLeadsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Leads extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    ArrayList<leadModel> leadList = new ArrayList<>();
    ArrayList<String> CodeList = new ArrayList<>();
    leadAdaptor adaptor;
    ActivityLeadsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeadsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        recyclerView = binding.recyclerId;
        searchView = binding.searchViewId;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adaptor = new leadAdaptor(Leads.this, leadList);
        recyclerView.setAdapter(adaptor);
        
        getLeads();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adaptor.getFilter().filter(newText);
                return false;
            }
        });

    }

    private void getLeads() {
        mReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()){
                    CodeList.add(snap.getKey());
                }
                fetchIt();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void fetchIt() {
        for (String code: CodeList){
            mReference.child("users").child(code).child("leads").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap: snapshot.getChildren()){
                        String Number = snap.getKey();
                        String pName = snap.child("Pname").getValue(String.class);
                        String name = snap.child("name").getValue(String.class);
                        String status = snap.child("status").getValue(String.class);

                        leadList.add(new leadModel(Number, name, pName, status, code));

                    }

                    adaptor.updateData(leadList);
                    adaptor.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}