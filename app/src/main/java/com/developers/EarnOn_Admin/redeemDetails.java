package com.developers.EarnOn_Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developers.EarnOn_Admin.databinding.ActivityRedeemDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class redeemDetails extends AppCompatActivity {

    ActivityRedeemDetailsBinding binding;
    TextView mAcc, mIfsc, mDate, mAmount;
    EditText mStatus, mMsg;
    String status, msg, reg_id, id, date, acc_no, ifsc, amount;
    LinearLayout updateBtn;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRedeemDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reg_id = getIntent().getStringExtra("code");
        id = getIntent().getStringExtra("id");

        mAcc = binding.AccountId;
        mIfsc = binding.IFSCId;
        mDate = binding.dataId;
        mStatus = binding.statusId;
        mMsg = binding.statusMsg;
        updateBtn = binding.UpdateId;
        mAmount = binding.AmountId;

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        fetchIt();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });



    }


    private void updateData() {
        status = mStatus.getText().toString().trim();
        msg = mMsg.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();
        data.put("status",status);
        data.put("msg",msg);

        mReference.child("users").child(reg_id).child("withdraw").child(id).updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Snackbar.make(binding.getRoot(), "UPDATED", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchIt() {
        mReference.child("users").child(reg_id).child("withdraw").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                date = snapshot.child("date").getValue(String.class);
                acc_no = snapshot.child("Acc_number").getValue(String.class);
                ifsc = snapshot.child("ifsc").getValue(String.class);
                amount = snapshot.child("amount").getValue(String.class);
                msg = snapshot.child("msg").getValue(String.class);
                status = snapshot.child("status").getValue(String.class);

                mDate.setText(date);
                mAcc.setText(acc_no);
                mIfsc.setText(ifsc);
                mMsg.setText(msg);
                mStatus.setText(status);
                mAmount.setText(amount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}