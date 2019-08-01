package com.example.voice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.voice.R;

import java.util.ArrayList;

public class LabelAdapter extends BaseAdapter {
    String[] labels, numbers;
    LayoutInflater inflater;

    public LabelAdapter(Context c, String[] labels, String[] numbers) {
        this.labels = labels;
        this.numbers = numbers;
        this.inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return labels.length;
    }

    @Override
    public Object getItem(int i) {
        return labels[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.label_layout, null);

        TextView labelNameTxt = v.findViewById(R.id.labelLayout_labelName);
        TextView recordsCountTxt = v.findViewById(R.id.labelLayout_recordsCount);

        labelNameTxt.setText(labels[i]);
        recordsCountTxt.setText(numbers[i]+" records");

        return v;
    }
}
