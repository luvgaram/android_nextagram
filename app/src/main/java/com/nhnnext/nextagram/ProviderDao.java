package com.nhnnext.nextagram;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.net.URLDecoder;
import java.util.ArrayList;

class ProviderDao {
    private final Context context;

    public ProviderDao(Context context) {
        this.context = context;

        SQLiteDatabase database = context.openOrCreateDatabase("LocalDATA.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        try {
            String sql = "CREATE TABLE IF NOT EXISTS Articles (ID integer primary key autoincrement," +
                    "ArticleNumber integer UNIQUE not null," +
                    "Title text not null," +
                    "WriterName text not null," +
                    "WriterID text not null," +
                    "Content text not null," +
                    "WriteDate text not null," +
                    "ImgName text not null);";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.e("test", "CREATE TABLE FAILED! - " + e);
            e.printStackTrace();
        }

        // strict mode
        database.close();
    }

    @SuppressLint("CommitPrefEdits")
    public void insertData(ArrayList<ArticleDTO> articleList) {

        int articleNumber;
        String title;
        String writer;
        String id;
        String content;
        String writeDate;
        String imgName;

        FileDownloader fileDownloader = new FileDownloader(context);
        ArticleDTO articleDTO;

        for (int i = 0; i < articleList.size(); ++i) {

            if ((articleDTO = articleList.get(i)) == null) continue;

            articleNumber = articleDTO.getArticleNumber();
            title = articleDTO.getTitle();
            writer = articleDTO.getWriter();
            id = articleDTO.getId();
            content = articleDTO.getContent();
            writeDate = articleDTO.getWriteDate();
            imgName = articleDTO.getImgName();

            if (i == articleList.size() - 1) {

                String prefName = context.getResources().getString(R.string.pref_name);
                SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
                String prefArticleNumberKey = context.getResources().getString(R.string.pref_article_number);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(prefArticleNumberKey, articleNumber + "");
                editor.apply();
            }


            try {
                title = URLDecoder.decode(title, "UTF-8");
                writer = URLDecoder.decode(writer, "UTF-8");
                id = URLDecoder.decode(id, "UTF-8");
                content = URLDecoder.decode(content, "UTF-8");
                writeDate = URLDecoder.decode(writeDate, "UTF-8");
                imgName = URLDecoder.decode(imgName, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            ContentValues values = new ContentValues();
            values.put("_id", articleNumber);
            values.put("Title", title);
            values.put("Writer", writer);
            values.put("Id", id);
            values.put("Content", content);
            values.put("WriteDate", writeDate);
            values.put("ImgName", imgName);

            context.getContentResolver().insert(NextagramContract.Articles.CONTENT_URI, values);

            fileDownloader.downFile(context.getString(R.string.server_url_value) + "/image/" + imgName, imgName);
        }

    }

    public ArticleDTO getArticleByArticleNumber(int articleNumber) {
        ArticleDTO article = null;

        String title;
        String writer;
        String id;
        String content;
        String writeDate;
        String imgName;

        String where = "_id = " + articleNumber;

        Cursor cursor = context.getContentResolver().query(
                NextagramContract.Articles.CONTENT_URI,
                NextagramContract.Articles.PROJECTION_ALL, where, null,
                NextagramContract.Articles._ID + " ASC"
        );

//        String sql = "SELECT * FROM Articles WHERE ArticleNumber = " + articleNumber + ";";
//        Cursor cursor = database.rawQuery(sql, null);

        if (cursor != null) {
            cursor.moveToFirst();
            articleNumber = cursor.getInt(0);
            title = cursor.getString(1);
            writer = cursor.getString(2);
            id = cursor.getString(3);
            content = cursor.getString(4);
            writeDate = cursor.getString(5);
            imgName = cursor.getString(6);

            article = new ArticleDTO(articleNumber, title, writer, id, content, writeDate, imgName);
        }

        assert cursor != null;
        cursor.close();
        return article;
    }



//
//    /**
//     * JSON파싱을 위한 테스트 문자열입니다.
//     * 각 데이터는 다음과 같습니다.
//     * ArticleNumber - 글번호 중복X 숫자
//     * Title - 글제목 문자열
//     * Writer - 작성자
//     * Id - 작성자ID
//     * Content - 글내용
//     * WriteDate - 작성일
//     * ImgName - 사진명
//     */
//    public String getJsonTestData() {
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("");
//
//        sb.append("[");
//
//        sb.append("      {");
//        sb.append("         'ArticleNumber':'1',");
//        sb.append("         'Title':'오늘도 좋은 하루',");
//        sb.append("         'Writer':'학생1',");
//        sb.append("         'Id':'6613d02f3e2153283f23bf621145f877',");
//        sb.append("         'Content':'하지만 곧 기말고사지...',");
//        sb.append("         'WriteDate':'2013-09-23-10-10',");
//        sb.append("         'ImgName':'photo1.jpg'");
//        sb.append("      },");
//        sb.append("      {");
//        sb.append("         'ArticleNumber':'2',");
//        sb.append("         'Title':'대출 최고 3000만원',");
//        sb.append("         'Writer':'김미영 팀장',");
//        sb.append("         'Id':'6326d02f3e2153266f23bf621145f734',");
//        sb.append("         'Content':'김미영팀장입니다. 고갱님께서는 최저이율로 최고 3000만원까지 30분 이내 통장입금가능합니다.',");
//        sb.append("         'WriteDate':'2013-09-24-11-22',");
//        sb.append("         'ImgName':'photo2.jpg'");
//        sb.append("      },");
//        sb.append("      {");
//        sb.append("         'ArticleNumber':'3',");
//        sb.append("         'Title':'MAC등록신청',");
//        sb.append("         'Writer':'학생2',");
//        sb.append("         'Id':'8426d02f3e2153283246bf6211454262',");
//        sb.append("         'Content':'1a:2b:3c:4d:5e:6f',");
//        sb.append("         'WriteDate':'2013-09-25-12-33',");
//        sb.append("         'ImgName':'photo3.jpg'");
//        sb.append("      }");
//
//        sb.append("]");
//
//        return sb.toString();
//    }
}
