package com.nhnnext.nextagram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Created by eunjooim on 15. 5. 9..
 */
public class HomeController {
    private Context context;
    private SharedPreferences sharedPreferences;
//    private static AsyncHttpClient client;
    public SharedPreferences.Editor editor;
    private Proxy proxy;
    private ProviderDao dao;

    public HomeController(Context context) {
        this.context = context;
//        client = new AsyncHttpClient();
        proxy = new Proxy(context);
        dao = new ProviderDao(context);
    }

    public void initSharedPreferences() {
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.pref_name), context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.server_url), context.getResources().getString(R.string.server_url_value));
        editor.putString(context.getResources().getString(R.string.pref_article_number), 0 + "");
        editor.commit();
    }

    public void refreshData() {
        new Thread() {
            public void run() {
//                String jsonData = proxy.getJSON();
                ArrayList<ArticleDTO> articleList = proxy.getArticleDTO();
                dao.insertData(articleList);

            }
        }.start();
    }

    public void startSyncDataService() {
        Intent intentSync = new Intent("com.nhnnext.nextagram.SyncDataService");
        context.startService(intentSync);
    }
}
