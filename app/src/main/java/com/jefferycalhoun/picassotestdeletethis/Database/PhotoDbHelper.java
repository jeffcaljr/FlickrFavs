package com.jefferycalhoun.picassotestdeletethis.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jeff on 5/11/17.
 */

public class PhotoDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FlickrFavorites.db";

    private static final String SQL_CREATE_PHOTOS =
            "CREATE TABLE " + PhotoSchema.TABLE_NAME + " (" +
                    PhotoSchema.PhotoEntry._ID + " INTEGER PRIMARY KEY," +
                    PhotoSchema.PhotoEntry.COLUMN_NAME_PHOTO_ID + " TEXT," +
                    PhotoSchema.PhotoEntry.COLUMN_NAME_FARM + " INTEGER," +
                    PhotoSchema.PhotoEntry.COLUMN_NAME_OWNER + " TEXT," +
                    PhotoSchema.PhotoEntry.COLUMN_NAME_SECRET + " TEXT," +
                    PhotoSchema.PhotoEntry.COLUMN_NAME_SERVER + " TEXT," +
                    PhotoSchema.PhotoEntry.COLUMN_NAME_TITLE + " TEXT)";

    private static final String SQL_DELETE_PHOTOS =
            "DROP TABLE IF EXISTS " + PhotoSchema.TABLE_NAME;

    public PhotoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_PHOTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_PHOTOS);
        onCreate(sqLiteDatabase);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
