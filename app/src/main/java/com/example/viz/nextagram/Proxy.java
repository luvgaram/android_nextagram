package com.example.viz.nextagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Proxy {

    private String serverUrl;
    private SharedPreferences pref;
    private Context context;

    public Proxy(Context context) {
        this.context = context;
        String prefName = context.getResources().getString(R.string.pref_name);
        pref = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        serverUrl = pref.getString(
                context.getResources().getString(R.string.server_url), "");
    }

    public String getJSON() {

        try {
            String prefArticleNumberKey = context.getResources().getString(R.string.pref_article_number);
            String articleNumber = pref.getString(prefArticleNumberKey, "0");
            String serverUrl = this.serverUrl + "/loadData/?ArticleNumber=" + articleNumber;
            URL url = new URL (serverUrl);
//            URL url = new URL("http://127.0.0.1:5009/loadData");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);

            conn.connect();

            int status = conn.getResponseCode();
            Log.i("test","ProxyResponseCode: "+status);

            switch(status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("test", "NETWORK ERROR: " + e);
        }

        return null;
        //TODO: responsecode가 200대 아닐 경우, 200대지만 빈값이 온 경우 어떻게 처리할까?
    }
}