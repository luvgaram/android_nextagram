package com.example.viz.nextagram;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private static AsyncHttpClient client = new AsyncHttpClient();
    private Button button1;
    private Button button2;
    private ArrayList<ArticleDTO> articleList;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences(getResources().getString(R.string.pref_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(getResources().getString(R.string.server_url), getResources().getString(R.string.server_url_value));
        editor.commit();

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 서버에서 데이터를 가져와서 db에 넣는 부분
        refreshData();
        // db로부터 게시판 글을 가져와서 리스트에 넣는 부분
        listView(); // refreshData()에서 서버에서 json과 이미지 파일 가져오는 작업이 비동기기 때문에 이게 먼저 실행되기 때문에 처음 화면에 화면에 아무것도 안 보임
    }

    private void refreshData() {
        client.get(getString(R.string.server_url_value) + "/loadData", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String jsonData = new String(bytes);
                Log.i("getJSonData", "success: " + jsonData);
                Dao dao = new Dao(getApplicationContext());
                dao.insertJsonData(jsonData);
                listView();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i("getJSonData: ", "fail: " + throwable.getMessage());
            }
        });

    }

    private void listView() {
        Dao dao = new Dao(getApplicationContext());
        articleList = dao.getArticleList();
        HomeViewAdapter customAdapter = new HomeViewAdapter(this, R.layout.custom_list_row, articleList);
        ListView listView = (ListView) findViewById(R.id.customlist_listview);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.button1:
                Intent intentWrite = new Intent(".WritingArticleView");
//                Intent intentWrite = new Intent(arg0.getContext(), WritingArticleView.class);
                startActivity(intentWrite);
                break;
            case R.id.button2:
                refreshData();
                listView();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(".ArticleView");
        String articleNumber = articleList.get(position).getArticleNumber() + "";
        intent.putExtra("ArticleNumber", articleNumber);
        startActivity(intent);

//        Intent intentView = new Intent(view.getContext(), ArticleView.class);
//        String articleNumber = articleList.get(position).getArticleNumber() + "";
//        intentView.putExtra("ArticleNumber", articleNumber);
//        startActivity(intentView);
    }
}