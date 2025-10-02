package com.developers.EarnOn_Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developers.EarnOn_Admin.Models.referalModel;
import com.developers.EarnOn_Admin.databinding.ActivityRefferalBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import android.widget.Filter;
import android.widget.Filterable;


public class refferalActivity extends AppCompatActivity {


    ActivityRefferalBinding binding;
    ArrayList<referalModel> referalList = new ArrayList<>();
    RecyclerView recyclerView;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    String upperName = "";
    refferalAdaptor adaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRefferalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        recyclerView = binding.recyclerView;


        adaptor = new refferalAdaptor(referalList, this, (position, currentAmount) -> {
            showDialogToUpdateAmount(position, currentAmount);
        });
        recyclerView.setAdapter(adaptor);


        mDatabase = FirebaseDatabase.getInstance();
        try {
            getRefferals();
        }catch (Exception e){
            e.printStackTrace();
        }

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void getRefferals() {
        mReference = mDatabase.getReference("users");

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()){

                    String REG_NO = snap.getKey().toString();
                    upperName = "";

                    mReference.child(REG_NO).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("harshit", "onDataChange: "+snapshot.toString());
                            snapshot.child("name").getValue().toString();
                            try{
                                upperName = snapshot.child("name").getValue().toString();
                            }catch (Exception e){

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            upperName = "Error";
                        }
                    });

                    mReference.child(REG_NO).child("refferals").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap: snapshot.getChildren()){
                                String code = snap.getKey().toString();

                                mReference.child(code).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String refered_by = snapshot.child("refer_by").getValue().toString();

                                        mReference.child(refered_by).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String amount = snapshot.child("amount").getValue().toString();
                                                referalList.add(new referalModel(refered_by, amount, "", code));
//                                        referalList.add(new referalModel(code, amount, upperName, code));

                                                adaptor.updateData(referalList);
                                                adaptor.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });



                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(refferalActivity.this, "Error + "+error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });



                            }

                            adaptor.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adaptor.notifyDataSetChanged();

    }


    private void showDialogToUpdateAmount(int position, String currentAmount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_update_amount, null, false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextAmount);
        input.setText(currentAmount);

        builder.setView(viewInflated)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String newAmount = input.getText().toString();
                    // Update the amount in your list and notify the adapter
                    referalList.get(position).setAmount(newAmount);
                    Toast.makeText(this, referalList.get(position).getrefered_by(), Toast.LENGTH_SHORT).show();
                    mReference.child(referalList.get(position).getrefered_by()).child("profile").child("amount").setValue(newAmount).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(refferalActivity.this, "success", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(refferalActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    adaptor.notifyDataSetChanged();
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }


    public void backClick(View view) {
        finish();
    }

}