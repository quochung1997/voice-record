package com.example.voice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.voice.R;
import com.example.voice.models.VoiceRecord;

import java.util.ArrayList;

public class RecordAdapter extends BaseAdapter {
    ArrayList<VoiceRecord> records;
    LayoutInflater inflater;

    public RecordAdapter(Context c, ArrayList<VoiceRecord> records) {
        this.records = records;
        this.inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int i) {
        return records.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.record_layout, null);

        TextView recordTitleTxt = v.findViewById(R.id.recordLayout_recordTitle);
        TextView recordPath = v.findViewById(R.id.recordLayout_recordPath);

        recordTitleTxt.setText(records.get(i).getNumber()+"");
        recordPath.setText(records.get(i).getPath());

        return v;
    }
}
