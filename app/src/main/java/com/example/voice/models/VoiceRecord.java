package com.example.voice.models;

import android.os.Environment;

public class VoiceRecord {
    public static String[] labels = {
            "cộng", "trừ", "nhân", "chia",
            "không", "một", "hai", "ba", "bốn", "năm", "lăm", "sáu", "bảy", "tám", "chín", "mười",
            "mươi", "mốt", "tư", "trăm", "nghìn", "triệu", "linh",
            "tất cả", "xóa", "bằng"
    };

    public static String[] labels_save = {
            "cong", "tru", "nhan", "chia",
            "khong", "mot(1)", "hai", "ba", "bon", "nam", "lam", "sau", "bay", "tam", "chin", "muoi(10)",
            "muoi", "mot", "tu", "tram", "nghin", "trieu", "linh",
            "tatca", "xoa", "bang"
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

    public String getLabelSave() {
        return labels_save[label];
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
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/voice_records/"+
                this.getLabelSave()+"_"+user.getId()+"_"+user.getGender()+"_"+
                String.format("%05d", number)+".wav";
    }
}
