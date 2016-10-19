package com.example.admin.chatapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {

    private Button btn_send_msg;
    private EditText input_msg;
    private String temp_key;
    private RecyclerView mRecyclerView;
    private DatabaseReference root;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<Massege, MassegeViewHolder> mFirebaseAdapter;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        btn_send_msg = (Button)findViewById(R.id.SendChatMsg);
        input_msg = (EditText)findViewById(R.id.editTextMsg);

        mRecyclerView = (RecyclerView)findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();

        final String user_name = getIntent().getExtras().getString("user_name");
        final String chat_name = getIntent().getExtras().getString("chat_name");

        //инициализация linetLayoutManeger
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        setTitle("Room - " + chat_name);

        root = FirebaseDatabase.getInstance().getReference().child("Chat").child(chat_name);

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Massege, MassegeViewHolder>(
                Massege.class,
                R.layout.massege_row,
                MassegeViewHolder.class,
                root
        ) {
            @Override
            protected void populateViewHolder(MassegeViewHolder viewHolder, Massege model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setMsg(model.getMsg());
            }
        };

        //перемещение на последнюю позицию
        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

                if(lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))){
                    mRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mFirebaseAdapter);

        //отправка сообщения
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

                input_msg.setText("");

                //помещаем добавленное сообщение на экран
                int s = mFirebaseAdapter.getItemCount();
                int d = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if(s != d){
                    mRecyclerView.scrollToPosition(s);
                }
            }
        });

    }


    public static class MassegeViewHolder extends RecyclerView.ViewHolder{

        View nView;

        public  MassegeViewHolder(View itemView){
            super(itemView);
            nView = itemView;
        }

        public void setName(String name){
            TextView massege_name = (TextView)nView.findViewById(R.id.massege_name);
            massege_name.setText(name);
        }

        public void setMsg(String msg){
            TextView massege_msg = (TextView)nView.findViewById(R.id.massege_text);
            massege_msg.setText(msg);
        }
    }


}