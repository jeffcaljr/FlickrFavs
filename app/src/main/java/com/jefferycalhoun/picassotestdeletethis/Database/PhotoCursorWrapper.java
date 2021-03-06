package com.jefferycalhoun.picassotestdeletethis.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.jefferycalhoun.picassotestdeletethis.Models.Photo;

/**
 * Created by Jeff on 5/11/17.
 */

public class PhotoCursorWrapper extends CursorWrapper {

    public PhotoCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Photo getPhoto(){
        String photoID = getString(getColumnIndex(PhotoSchema.PhotoEntry.COLUMN_NAME_PHOTO_ID));
        String owner = getString(getColumnIndex(PhotoSchema.PhotoEntry.COLUMN_NAME_OWNER));
        String secret = getString(getColumnIndex(PhotoSchema.PhotoEntry.COLUMN_NAME_SECRET));
        String server = getString(getColumnIndex(PhotoSchema.PhotoEntry.COLUMN_NAME_SERVER));
        String title = getString(getColumnIndex(PhotoSchema.PhotoEntry.COLUMN_NAME_TITLE));
        int farm = getInt(getColumnIndex(PhotoSchema.PhotoEntry.COLUMN_NAME_FARM));

        Photo photo = new Photo(photoID, owner, secret, server, title, farm);
        return photo;

    }
}
