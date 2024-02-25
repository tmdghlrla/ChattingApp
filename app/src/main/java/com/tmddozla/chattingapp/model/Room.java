package com.tmddozla.chattingapp.model;

public class Room {
    public String nickName;
    public String count;
    public String LastMessage;
    public String roomNumber;

    public Room() {
    }

    public Room(String nickName, String count) {
        this.nickName = nickName;
        this.count = count;
    }

    public Room(String nickName, String count, String lastMessage) {
        this.nickName = nickName;
        this.count = count;
        LastMessage = lastMessage;
    }
}
