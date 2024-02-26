package com.tmddozla.chattingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tmddozla.chattingapp.adapter.ChatAdapter;
import com.tmddozla.chattingapp.config.Config;
import com.tmddozla.chattingapp.model.Chat;
import com.tmddozla.chattingapp.model.Room;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link secondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class secondFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public secondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment secondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static secondFragment newInstance(String param1, String param2) {
        secondFragment fragment = new secondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    FloatingActionButton btnAdd;
    DatabaseReference mDatabase;
    ArrayList<Chat> chatArrayList = new ArrayList<>();
    ArrayList<Room> roomArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    ChatAdapter adapter;
    String email = "";
    String nickName = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_second, container, false);

        btnAdd = rootView.findViewById(R.id.btnAdd);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
        email = sp.getString("email", "");
        nickName = sp.getString("nick" + email, "");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 데이터베이스에 변경이 감지되면 호출됩니다.
                // dataSnapshot에는 현재 데이터베이스의 스냅샷이 포함됩니다.
                // 스냅샷에서 데이터를 가져와서 처리합니다.
                int i = 0;
                int j = 0;
                chatArrayList.clear();
                roomArrayList.clear();
                String name = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i("tag", "node : " + snapshot.getKey()); // 2
                    if (snapshot.getKey().equals("chatRoomList")) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot childDataSnapshot : childSnapshot.getChildren()) {
                                // "messageList" 하위의 각 데이터에서 필요한 값을 가져옵니다.
                                if (name == "") {
                                    name = childDataSnapshot.getKey();
                                } else {
                                    name = name + ", " + childDataSnapshot.getKey();
                                    Log.i("tag", "nickName: " + name);
                                }
                                j++;
                            }
                            Room room = new Room(name, "" + j);
                            roomArrayList.add(room);
                            Log.i("tag", "roomSize : " + roomArrayList.size()); // 채팅방 개수
                            Log.i("tag", "j : " + j); // 인원수
                            Log.i("tag", "count : " + snapshot.getChildrenCount()); // 방 개수
                        }
                    }
                    // 채팅방 메세지
                    else {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot childDataSnapshot : childSnapshot.getChildren()) {
                                // "messageList" 하위의 각 데이터에서 필요한 값을 가져옵니다.
                                // "messageList" 하위의 각 데이터에서 필요한 값을 가져옵니다.
                                String message = childDataSnapshot.child("message").getValue(String.class);
                                String createdAt = childDataSnapshot.child("createdAt").getValue(String.class);
                                String nickname = childDataSnapshot.child("nickname").getValue(String.class);
                                if(nickname == null) {
                                    nickname = childDataSnapshot.child("nickName").getValue(String.class);
                                }

                                Chat chat = new Chat(message, createdAt, nickname, i);
                                chatArrayList.add(chat);
                                Log.i("tag", "Message: " + message + ", Created At: " + createdAt + ", Nickname: " + nickname);
                            }
                        }
                        i++;
                    }
                }
                adapter = new ChatAdapter(getActivity(), chatArrayList, roomArrayList, nickName, email);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 데이터베이스에 오류가 발생하면 호출됩니다.
                Log.e("YourFragment", "Error: " + databaseError.getMessage());
            }
        });
        // 플로팅 버튼 눌렀을 때 채팅방을 생성한다.
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size = "" + roomArrayList.size();
                Log.i("ttag", "size : " + size);
                mDatabase.child("chatRoomList").child(size).child(nickName).setValue(email);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("index", Integer.parseInt(size));
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}