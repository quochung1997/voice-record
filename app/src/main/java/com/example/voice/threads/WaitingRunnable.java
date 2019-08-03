package com.example.voice.threads;

import com.example.voice.recordhelper.WavRecorder;

public class WaitingRunnable implements Runnable {
    String outputFile;

    public WaitingRunnable(String outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        WavRecorder recorder = new WavRecorder(outputFile);

        try {
            recorder.startRecording();
            Thread.currentThread();
            Thread.sleep(2000);
            recorder.stopRecording();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
