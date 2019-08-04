package com.example.voice;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.voice.recordhelper.WavRecorder;

public class ReplaceRecordAsyn extends AsyncTask<Void, Integer, Void> {
    Activity contextParent;
    WavRecorder recorder;


    public ReplaceRecordAsyn(Activity contextParent, String outputFile) {
        this.contextParent = contextParent;
        this.recorder = new WavRecorder(outputFile);

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        recorder.startRecording();
        Button startBtn = contextParent.findViewById(R.id.replaceAct_startBtn);
        startBtn.setEnabled(false);

        SystemClock.sleep(300);
    }


    @Override
    protected Void doInBackground(Void... params) {

        for (int i = 0; i <= 10; i++) {
            SystemClock.sleep(100);
            publishProgress(i);
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        ProgressBar progressBar = contextParent.findViewById(R.id.replaceAct_progress);

        int number = values[0];

        progressBar.setProgress(number*10);

    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        recorder.stopRecording();

        SystemClock.sleep(300);
        Button startBtn = contextParent.findViewById(R.id.replaceAct_startBtn);
        ProgressBar progressBar = contextParent.findViewById(R.id.replaceAct_progress);

        startBtn.setEnabled(true);
        progressBar.setProgress(0);

        Toast.makeText(contextParent, "Saved", Toast.LENGTH_SHORT).show();
    }

}
