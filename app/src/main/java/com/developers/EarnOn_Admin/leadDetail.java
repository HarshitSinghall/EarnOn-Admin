package com.developers.EarnOn_Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developers.EarnOn_Admin.databinding.ActivityLeadDetailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class leadDetail extends AppCompatActivity {

    TextView mName, mNumber, mDate, mAmount, mItem, mCode;
    EditText mstatus, mMsg;
    ImageView mProductImg;
    LinearLayout mSavebtn, mAddbtn;
    ActivityLeadDetailBinding binding;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    String Reg_no, Number, pCode, name, date, status, statusMsg, amount, pName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeadDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Reg_no = getIntent().getStringExtra("code");
        Number = getIntent().getStringExtra("number");

        mName = binding.NameId;
        mNumber = binding.MobileId;
        mDate = binding.dataId;
        mAmount = binding.amountId;
        mItem = binding.LeadId;
        mCode = binding.codeId;
        mstatus = binding.statusId;
        mMsg = binding.statusMsg;
        mSavebtn = binding.SaveId;
        mProductImg = binding.LogoId;
        mAddbtn = binding.addAmountId;

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("users");

        getData();

        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = mstatus.getText().toString().trim();
                statusMsg = mMsg.getText().toString().trim();
                saveData();
            }
        });

        mAddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAmount();
            }
        });

    }

    private void addAmount() {
        mReference = mDatabase.getReference("users");
        mReference.child(Reg_no).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String balance = snapshot.child("amount").getValue(String.class);
                String newBalance = String.valueOf(Float.parseFloat(balance.replace("Rs.","")) + Float.parseFloat(amount.replace("Rs.","")));
                Map<String, Object> data = new HashMap<>();
                data.put("amount",newBalance);
                mReference.child(Reg_no).child("profile").updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Snackbar.make(binding.getRoot(), "Amount added", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveData() {
        mReference = mDatabase.getReference("users");
        HashMap<String, String> data = new HashMap<>();
        data.put("name",name);
        data.put("Pcode",pCode);
        data.put("date",date);
        data.put("amount",amount);
        data.put("statusRply",statusMsg);
        data.put("status",status);
        data.put("Pname",pName);


        mReference.child(Reg_no).child("leads").child(Number).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(leadDetail.this, "updated", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(leadDetail.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getData() {
        mReference = mDatabase.getReference("users");
        mReference.child(Reg_no).child("leads").child(Number).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue(String.class);
                pCode = snapshot.child("Pcode").getValue(String.class);
                date = snapshot.child("date").getValue(String.class);
                amount = snapshot.child("amount").getValue(String.class);
                statusMsg = snapshot.child("statusRply").getValue(String.class);
                status = snapshot.child("status").getValue(String.class);
                pName = snapshot.child("Pname").getValue(String.class);

                mName.setText(name);
                mNumber.setText(pCode);
                mDate.setText(date);
                mAmount.setText(amount);
                mMsg.setText(statusMsg);
                mstatus.setText(status);
                mNumber.setText(Number);
                mCode.setText(Reg_no);
                mItem.setText(pCode);

                getProductImg(pCode);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProductImg(String pCode) {
        mReference = mDatabase.getReference("Admin");
        mReference.child("items").child(pCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url = snapshot.child("img").getValue(String.class);
                Picasso.get().load(url).fit().into(mProductImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}