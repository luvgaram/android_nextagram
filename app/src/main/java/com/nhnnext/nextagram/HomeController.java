package com.nhnnext.nextagram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

// Created by eunjooim on 15. 5. 9.

class HomeController {
    private final Context context;
    private final Proxy proxy;
    private final ProviderDao dao;
    private Intent intentSync;

    public HomeController(Context context) {
        this.context = context;
//        client = new AsyncHttpClient();
        proxy = new Proxy(context);
        dao = new ProviderDao(context);
    }

    public void initSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.pref_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.server_url), context.getResources().getString(R.string.server_url_value));
        editor.putString(context.getResources().getString(R.string.pref_article_number), 0 + "");
        editor.apply();
    }

    public void refreshData() {
        new Thread() {
            public void run() {
                ArrayList<ArticleDTO> articleList = proxy.getArticleDTO();
                dao.insertData(articleList);
            }
        }.start();
    }

    public void startSyncDataService() {
        intentSync = new Intent("com.nhnnext.nextagram.SyncDataService");
        context.startService(intentSync);
    }

    public void stopSyncDataService() {
        Log.i("stopservice", "stopservice");
        context.stopService(intentSync);
    }
}
