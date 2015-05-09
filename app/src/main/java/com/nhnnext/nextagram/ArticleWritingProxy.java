package com.nhnnext.nextagram;

import android.content.SharedPreferences;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

public class ArticleWritingProxy {

    private SharedPreferences pref;
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void uploadArticle(ArticleDTO article, String filePath, AsyncHttpResponseHandler responseHandler) {

        RequestParams params = new RequestParams();
        params.put("title", article.getTitle());
        params.put("writer", article.getWriter());
        params.put("id", article.getId());
        params.put("content", article.getContent());
        params.put("writeDate", article.getWriteDate());
        params.put("imgName", article.getImgName());

        try {
            params.put("uploadedfile", new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        client.post("http://54.64.250.239:5009/upload", params, responseHandler);
    }
}
