package com.tmddozla.chattingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tmddozla.chattingapp.ChatActivity;
import com.tmddozla.chattingapp.R;
import com.tmddozla.chattingapp.model.Chat;
import com.tmddozla.chattingapp.model.Room;

import java.util.ArrayList;

// 2. RecyclerView.Adapter를 상속한다.
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    // 3. 멤버변수와 생성자를 만든다.
    Context context; // 엑티비티 정보
    ArrayList<Chat> chatArrayList;
    ArrayList<Room> roomArrayList;
    String nickName;
    String email;
    // Firebase 데이터베이스 초기화
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    // DatabaseReference 객체 설정
    DatabaseReference mDatabase = database.getReference();
    String name;

    public ChatAdapter(Context context, ArrayList<Chat> chatArrayList, ArrayList<Room> roomArrayList, String nickName, String email) {
        this.context = context;
        this.chatArrayList = chatArrayList;
        this.roomArrayList = roomArrayList;
        this.nickName = nickName;
        this.email = email;
    }

    // 4. 오버라이딩 함수를 구현한다.
    // onCreateViewHolder: 뷰 홀더를 생성하고 레이아웃을 연결하는 부분
    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_row, parent, false);
        return new ChatAdapter.ViewHolder(view);
    }

    // 화면 담당
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = roomArrayList.get(position);
        String nickName = room.nickName;
        String count = room.count;
        String lastMessage = "";

        holder.txtNick.setText(nickName);
        holder.txtCount.setText(count);

        for(int i = 0; i < chatArrayList.size(); i++) {
            if(position == chatArrayList.get(i).roomNumber) {
                lastMessage = chatArrayList.get(i).message;
            }
        }
        holder.txtMessage.setText(lastMessage);
    }

    @Override
    public int getItemCount() {
        return roomArrayList.size(); // 여기에 적힌 숫자만큼 recyclerView를 가져옴
    }

    // 1. 메모리에 있는 데이터를 연결할 ViewHolder를 만든다.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNick, txtCount, txtMessage;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNick = itemView.findViewById(R.id.txtNick);
            txtCount = itemView.findViewById(R.id.txtCount);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    name = txtNick.getText().toString().trim();
                    Log.i("tag", "name : " + name);

                    if(!name.contains(nickName)) {
                        mDatabase.child("chatRoomList").child("" + index).child(nickName).setValue(email);
                    }
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("index", index);
                    context.startActivity(intent);
                }
            });
        }
    }
}