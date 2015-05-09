package com.nhnnext.nextagram;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;


public class HomeView extends Activity implements AdapterView.OnItemClickListener, OnClickListener {

//    private static AsyncHttpClient client = new AsyncHttpClient();
    private Button button1;
    private Button button2;
    private HomeController homeController;
//    private ArrayList<ArticleDTO> articleList;
//    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeController = new HomeController(getApplicationContext());
        homeController.initSharedPreferences();
        homeController.startSyncDataService();

//        pref = getSharedPreferences(getResources().getString(R.string.pref_name), MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//
//        editor.putString(getResources().getString(R.string.server_url), getResources().getString(R.string.server_url_value));
//        editor.commit();
//
//        Intent intentSync = new Intent("com.nhnnext.nextagram.SyncDataService");
//        startService(intentSync);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        setListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        homeController.refreshData();
    }

//    private void refreshData() {
//        client.get(getString(R.string.server_url_value) + "/loadData", new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                String jsonData = new String(bytes);
//                Log.i("getJSonData", "success: " + jsonData);
//                ProviderDao dao = new ProviderDao(getApplicationContext());
//                dao.insertJsonData(jsonData);
//                // HomeViewAdapter를 CursorAdapter로 변경
//                setListView(dao.getArticleList());
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                Log.i("getJSonData: ", "fail: " + throwable.getMessage());
//            }
//        });
//    }

    private void setListView() {
        ListView listView = (ListView) findViewById(R.id.customlist_listview);

        Cursor mCursor = getContentResolver().query(
                NextagramContract.Articles.CONTENT_URI,
                NextagramContract.Articles.PROJECTION_ALL, null, null,
                NextagramContract.Articles._ID + " asc"
        );

        HomeViewAdapter customAdapter = new HomeViewAdapter(this, mCursor, R.layout.custom_list_row);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.button1:
                Intent intentWrite = new Intent(".WritingArticleView");
                startActivity(intentWrite);
                break;
            case R.id.button2:
                homeController.refreshData();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(".ArticleView");
//        String articleNumber = articleList.get(position).getArticleNumber() + "";
//        intent.putExtra("ArticleNumber", articleNumber);
        intent.putExtra("ArticleNumber", ((HomeViewAdapter.ViewHolderItem)view.getTag()).articleNumber);
        Log.i("test", "ArticleNumber: " + ((HomeViewAdapter.ViewHolderItem)view.getTag()).articleNumber);
        startActivity(intent);
    }
}