package com.nhnnext.nextagram;

import android.net.Uri;
import android.provider.BaseColumns;

// Created by eunjooim on 15. 5. 5.

// provider에 접근하기 위한 API
class NextagramContract {
    public static final String AUTHORITY = "com.nhnnext.nextagram";
    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Articles implements BaseColumns {
        public static final String _ID = "_id";
        public static final String TITLE = "Title";
        public static final String WRITER = "Writer";
        public static final String ID = "Id";
        public static final String CONTENTS = "Content";
        public static final String WRITE_DATE = "WriteDate";
        public static final String IMAGE_NAME = "ImgName";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(NextagramContract.CONTENT_URI, Articles.class.getSimpleName());

        public static final String[] PROJECTION_ALL = {
                _ID, TITLE, WRITER, ID, CONTENTS, WRITE_DATE, IMAGE_NAME
        };

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }
}
