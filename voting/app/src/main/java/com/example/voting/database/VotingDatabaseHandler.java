package com.example.voting.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class VotingDatabaseHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "VotingDB";
    private SQLiteDatabase db;

    // tables
    private String POLL_TABLE = "CREATE TABLE polls (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, desc TEXT)";

    private String VOTES_TABLE = "CREATE TABLE votes (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, pollId INTEGER , body TEXT NOT NULL, votes INTEGER, FOREIGN KEY (pollId) REFERENCES polls (id) )";

    public VotingDatabaseHandler(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(POLL_TABLE);
        sqLiteDatabase.execSQL(VOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS polls, votes");
        onCreate(sqLiteDatabase);
    }

    // create poll
    public void newPoll(String title, String desc, ArrayList<String> optionsP) {

        db = this.getWritableDatabase();

        ContentValues pollValue = new ContentValues();
        ContentValues optionsValue = new ContentValues();

        pollValue.put("title", title);
        pollValue.put("desc", desc);
        long pollId = db.insert("polls", null, pollValue);

        for (int i = 0; i < optionsP.size(); i++) {
            optionsValue.put("pollId", pollId);
            optionsValue.put("body", optionsP.get(i));
            optionsValue.put("votes", 0);
            db.insert("votes", null, optionsValue);
        }

        db.close();
    }

    public Cursor getPolls() {
        db = this.getReadableDatabase();

        final Cursor cursor = db.rawQuery("SELECT * FROM polls", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return cursor;
            }
        }
        return null;
    }

    public Cursor getPollById(String id) {
        db = this.getReadableDatabase();

        final Cursor cursor = db.rawQuery("SELECT * FROM polls WHERE polls.id = ?", new String[]{id});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return cursor;
            }
        }
        return null;
    }
    public Cursor getVotes(String pollId) {
        db = this.getReadableDatabase();

        final Cursor cursor = db.rawQuery("SELECT * FROM votes WHERE votes.pollId = ?", new String[]{pollId});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return cursor;
            }
        }
        return null;
    }

    public void closeDB(){
        this.db.close();
    }
    public Cursor searchPolls(String title) {
        db = this.getReadableDatabase();

        final Cursor cursor = db.rawQuery("SELECT * FROM polls WHERE title LIKE ?", new String []{"%"+title+"%"});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return cursor;
            }
        }
        return null;
    }


    public void addVote(int optionId) {
        db = this.getWritableDatabase();
        db.execSQL("UPDATE votes SET votes = votes + 1 where id = ?", new String[]{String.valueOf(optionId)});
        db.close();
    }

    public void deletePoll(int pollId) {
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM polls Where id = ?", new String[]{String.valueOf(pollId)});
        db.close();

    }


}
