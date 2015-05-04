package com.example.viz.nextagram;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by eunjooim on 15. 5. 4..
 */
public class SyncDataService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
