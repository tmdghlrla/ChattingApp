package com.tmddozla.chattingapp.model;

import java.io.Serializable;

public class Chat implements Serializable {
    public String message;
    public String createdAt;
    public String nickName;
    public int roomNumber;

    public Chat() {
    }

    public Chat(String message, String createdAt, String nickName, int roomNumber) {
        this.message = message;
        this.createdAt = createdAt;
        this.nickName = nickName;
        this.roomNumber = roomNumber;
    }
}
