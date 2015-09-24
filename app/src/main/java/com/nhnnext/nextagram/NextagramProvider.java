package com.nhnnext.nextagram;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Locale;

// Created by eunjooim on 15. 5. 5..

public class NextagramProvider extends ContentProvider {

    private Context context;
    private SQLiteDatabase database;
    private final String TABLE_NAME = "Articles";

    private static final int ARTICLE_LIST = 1;
    private static final int ARTICLE_ID = 2;
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(NextagramContract.AUTHORITY, "Articles", ARTICLE_LIST);
        URI_MATCHER.addURI(NextagramContract.AUTHORITY, "Articles/#", ARTICLE_ID);
    }

    private void sqLiteInitialize() {
        database = context.openOrCreateDatabase("LocalDATA.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        database.setLocale(Locale.getDefault());
        database.setVersion(1);
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME
                + "(_id integer primary key autoincrement,"
                + "Title text not null,"
                + "Writer text not null,"
                + "Id text not null,"
                + "Content text not null,"
                + "WriteDate text not null,"
                + "ImgName text UNIQUE not null);";
        database.execSQL(sql);
    }

    private boolean isTableExist() {
        String searchTable = "select DISTINCT tbl_name from " +
                "sqLite_master where tbl_name = '" + TABLE_NAME + "';";
        Cursor cursor = database.rawQuery(searchTable, null);

        if (cursor.getCount() == 0) {
            return false;
        }

        cursor.close();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (URI_MATCHER.match(uri)) {
            case ARTICLE_LIST:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = NextagramContract.Articles.SORT_ORDER_DEFAULT;
                }
                break;
            case ARTICLE_ID:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = NextagramContract.Articles.SORT_ORDER_DEFAULT;
                }

                if (selection == null) {
                    selection = "_ID = ?";
                    selectionArgs = new String[] {uri.getLastPathSegment()};
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = database.query(TABLE_NAME, NextagramContract.Articles.PROJECTION_ALL, selection,
                                        selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // URI 유효성 검사
        if (URI_MATCHER.match(uri) != ARTICLE_LIST) {
            throw new IllegalArgumentException("[Insert]Insertion을 지원하지 않는 URI입니다: " + uri);
        }

        // ARTICLE_LIST라면
        else {
            // database에 insert 후 해당 ID를 리턴 받음
            long id = database.insert("Articles", null, values);

            // 리턴받은 ID로 ContentUris에 등록
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            // data 들어가면 component 자동으로 refresh
            getContext().getContentResolver().notifyChange(itemUri, null);

            return itemUri;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public boolean onCreate() {
        this.context = getContext();
        sqLiteInitialize();
        if (!isTableExist()) {
            createTable();
            return true;
        }
        return false;
    }
}
