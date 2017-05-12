package com.jefferycalhoun.picassotestdeletethis.Database;

import android.provider.BaseColumns;

/**
 * Created by Jeff on 5/11/17.
 */

public class PhotoSchema {
    public static final String TABLE_NAME = "photos";
    public static class PhotoEntry implements BaseColumns {
        public static final String COLUMN_NAME_PHOTO_ID = "photo_id";
        public static final String COLUMN_NAME_OWNER = "owner";
        public static final String COLUMN_NAME_SECRET = "secret";
        public static final String COLUMN_NAME_SERVER = "server";
        public static final String COLUMN_NAME_FARM = "farm";
        public static final String COLUMN_NAME_TITLE = "title";
    }
}
