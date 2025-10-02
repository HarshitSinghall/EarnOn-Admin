package com.developers.EarnOn_Admin;

import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.developers.EarnOn_Admin.databinding.ActivityNotificationDetailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class notificationDetail extends AppCompatActivity {

    ActivityNotificationDetailBinding binding;
    Button uploadImage, uploadNotification, largeImagebtn;
    EditText mTitle, mMsg;
    String title, msg, ImgUrl, ImgUrl2;
    ImageView imageView, imageView2;
    boolean imageSelected = false, imageSelected2 = false;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    private Uri FileUri;
    private Uri FileUri2;
    private Bitmap Imagebitmap;
    private Bitmap Imagebitmap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        uploadNotification = binding.save;
        mTitle = binding.TitleId;
        mMsg = binding.MessageId;
        imageView = binding.imageView;
        uploadImage = binding.selectbtn;
        largeImagebtn = binding.selectLarge;
        imageView2 = binding.Largeimg;

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
            }
        });

        largeImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
            }
        });

        uploadNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImg();
            }
        });

    }

    private void uploadImg() {
        if (imageSelected){
            mStorageReference.child("notification/" + UUID.randomUUID().toString()).putFile(FileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mStorageReference.child(taskSnapshot.getMetadata().getPath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ImgUrl = String.valueOf(uri);
                            uploadLargeImage();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(binding.getRoot(), e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                }
            });
        }else{
            ImgUrl = "null";
            uploadLargeImage();
        }
    }

    private void uploadLargeImage() {
        if (imageSelected2){
            mStorageReference.child("notification/" + UUID.randomUUID().toString()).putFile(FileUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mStorageReference.child(taskSnapshot.getMetadata().getPath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ImgUrl2 = String.valueOf(uri);
                            upload();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(binding.getRoot(), e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                }
            });
        }else{
            ImgUrl2 = "null";
            upload();
        }
    }

    private void upload() {
        title = mTitle.getText().toString().trim();
        msg = mMsg.getText().toString().trim();
        Map<String, String> data = new HashMap<>();
        data.put("Title",title);
        data.put("Description",msg);
        data.put("Image",ImgUrl);
        data.put("large_Image",ImgUrl2);
        mReference.child("Admin").child("Notification").push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(notificationDetail.this, "Created", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Snackbar.make(binding.getRoot(), task.getException().getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null) {
                try {
                    FileUri = data.getData();
                    InputStream imgStream = getContentResolver().openInputStream(data.getData());
                    Imagebitmap = BitmapFactory.decodeStream(imgStream);
                    imageView.setImageBitmap(Imagebitmap);
                    imageSelected = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode == 101) {
            if (data != null) {
                try {
                    FileUri2 = data.getData();
                    InputStream imgStream = getContentResolver().openInputStream(data.getData());
                    Imagebitmap2 = BitmapFactory.decodeStream(imgStream);
                    imageView2.setImageBitmap(Imagebitmap2);
                    imageSelected2 = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}