package com.tmddozla.chattingapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tmddozla.chattingapp.adapter.ChatAdapter;
import com.tmddozla.chattingapp.adapter.ChatListAdapter;
import com.tmddozla.chattingapp.model.Chat;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    EditText editText;
    Button btnSend;

    DatabaseReference mDatabase;
    ArrayList<Chat> chatArrayList = new ArrayList<>();

    RecyclerView recyclerView;
    ChatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editText = findViewById(R.id.editText);
        btnSend = findViewById(R.id.btnSend);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        int index = getIntent().getIntExtra("index", 0);

        mDatabase = FirebaseDatabase.getInstance().getReference("messageList").child("" + index);

        // ValueEventListener를 사용하여 데이터 변경 사항을 듣습니다.
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {                // 데이터베이스에 변경이 감지되면 호출됩니다.
                // dataSnapshot에는 현재 데이터베이스의 스냅샷이 포함됩니다.

                // 스냅샷에서 데이터를 가져와서 처리합니다.
                int i = 0;
                chatArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 각 자식 노드에 대해 데이터를 가져와 처리합니다.
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        // "messageList" 하위의 각 데이터에서 필요한 값을 가져옵니다.
                        String message = childSnapshot.child("message").getValue(String.class);
                        String createdAt = childSnapshot.child("createdAt").getValue(String.class);
                        String nickname = childSnapshot.child("nickname").getValue(String.class);

                        Chat chat = new Chat(message, createdAt, nickname, i);
                        chatArrayList.add(chat);
                        Log.i("tag", "Message: " + message + ", Created At: " + createdAt + ", Nickname: " + nickname);
                    }
                    i++;
                }
                adapter = new ChatListAdapter(ChatActivity.this, chatArrayList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 데이터베이스에 오류가 발생하면 호출됩니다.
                Log.e("YourFragment", "Error: " + databaseError.getMessage());
            }
        });
    }
}