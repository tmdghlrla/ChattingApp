package com.tmddozla.chattingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tmddozla.chattingapp.ChatActivity;
import com.tmddozla.chattingapp.R;
import com.tmddozla.chattingapp.model.Chat;
import com.tmddozla.chattingapp.model.Room;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    // 3. 멤버변수와 생성자를 만든다.
    Context context; // 엑티비티 정보
    ArrayList<Chat> chatArrayList;
    String nickName;
    final int TYPE_MY=0;
    final int TYPE_OTHER=1;

    public ChatListAdapter(Context context, ArrayList<Chat> chatArrayList, String nickName) {
        this.context = context;
        this.chatArrayList = chatArrayList;
        this.nickName = nickName;
    }

    @Override
    public int getItemViewType(int position) {
        if(chatArrayList.get(position).nickName.equals(nickName)) {
            //내가 쓴 글
            return TYPE_MY;
        } else {
            return TYPE_OTHER;
        }
    }

    // 4. 오버라이딩 함수를 구현한다.
    // onCreateViewHolder: 뷰 홀더를 생성하고 레이아웃을 연결하는 부분
    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == TYPE_MY) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat2_row, parent, false);
        } else if (viewType == TYPE_OTHER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_row, parent, false);
        }
        return new ChatListAdapter.ViewHolder(view);
    }

    // 화면 담당
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chatArrayList.get(position);
        holder.txtNick.setText(chat.nickName);
        holder.txtTime.setText(chat.createdAt);
        holder.txtMessage.setText(" " + chat.message + " ");
    }

    @Override
    public int getItemCount() {
        return chatArrayList.size(); // 여기에 적힌 숫자만큼 recyclerView를 가져옴
    }

    // 1. 메모리에 있는 데이터를 연결할 ViewHolder를 만든다.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNick, txtMessage, txtTime;
        CardView cardView;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNick = itemView.findViewById(R.id.txtNick);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
            cardView = itemView.findViewById(R.id.cardView);
            layout = itemView.findViewById(R.id.layout);
            
        }
    }
}