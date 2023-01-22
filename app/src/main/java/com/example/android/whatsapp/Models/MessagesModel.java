package com.example.android.whatsapp.Models;

public class MessagesModel {
    String Uid,message,mesaageId;
    Long timestamp;

    public MessagesModel(String Uid, String message, Long timestamp) {
        this.Uid = Uid;
        this.message = message;
        this.timestamp = timestamp;
    }
    public MessagesModel(String Uid, String message) {
        this.Uid = Uid;
        this.message = message;
    }

    public MessagesModel(String mesaageId) {
        this.mesaageId = mesaageId;
    }

    public MessagesModel(){}

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMesaageId() {
        return mesaageId;
    }

    public void setMesaageId(String mesaageId) {
        this.mesaageId = mesaageId;
    }
}
