package com.developers.EarnOn_Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developers.EarnOn_Admin.databinding.ActivityQueryDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class queryDetails extends AppCompatActivity {

    ActivityQueryDetailsBinding binding;
    String Reg_no, id, message, Rply, imgLink, Status;
    EditText mStatus, mReply;
    ImageView imageView;
    TextView mMessage;
    LinearLayout saveBtn;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQueryDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mStatus = binding.statusId;
        mReply = binding.DescriptionId;
        mMessage = binding.title;
        saveBtn = binding.SaveId;
        imageView = binding.imageView;

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("users");
        Reg_no = getIntent().getStringExtra("reg_no");
        id = getIntent().getStringExtra("id");

        getData();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }
        });

    }

    private void uploadData() {
        Rply = mReply.getText().toString().trim();
        Status = mStatus.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();
        data.put("rply", Rply);
        data.put("status", Status);

        mReference.child(Reg_no).child("query").child(id).updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
            }
        });
        //TODO: VIDEO LINK
        //TODO: ITEM IN FIREBASE

    }

    private void getData() {
        mReference.child(Reg_no).child("query").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                message = snapshot.child("message").getValue(String.class);
                Rply = snapshot.child("rply").getValue(String.class);
                imgLink = snapshot.child("img").getValue(String.class);

                mMessage.setText(message);
                mReply.setText(Rply);
                if (!imgLink.contains("null")) {
                    Picasso.get().load(imgLink).into(imageView);
                } else {
                    imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}