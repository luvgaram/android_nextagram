package com.nhnnext.nextagram;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


public class HomeView extends Activity implements AdapterView.OnItemClickListener, OnClickListener {

    private HomeController homeController;
    private Cursor mCursor;
    private Thread contThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // strict mode 적용
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .permitNetwork()
                .permitDiskReads()
                .permitDiskWrites()
                .penaltyLog()
                .penaltyDropBox()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contThread = new Thread(new Runnable() {
            @Override
            public void run() {

                homeController = new HomeController(getApplicationContext());
                homeController.initSharedPreferences();
                homeController.startSyncDataService();
                homeController.refreshData();
            }
        });

        contThread.start();

//        homeController = new HomeController(getApplicationContext());
//        homeController.initSharedPreferences();
//        homeController.startSyncDataService();

        Button button1 = (Button) findViewById(R.id.btn_write);
        Button button2 = (Button) findViewById(R.id.btn_refresh);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        setListView();
    }

    @Override
    protected void onResume() {
        super.onResume();


//        homeController.refreshData();
    }

    private void setListView() {
        ListView listView = (ListView) findViewById(R.id.customlist_listview);

        mCursor = getContentResolver().query(
                NextagramContract.Articles.CONTENT_URI,
                NextagramContract.Articles.PROJECTION_ALL, null, null,
                NextagramContract.Articles._ID + " asc"
        );

        HomeViewAdapter customAdapter = new HomeViewAdapter(this, mCursor);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_write:
                Intent intentWrite = new Intent(".WritingArticleView");
                startActivity(intentWrite);
                break;
            case R.id.btn_refresh:
                homeController.refreshData();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(".ArticleView");

        intent.putExtra("ArticleNumber", ((HomeViewAdapter.ViewHolderItem)view.getTag()).articleNumber);
        Log.i("test", "ArticleNumber: " + ((HomeViewAdapter.ViewHolderItem) view.getTag()).articleNumber);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCursor.close();
    }

}