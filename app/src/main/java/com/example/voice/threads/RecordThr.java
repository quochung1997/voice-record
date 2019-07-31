package com.example.voice.threads;

import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;

import com.example.voice.models.VoiceRecord;
import com.example.voice.sqlites.VoiceRecordSqliteDao;

import java.io.IOException;
import java.util.ArrayList;

public class RecordThr implements Runnable {

    private MediaRecorder myAudioRecorder;
    private String outputFile;

    private String userId;
    private String gender;
    private int recordId;

    private Button startBtn;
    private TextView statusTxt;

    private VoiceRecordSqliteDao recordDao;
    private ArrayList<VoiceRecord> records;

    public RecordThr(String userId, String gender, int recordId, Button startBtn, TextView statusTxt,
                     VoiceRecordSqliteDao recordDao, ArrayList<VoiceRecord> records) {
        this.userId = userId;
        this.gender = gender;
        this.recordId = recordId;
        this.startBtn = startBtn;
        this.statusTxt = statusTxt;
        this.recordDao = recordDao;
        this.records = records;
    }

    @Override
    public void run() {

        boolean isSuccess = false;

        //startBtn.setEnabled(false);

        try {
            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+
                    userId+"_"+gender+"_"+String.format("%05d", recordId)+".3gp";

            //Toast.makeText(getApplicationContext(), outputFile, Toast.LENGTH_LONG).show();

            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputFile);

            myAudioRecorder.prepare();
            myAudioRecorder.start();

            Thread.sleep(10000);

            myAudioRecorder.stop();
            myAudioRecorder.release();

            isSuccess = true;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!isSuccess) {
            records.remove(records.size()-1);
            recordDao.delete(recordId);
        }

        //startBtn.setEnabled(true);

    }
}
