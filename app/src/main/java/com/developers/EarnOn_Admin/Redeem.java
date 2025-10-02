package com.developers.EarnOn_Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.developers.EarnOn_Admin.Adaptors.redeemAdaptor;
import com.developers.EarnOn_Admin.Models.redeemModel;
import com.developers.EarnOn_Admin.databinding.ActivityRedeemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Redeem extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    private ArrayList<redeemModel> redeemList = new ArrayList<>();
    private ArrayList<String> CodeList = new ArrayList<>();
    redeemAdaptor adaptor;
    RecyclerView recyclerView;
    ActivityRedeemBinding binding;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRedeemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        recyclerView = binding.recyclerId;
        searchView = binding.searchViewId;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Redeem.this));

        adaptor = new redeemAdaptor(redeemList, Redeem.this);
        recyclerView.setAdapter(adaptor);

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
        
        fetchWithdraw();


    }

    private void fetchWithdraw() {
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
            mReference.child("users").child(code).child("withdraw").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap: snapshot.getChildren()){
                        String Number = snap.child("Acc_number").getValue(String.class);
                        String msg = snap.child("msg").getValue(String.class);
                        String amount = snap.child("amount").getValue(String.class);
                        String status = snap.child("status").getValue(String.class);
                        String date = snap.child("date").getValue(String.class);

                        redeemList.add(new redeemModel(Number, date, msg, status, amount, snap.getKey(), code));
                        
                    }
                    adaptor.updateData(redeemList);
                    adaptor.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {



                }
            });
        }
    }

}