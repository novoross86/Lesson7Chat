package com.example.admin.chatapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {

    private Button btn_send_msg;
    private EditText input_msg;
    private TextView chat_converstion;
    private String user_name, room_name;
    private String temp_key;



    private DatabaseReference root;
    private DatabaseReference foo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        btn_send_msg = (Button)findViewById(R.id.SendChatMsg);
        input_msg = (EditText)findViewById(R.id.editTextMsg);
        chat_converstion = (TextView)findViewById(R.id.textView4);

        final String user_name = getIntent().getExtras().getString("user_name");
        final String chat_name = getIntent().getExtras().getString("chat_name");

        setTitle("Room - " + room_name);

        root = FirebaseDatabase.getInstance().getReference().child("Chat").child(chat_name);

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object>map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name",user_name);
                map2.put("msg",input_msg.getText().toString());

                message_root.updateChildren(map2);
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_converstion(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_converstion(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private  String chat_msg, chat_user_name;

    private void append_chat_converstion(DataSnapshot dataSnapshot){
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){
            chat_msg = (String)((DataSnapshot)i.next()).getValue();
            chat_user_name = (String)((DataSnapshot)i.next()).getValue();

            chat_converstion.append(chat_user_name+" : "+chat_msg+" \n");
        }
    }
}
