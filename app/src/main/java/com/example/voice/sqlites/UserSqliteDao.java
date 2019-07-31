package com.example.voice.sqlites;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.voice.models.User;

import java.util.ArrayList;

public class UserSqliteDao extends SQLiteOpenHelper {

    public UserSqliteDao(Context c) {
        super(c, "user", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS user (" +
                "id VARCHAR PRIMARY KEY NOT NULL, " +
                "gender VARCHAR NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS user");
        onCreate(sqLiteDatabase);
    }

    public void drop() {
        getWritableDatabase().execSQL("DROP TABLE user");
    }

    public User get(String id) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM user WHERE id='"+id+"'", null);

        if (c.moveToNext()) {
            User user = new User(c.getString(c.getColumnIndex("id")), c.getString(c.getColumnIndex("gender")));
            return user;
        }

        return null;
    }

    public ArrayList<User> getAll() {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM user", null);
        ArrayList<User> list = new ArrayList<>();

        while (c.moveToNext()) {
            User user = new User(c.getString(c.getColumnIndex("id")), c.getString(c.getColumnIndex("gender")));
            list.add(user);
        }

        return list;
    }

    public void insert(User user) {
        SQLiteDatabase db = getWritableDatabase();

        SQLiteStatement stm = db.compileStatement("INSERT INTO user VALUES(?, ?)");

        stm.bindString(1, user.getId());
        stm.bindString(2, user.getGender());

        stm.executeInsert();
    }

    public void delete(String id) {
        SQLiteDatabase db = getWritableDatabase();

        SQLiteStatement stm = db.compileStatement("DELETE FROM user WHERE id=?");

        stm.bindString(1, id);

        stm.executeUpdateDelete();
    }

    public void update(User user) {
        SQLiteDatabase db = getWritableDatabase();

        SQLiteStatement stm = db.compileStatement("UPDATE user SET gender=? WHERE id=?");

        stm.bindString(1, user.getId());
        stm.bindString(2, user.getGender());

        stm.executeUpdateDelete();
    }
}
