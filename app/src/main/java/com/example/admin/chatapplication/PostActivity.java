package com.example.admin.chatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity{

    private EditText mPostTitle;
    private EditText mPostDesc;
    private Spinner mPostChannel;
    private Button mSubmitBtn;

    private static final String TAG = "myLogs";

    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("post");

        mPostTitle = (EditText)findViewById(R.id.mPostTitle);
        mPostDesc = (EditText)findViewById(R.id.mPostDesc);
        mPostChannel = (Spinner)findViewById(R.id.mSp);
        mSubmitBtn = (Button)findViewById(R.id.mBtn);

        mProgress = new ProgressDialog(this);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPosting();
            }
        });
    }

    private void startPosting(){

        Log.d(TAG, "Нажата кнопка");

        mProgress.setMessage("Posting ...");
        mProgress.show();

        String title_val = mPostTitle.getText().toString().trim();
        String desc_val = mPostDesc.getText().toString().trim();
        String channel_val = mPostChannel.getSelectedItem().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val)&& !TextUtils.isEmpty(channel_val)){

            Log.d(TAG, "Проверка прошла успешно");

            DatabaseReference newPost = mDatabase.push();

            newPost.child("title").setValue(title_val);
            newPost.child("text").setValue(desc_val);
            newPost.child("channel").setValue(channel_val);

            mProgress.dismiss();

            startActivity(new Intent(PostActivity.this, MainActivity.class));

        }
    }
}
