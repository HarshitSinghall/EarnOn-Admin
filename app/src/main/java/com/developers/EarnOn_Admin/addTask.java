package com.developers.EarnOn_Admin;

import static com.developers.EarnOn_Admin.taskDetails.AnswerList;
import static com.developers.EarnOn_Admin.taskDetails.AudienceList;
import static com.developers.EarnOn_Admin.taskDetails.QuestinList;
import static com.developers.EarnOn_Admin.taskDetails.earnList;
import static com.developers.EarnOn_Admin.taskDetails.specificationList;
import static com.developers.EarnOn_Admin.taskDetails.termsList;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.developers.EarnOn_Admin.databinding.ActivityAddTaskBinding;
import com.google.android.material.snackbar.Snackbar;

public class addTask extends AppCompatActivity {

    ActivityAddTaskBinding binding;
    EditText mEditText, mAnswerText;
    Button addBtn;
    String Text, answer;
    String task;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mEditText = binding.TitleId;
        addBtn = binding.addBtn;
        mAnswerText = binding.answerId;
        relativeLayout = binding.relativeLayout2;

        task = getIntent().getStringExtra("task");
        if (task.equals("FAQ")){
            relativeLayout.setVisibility(View.VISIBLE);
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Text = mEditText.getText().toString().trim();
                switch (task) {
                    case "earn":
                        earnList.add(Text);
                        Snackbar.make(binding.getRoot(), "Added", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "specification":
                        specificationList.add(Text);
                        Snackbar.make(binding.getRoot(), "Added", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "terms":
                        termsList.add(Text);
                        Snackbar.make(binding.getRoot(), "Added", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "target":
                        AudienceList.add(Text);
                        Snackbar.make(binding.getRoot(), "Added", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "FAQ":
                        answer = mAnswerText.getText().toString().trim();
                        QuestinList.add(Text);
                        AnswerList.add(answer);
                        Snackbar.make(binding.getRoot(), "Added", Snackbar.LENGTH_SHORT).show();
                        break;
                }

            }
        });


    }

    public void DONE(View view) {
        finish();
    }
}