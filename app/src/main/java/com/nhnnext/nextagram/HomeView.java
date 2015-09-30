package com.nhnnext.nextagram;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class HomeView extends Activity implements AdapterView.OnItemClickListener, OnClickListener {

    private HomeController homeController;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeController = new HomeController(getApplicationContext());
        homeController.initSharedPreferences();
        homeController.startSyncDataService();
        homeController.refreshData();

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
        registerBattery();

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(brBattery);
    }

    private void registerBattery() {
        // 배터리 상태 체크
        IntentFilter intentFilter = new IntentFilter("com.nhnnext.nextagram.BATTERY");

        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(brBattery, intentFilter);
    }

    BroadcastReceiver brBattery = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = getNetworkInfo();
            String action = intent.getAction();
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

            if (level <= 15 && action.equals(Intent.ACTION_POWER_DISCONNECTED) && status == 0) {
                Toast.makeText(context, "배터리가 부족하거나 인터넷 연결이 없어 업데이트를 중지합니다", Toast.LENGTH_SHORT).show();
                homeController.stopSyncDataService();
            } else {
                Toast.makeText(context, "데이터를 업데이트합니다", Toast.LENGTH_SHORT).show();
                homeController.startSyncDataService();
            }
        }
    };

    private int getNetworkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // 인터넷 연결이 없거나 모바일인 경우
        if(networkInfo == null || networkInfo.getType() == 0) return 0;
        // Wi-Fi
        else return 1;
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