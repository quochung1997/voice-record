package com.example.voice;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PlayingAsync extends AsyncTask<Void, Integer, Void> {
    Activity parentContext;
    SoundPool soundPool;
    String path;
    int soundNumber;

    public PlayingAsync(Activity parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SystemClock.sleep(200);

        Button btn = parentContext.findViewById(R.id.playingAct_backBtn);
        btn.setEnabled(false);

    }

    @Override
    protected Void doInBackground(Void... voids) {

        for (int i = 0; i <= 10; i++) {
            SystemClock.sleep(100);
            publishProgress(i);
        }

        return null;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        ProgressBar progressBar = parentContext.findViewById(R.id.playingAct_progressBar);
        int number = values[0];
        progressBar.setProgress(number*10);

    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        SystemClock.sleep(200);

        Button btn = parentContext.findViewById(R.id.playingAct_backBtn);
        btn.setEnabled(true);

        Toast.makeText(parentContext, "Done", Toast.LENGTH_SHORT).show();

    }
}
