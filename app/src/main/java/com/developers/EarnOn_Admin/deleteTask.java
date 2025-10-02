package com.developers.EarnOn_Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developers.EarnOn_Admin.databinding.ActivityDeleteTaskBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class deleteTask extends AppCompatActivity {

    TextView mCategory;
    LinearLayout categoryDrop;
    ActivityDeleteTaskBinding binding;
    EditText mItem;
    String ItemCode, Category;
    Button deleteBtn;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mCategory = binding.CategoryId;
        categoryDrop = binding.categoryDrop;
        mItem = binding.ItemCideId;
        deleteBtn = binding.SelectImg;

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Admin");

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        categoryDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(deleteTask.this, mCategory);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.category, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mCategory.setText(item.getTitle());
                        return true;
                    }
                });
            }
        });

    }

    private void delete() {
        ItemCode = mItem.getText().toString().trim();
        Category = mCategory.getText().toString();

        mReference.child("tasks").child(Category).child(ItemCode).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                mReference.child("items").child(ItemCode).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        removeFromDB();
                    }
                });
            }
        });

    }

    private void removeFromDB() {
        String URL = "https://earnon-desk.000webhostapp.com/myPHP/deleteItem.php?code="+ItemCode;
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(deleteTask.this, response, Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }
}