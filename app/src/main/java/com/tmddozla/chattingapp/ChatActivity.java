package com.tmddozla.chattingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.tmddozla.chattingapp.config.Config;
import com.tmddozla.chattingapp.model.Chat;

import java.time.LocalTime;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    EditText editText;
    Button btnSend;

    DatabaseReference mDatabase;
    ArrayList<Chat> chatArrayList = new ArrayList<>();

    RecyclerView recyclerView;
    ChatListAdapter adapter;
    String email = "";
    String nickName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editText = findViewById(R.id.editText);
        btnSend = findViewById(R.id.btnSend);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        email = sp.getString("email", "");
        nickName = sp.getString("nick" + email, "");

        int index = getIntent().getIntExtra("index", 0);
        Log.i("ttag", "index : " + index);

        mDatabase = FirebaseDatabase.getInstance().getReference("messageList").child("" + index);

        // ValueEventListener를 사용하여 데이터 변경 사항을 듣습니다.
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {                // 데이터베이스에 변경이 감지되면 호출됩니다.
                // dataSnapshot에는 현재 데이터베이스의 스냅샷이 포함됩니다.

                // 스냅샷에서 데이터를 가져와서 처리합니다.
                chatArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // "messageList" 하위의 각 데이터에서 필요한 값을 가져옵니다.
                    String message = snapshot.child("message").getValue(String.class);
                    String createdAt = snapshot.child("createdAt").getValue(String.class);
                    String nickname = snapshot.child("nickname").getValue(String.class);

                    if(nickname == null) {
                        nickname = snapshot.child("nickName").getValue(String.class);
                    }

                    Chat chat = new Chat(message, createdAt, nickname);
                    chatArrayList.add(chat);
                    Log.i("tag", "Message: " + message + ", Created At: " + createdAt + ", Nickname: " + nickname);
                }
                adapter = new ChatListAdapter(ChatActivity.this, chatArrayList, nickName);
                recyclerView.setAdapter(adapter);
                recyclerView.scrollToPosition(chatArrayList.size()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 데이터베이스에 오류가 발생하면 호출됩니다.
                Log.e("YourFragment", "Error: " + databaseError.getMessage());
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sentence = editText.getText().toString().trim();
                LocalTime currentTime = LocalTime.now();

                // 시간과 분 추출
                int hour = currentTime.getHour();
                int minute = currentTime.getMinute();
                Log.i("tag", "nickName : " + nickName);
                // 한 자리 수일 때는 앞에 0을 붙여서 두 자리로 표현
                String formattedTime = String.format("%02d:%02d", hour, minute);

                if (sentence.equals("")) {
                    return;
                }
                Chat chat = new Chat(sentence, formattedTime, nickName);
                mDatabase.push().setValue(chat);
            }
        });
    }
}