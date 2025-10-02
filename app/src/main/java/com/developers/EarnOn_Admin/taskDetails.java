package com.developers.EarnOn_Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developers.EarnOn_Admin.databinding.ActivityTaskDetailsBinding;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class taskDetails extends AppCompatActivity {

    EditText mTitle, mItem, mAmount, mConfirmTime, mDescription, mHighlight, mTrackingTime, mLink;
    String Title, Item, Amount, ConfirmTime, Description, Category, Highlight, TrackingTime, ImageLink, VideoLink, websiteLink;
    TextView mCategory;
    LinearLayout categoryDrop;
    ImageView mImageView, mLargeImageView;
    Button selectImage, selectVideo, FAQ, earn, specification, terms, targetaudience, create;
    ActivityTaskDetailsBinding binding;
    public static ArrayList<String> AudienceList, termsList, specificationList, earnList, QuestinList, AnswerList;
    FirebaseDatabase mDatabase;
    FirebaseStorage mStorage;
    StorageReference mStorageRef;
    DatabaseReference mReference;
    private Bitmap Imagebitmap;
    Uri ImageUri;
    Uri LargeImageUri;

    public static String ITEM_URL = "https://earnon-desk.000webhostapp.com/myPHP/saveItem.php?code=";
    private boolean LargeImageSelected = false;
    private boolean ImageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mTitle = binding.TitleId;
        mItem = binding.ItemCideId;
        mAmount = binding.AmountId;
        mConfirmTime = binding.ConfirmId;
        mDescription = binding.DescriptionId;
        mCategory = binding.CategoryId;
        mHighlight = binding.HighlightId;
        mTrackingTime = binding.TrackingId;
        categoryDrop = binding.categoryDrop;
        selectImage = binding.SelectImg;
        selectVideo = binding.selectVideo;
        mLargeImageView = binding.LargeImageView;
        FAQ = binding.FAQbtn;
        earn = binding.howTobtn;
        specification = binding.SpecificationBtn;
        terms = binding.Termbtn;
        targetaudience = binding.sudiencebtn;
        create = binding.Createbtn;
        mImageView = binding.imageView;
        mLink = binding.linkId;


        AudienceList = new ArrayList<>();
        termsList = new ArrayList<>();
        specificationList = new ArrayList<>();
        earnList = new ArrayList<>();
        QuestinList = new ArrayList<>();
        AnswerList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Admin");
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();

        Intent intent = new Intent(taskDetails.this, addTask.class);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
            }
        });

        selectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select large Video"), 101);
            }
        });

        FAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("task","FAQ");
                startActivity(intent);
            }
        });

        earn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("task","earn");
                startActivity(intent);
            }
        });

        specification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("task","specification");
                startActivity(intent);
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("task","terms");
                startActivity(intent);
            }
        });

        targetaudience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("task","target");
                startActivity(intent);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                websiteLink = mLink.getText().toString().trim();
                Title = mTitle.getText().toString().trim();
                TrackingTime = mTrackingTime.getText().toString().trim();
                Highlight = mHighlight.getText().toString().trim();
                Description = mDescription.getText().toString().trim();
                ConfirmTime = mConfirmTime.getText().toString().trim();
                Item = mItem.getText().toString().trim();
                Amount = mAmount.getText().toString().trim();
                Category = mCategory.getText().toString();

                videoLink();
            }
        });

        categoryDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(taskDetails.this, mCategory);
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

    private void save() {
        HashMap<String, String> faqMap = new HashMap<>();
        faqMap = faqFilter();

        HashMap<String, String> earnMap = new HashMap<>();
        earnMap = earnFilter();

        HashMap<String, String> SpecMap = new HashMap<>();
        SpecMap = specFilter();

        HashMap<String, String> targetMap = new HashMap<>();
        targetMap = targetFilter();

        HashMap<String, String> termMap = new HashMap<>();
        termMap = termFilter();

        mReference.child("tasks").child(Category).child(Item).child("FAQ").setValue(faqMap);
        mReference.child("tasks").child(Category).child(Item).child("how_to_earn").setValue(earnMap);
        mReference.child("tasks").child(Category).child(Item).child("specifiacation").setValue(SpecMap);
        mReference.child("tasks").child(Category).child(Item).child("target_audience").setValue(targetMap);
        mReference.child("tasks").child(Category).child(Item).child("term_&_condition").setValue(termMap);

        HashMap<String, Object> data = new HashMap<>();
        data.put("title",Title);
        data.put("amount",Amount);
        data.put("confirm_time",ConfirmTime);
        data.put("description",Description);
        data.put("small_des",Highlight);
        data.put("tracking_time",TrackingTime);
        data.put("small_image",ImageLink);
        data.put("video_link",VideoLink);
        if (Category.equals("mini")){
            data.put("app_link",websiteLink);
        }

        mReference.child("tasks").child(Category).child(Item).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Map<String, String> value = new HashMap<>();
                    value.put(Item, Category);
                    mReference.child("items").child(Item).setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Snackbar.make(binding.getRoot(), "Created", Snackbar.LENGTH_SHORT).show();
                            saveinDB();
                        }
                    });
                }else{
                    MainActivity.error = task.getException().getLocalizedMessage();
                    Snackbar.make(binding.getRoot(), task.getException().getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveinDB() {
        String encodedLink;
        try {
            encodedLink = URLEncoder.encode(websiteLink, "UTF-8");
        } catch (Exception e) {
            // Handle the error
            e.printStackTrace();
            encodedLink = websiteLink; // or handle it in another appropriate way
        }
        Toast.makeText(this, encodedLink, Toast.LENGTH_SHORT).show();
        ITEM_URL = ITEM_URL + Item + "&name=" + Title + "&link=" + encodedLink;
        StringRequest request = new StringRequest(ITEM_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Snackbar.make(binding.getRoot(), response, Snackbar.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainActivity.error = error.getMessage();
                Toast.makeText(taskDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    private HashMap<String, String> termFilter() {
        HashMap<String, String> map = new HashMap();
        for (int i=0; i < termsList.size(); i++){
            map.put(String.valueOf(i),termsList.get(i));
        }
        return map;
    }

    private HashMap<String, String> targetFilter() {
        HashMap<String, String> map = new HashMap();
        for (int i=0; i < AudienceList.size(); i++){
            map.put(String.valueOf(i),AudienceList.get(i));
        }
        return map;
    }

    private HashMap<String, String> specFilter() {
        HashMap<String, String> map = new HashMap();
        for (int i=0; i < specificationList.size(); i++){
            map.put(String.valueOf(i),specificationList.get(i));
        }
        return map;
    }

    private HashMap<String, String> earnFilter() {
        HashMap<String, String> map = new HashMap();
        for (int i=0; i < earnList.size(); i++){
            map.put(String.valueOf(i),earnList.get(i));
        }
        return map;
    }

    private HashMap<String, String> faqFilter() {
        HashMap<String, String> map = new HashMap();
        for (int i=0; i < QuestinList.size(); i++){
            map.put(QuestinList.get(i),AnswerList.get(i));
        }
        return map;
    }

    private void videoLink() {
        if (LargeImageSelected){

            mStorageRef.child("images/" + UUID.randomUUID().toString()).putFile(LargeImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mStorageRef.child(taskSnapshot.getMetadata().getPath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            VideoLink = String.valueOf(uri);
                            imageLink();
                            Snackbar.make(binding.getRoot(), " big Image inserted", Snackbar.LENGTH_SHORT).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    MainActivity.error = e.getLocalizedMessage();
                    Snackbar.make(binding.getRoot(), e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                }
            });
        }else{

            VideoLink = "null";
            imageLink();
        }
    }

    private void imageLink() {
        if (ImageSelected){

            mStorageRef.child("images/" + UUID.randomUUID().toString()).putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mStorageRef.child(taskSnapshot.getMetadata().getPath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ImageLink = String.valueOf(uri);
                            save();
                            Snackbar.make(binding.getRoot(), "Image inserted", Snackbar.LENGTH_SHORT).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    MainActivity.error = e.getLocalizedMessage();
                    Snackbar.make(binding.getRoot(), e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                }
            });
        }else{
            ImageLink = "null";
            save();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null) {
                try {
                    ImageUri = data.getData();
                    InputStream imgStream = getContentResolver().openInputStream(data.getData());
                    Imagebitmap = BitmapFactory.decodeStream(imgStream);
                    selectImage.setText("Image selected");
                    mImageView.setImageBitmap(Imagebitmap);
                    ImageSelected = true;
                } catch (FileNotFoundException e) {
                    MainActivity.error = e.getLocalizedMessage();
                    e.printStackTrace();
                }
            }
        }else if (requestCode == 101){

            try {
                LargeImageUri = data.getData();
                InputStream imgStream = getContentResolver().openInputStream(data.getData());
                Imagebitmap = BitmapFactory.decodeStream(imgStream);
                mLargeImageView.setImageBitmap(Imagebitmap);
                selectVideo.setText("Large Image selected");
                LargeImageSelected = true;
            } catch (FileNotFoundException e) {
                MainActivity.error = e.getLocalizedMessage();
                e.printStackTrace();
            }
        }
    }

}