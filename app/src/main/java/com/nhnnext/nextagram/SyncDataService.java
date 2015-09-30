package com.nhnnext.nextagram;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.util.Log;

// Created by eunjooim on 15. 5. 4.

public class SyncDataService extends Service {
    private static final String TAG = SyncDataService.class.getSimpleName();
    private Proxy proxy; // 서버에서 Article들을 받아옴
    private ProviderDao dao; // 받아온 Article들을 DB에 저장
    private TimerTask mTask;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        proxy = new Proxy(getApplicationContext());
        dao = new ProviderDao(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 새 TimerTask를 생성하고 Run 메서드를 override
        mTask = new TimerTask() {
            @Override
            public void run() {
                ArrayList<ArticleDTO> articleList = proxy.getArticleDTO();
                dao.insertData(articleList);
            }
        };

        // 새 Timer 생성해서 TimerTask, 시작 시점, 주기 설정
        Timer mTimer = new Timer();
        mTimer.schedule(mTask, 1000 * 5, 1000 * 5);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        mTask.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
