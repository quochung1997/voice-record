package com.example.voice.models;

import android.os.Environment;

public class VoiceRecord {
    public static String[] labels = {
            "cộng", "trừ", "nhân", "chia",
            "không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín", "mười",
            "mươi", "mốt", "tư", "trăm", "nghìn", "triệu",
            "tất cả", "xóa"
    };

    User user;
    int label;
    int number;

    public int getLabelIndex() {return label;}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLabel() {
        return labels[label];
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public VoiceRecord() {
    }

    public VoiceRecord(User user, int label, int number) {
        this.user = user;
        this.label = label;
        this.number = number;
    }

    public String getPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+
                this.getLabel()+"_"+user.getId()+"_"+user.getGender()+"_"+
                String.format("%05d", number)+".wav";
    }
}
