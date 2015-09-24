package com.nhnnext.nextagram;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;

import java.io.File;

class FileDownloader {
    private final Context context;
    private static final SyncHttpClient client = new SyncHttpClient();

    public FileDownloader(Context context) {
        this.context = context;
    }

    public void downFile(String fileUrl, String fileName) {
        // 서버에 올린 다음 internal storage (/data/data/appname/files/)로 사진을 내려받고 있음
        // 실제로 이미지를 불러 올 때는 여기서 읽어드린다 - 그래서 db에 이미지 이름만 있어도 불러옴 - 지정된 위치
        final File filePath = new File(context.getFilesDir().getPath() + "/" + fileName);

        Log.i("isFileExists", filePath.exists() + " " + filePath.getAbsolutePath());

        if (!filePath.exists()) {
            client.get(fileUrl, new FileAsyncHttpResponseHandler(context) {
                @Override
                public void onFailure(int i, Header[] headers, Throwable throwable, File file) {
                    Log.i("FileDownloader", "fail: ");
                }

                @Override
                public void onSuccess(int i, Header[] headers, File file) {
                    Log.i("FileDownloader", "success responsePath: " + file.getAbsolutePath());
                    Log.i("FileDownloader", "success originalPath: " + filePath.getAbsolutePath());
                    boolean result = file.renameTo(filePath);
                    Log.i("FileDownloader", "renaming result: " + result);
                }
            });
        }
    }
}
