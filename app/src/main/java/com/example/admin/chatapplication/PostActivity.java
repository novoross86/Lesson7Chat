package com.example.admin.chatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostActivity extends AppCompatActivity{

    private EditText mPostTitle;
    private EditText mPostDesc;
    private Spinner mPostChannel;
    private Button mSubmitBtn;


    private DatabaseReference mDatabase;
    private DatabaseReference chDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUer;
    private DatabaseReference mDatabaseUser;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth =FirebaseAuth.getInstance();

        mCurrentUer = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        chDatabase = FirebaseDatabase.getInstance().getReference().child("Chat");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mCurrentUer.getUid());

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

        mProgress.setMessage("Posting ...");
        mProgress.show();

        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val = mPostDesc.getText().toString().trim();
        final String channel_val = mPostChannel.getSelectedItem().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val)&& !TextUtils.isEmpty(channel_val)){

            final DatabaseReference newPost = mDatabase.push();



            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //создание ид в чатах
                    DatabaseReference newChat = chDatabase.push();
                    final String temp_key = newChat.getKey();
                    newChat.child("title").setValue(title_val);

                    final String user_name = dataSnapshot.child("name").getValue().toString();

                    newPost.child("title").setValue(title_val);
                    newPost.child("text").setValue(desc_val);
                    newPost.child("channel").setValue(channel_val);
                    newPost.child("uid").setValue(mCurrentUer.getUid());
                    newPost.child("username").setValue(dataSnapshot.child("name").getValue());
                    newPost.child("chat_id").setValue(temp_key);


                    mProgress.dismiss();

                    //startActivity(new Intent(PostActivity.this, MainActivity.class));

                    Intent chatRoomIntent = new Intent(PostActivity.this, ChatRoom.class);
                    chatRoomIntent.putExtra("chat_id", temp_key);
                    chatRoomIntent.putExtra("user_name", user_name);
                    startActivity(chatRoomIntent);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }
    }
}
