package com.example.voice.threads;

import com.example.voice.models.VoiceRecord;
import com.example.voice.recordhelper.WavRecorder;
import com.example.voice.sqlites.VoiceRecordSqliteDao;

import java.util.ArrayList;

public class WavRecorderRunnable implements Runnable {
    WavRecorder wavRecorder;

    public WavRecorderRunnable(WavRecorder wavRecorder) {
        this.wavRecorder = wavRecorder;
    }

    @Override
    public void run() {

        try {
            wavRecorder.startRecording();

            Thread.currentThread();
            Thread.sleep(1000);

            wavRecorder.stopRecording();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}




