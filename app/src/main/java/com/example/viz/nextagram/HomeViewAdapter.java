package com.example.viz.nextagram;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class HomeViewAdapter extends ArrayAdapter<ArticleDTO> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<ArticleDTO> articleData;

    public HomeViewAdapter(Context context, int layoutResourceId, ArrayList<ArticleDTO> articleData) {
        super(context, layoutResourceId, articleData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.articleData = articleData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        // 해당 row의 레이아웃 그려주기
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        // 해당 row의 텍스트들 넣어주기
        TextView tvTitle = (TextView) row.findViewById(R.id.customlist_textview1);
        TextView tvContent = (TextView) row.findViewById(R.id.customlist_textview2);
        tvTitle.setText(articleData.get(position).getTitle());
        tvContent.setText(articleData.get(position).getContent());

        // 해당 row의 이미지 넣어주기
        ImageView imageView = (ImageView) row.findViewById(R.id.customlist_imageview);
        WeakReference<ImageView> imageViewReference = new WeakReference<ImageView>(imageView);

        String imgPath = context.getFilesDir().getPath() + "/" + articleData.get(position).getImgName();

        Bitmap bitmap = ImageLoader.getInstance().get(imgPath);
        if (bitmap != null) {
            Log.e("result", "getCache");
            imageViewReference.get().setImageBitmap(bitmap);
        } else {
            Log.e("result", "putCache");
            File imgLoadPath = new File(imgPath);

            if (imgLoadPath.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                options.inPurgeable = true;

                bitmap = BitmapFactory.decodeFile(imgPath, options);
                ImageLoader.getInstance().put(imgPath, bitmap);

                // Bitmap resized = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true);
                imageViewReference.get().setImageBitmap(bitmap);
            } else {
                Log.e("viewImage", "fail: file not found");
            }
        }

        return row;
    }
}
