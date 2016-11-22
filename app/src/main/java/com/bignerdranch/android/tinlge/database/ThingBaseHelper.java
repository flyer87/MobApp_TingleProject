package com.bignerdranch.android.tinlge.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.bignerdranch.android.tinlge.database.ThingDbSchema.*;

/**
 * Created by Omer on 13.03.2016.
 */
public class ThingBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "thingsDB.db";

    public ThingBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ThingTable.NAME + "(" +
                ThingTable.Cols.ID + " integer primary key autoincrement, " +
                //ThingTable.Cols.UUID + ", "+
                ThingTable.Cols.WHAT + ", "+
                ThingTable.Cols.WHERE + ", " +
                ThingTable.Cols.IMPORTANT + "  INTEGER DEFAULT 0" +
                " )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nothing now
    }
}
