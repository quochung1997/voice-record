package com.example.voice.sqlites;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.voice.models.User;
import com.example.voice.models.VoiceRecord;

import java.util.ArrayList;

public class VoiceRecordSqliteDao extends SQLiteOpenHelper {

    public VoiceRecordSqliteDao(Context c) {
        super(c, "record", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS record(" +
                "user_id VARCHAR NOT NULL, " +
                "label_no INTEGER NOT NULL, " +
                "number INTEGER PRIMARY KEY AUTOINCREMENT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS record");
        onCreate(sqLiteDatabase);
    }

    public ArrayList<VoiceRecord> getAll() {
        ArrayList<VoiceRecord> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM record", null);

        while (c.moveToNext()) {
            VoiceRecord record = new VoiceRecord();
            User user = new User(c.getString(0), null);

            record.setUser(user);
            record.setLabel(c.getInt(1));
            record.setNumber(c.getInt(2));

            list.add(record);
        }

        return list;
    }

    public void drop() {
        getWritableDatabase().execSQL("DROP TABLE record");
    }

    public int insert(VoiceRecord record) {
        SQLiteDatabase db = getWritableDatabase();

        SQLiteStatement stm = db.compileStatement("INSERT INTO record VALUES(?, ?, NULL)");

        stm.bindString(1, record.getUser().getId());
        stm.bindLong(2, (long) record.getLabelIndex());
        //stm.bindLong(3, (long) record.getNumber());

        stm.executeInsert();

        Cursor c = db.rawQuery("select last_insert_rowid()", null);

        if (c.moveToNext()) return c.getInt(0);

        return -1;

    }

    public void update(VoiceRecord record) {
        SQLiteDatabase db = getWritableDatabase();

        SQLiteStatement stm = db.compileStatement("UPDATE record SET user_id=?, label=? WHERE number=?");


        stm.bindString(1, record.getUser().getId());
        stm.bindLong(2, (long) record.getLabelIndex());
        stm.bindLong(3, (long) record.getNumber());

        stm.executeUpdateDelete();
    }

    public void delete(int num) {
        SQLiteDatabase db = getWritableDatabase();

        SQLiteStatement stm = db.compileStatement("DELETE FROM record WHERE number="+num);

        stm.executeUpdateDelete();
    }

}
