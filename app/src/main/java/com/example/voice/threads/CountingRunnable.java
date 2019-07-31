package com.example.voice.threads;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class CountingRunnable implements Runnable {
    Context c;

    public CountingRunnable(Context c) {
        this.c = c;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Looper.prepare();
        Toast.makeText(c, "Recording ended", Toast.LENGTH_LONG).show();
    }
}
