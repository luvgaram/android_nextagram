package com.nhnnext.nextagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class HomeViewAdapter extends CursorAdapter {
    private int layoutResourceId;
    private ArrayList<ArticleDTO> articleDTOData;
    private Context context;
    private Cursor cursor;
    private SharedPreferences pref;
    private LayoutInflater mLayoutInflater;

    public HomeViewAdapter(Context context, Cursor cursor, int layoutResourceId) {
        super(context, cursor, layoutResourceId);
        this.context = context;
        this.cursor = cursor;
        this.layoutResourceId = layoutResourceId;
        pref = context.getSharedPreferences(
                context.getString(R.string.pref_name), context.MODE_PRIVATE
        );
        mLayoutInflater = LayoutInflater.from(context);
    }

    static class ViewHolderItem {
        TextView tvTitle;
        TextView tvContents;
        ImageView imageView;
        String articleNumber;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View row = mLayoutInflater.inflate(layoutResourceId, parent, false);
        ViewHolderItem viewHolder = new ViewHolderItem();

        viewHolder.tvTitle = (TextView) row.findViewById(R.id.customlist_textview1);
        viewHolder.tvContents = (TextView) row.findViewById(R.id.customlist_textview2);
        viewHolder.imageView = (ImageView) row.findViewById(R.id.customlist_imageview);
        row.setTag(viewHolder);

        return row;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String title = cursor.getString(cursor.getColumnIndex(NextagramContract.Articles.TITLE));
        String contents = cursor.getString(cursor.getColumnIndex(NextagramContract.Articles.CONTENTS));
        String imageName = cursor.getString(cursor.getColumnIndex(NextagramContract.Articles.IMAGE_NAME));
        String articleNumber = cursor.getString(cursor.getColumnIndex(NextagramContract.Articles._ID));

        ViewHolderItem viewHolder = (ViewHolderItem) view.getTag();
        viewHolder.tvTitle.setText(title);
        viewHolder.tvContents.setText(contents);
        viewHolder.articleNumber = articleNumber;

        String imgPath = context.getFilesDir().getPath() + "/" + imageName;
        File img_load_path = new File(imgPath);
        if (img_load_path.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
            viewHolder.imageView.setImageBitmap(bitmap);
        }
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View row = convertView;
//
//        // 해당 row의 레이아웃 그려주기
//        if (row == null) {
//            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            row = inflater.inflate(layoutResourceId, parent, false);
//        }
//
//        // 해당 row의 텍스트들 넣어주기
//        TextView tvTitle = (TextView) row.findViewById(R.id.customlist_textview1);
//        TextView tvContent = (TextView) row.findViewById(R.id.customlist_textview2);
//        tvTitle.setText(articleData.get(position).getTitle());
//        tvContent.setText(articleData.get(position).getContent());
//
//        // 해당 row의 이미지 넣어주기
//        ImageView imageView = (ImageView) row.findViewById(R.id.customlist_imageview);
//        WeakReference<ImageView> imageViewReference = new WeakReference<ImageView>(imageView);
//
//        String imgPath = context.getFilesDir().getPath() + "/" + articleData.get(position).getImgName();
//
//        Bitmap bitmap = ImageLoader.getInstance().get(imgPath);
//        if (bitmap != null) {
//            Log.e("result", "getCache");
//            imageViewReference.get().setImageBitmap(bitmap);
//        } else {
//            Log.e("result", "putCache");
//            File imgLoadPath = new File(imgPath);
//
//            if (imgLoadPath.exists()) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 2;
//                options.inPurgeable = true;
//
//                bitmap = BitmapFactory.decodeFile(imgPath, options);
//                ImageLoader.getInstance().put(imgPath, bitmap);
//
//                // Bitmap resized = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true);
//                imageViewReference.get().setImageBitmap(bitmap);
//            } else {
//                Log.e("viewImage", "fail: file not found");
//            }
//        }
//
//        return row;
//    }
}
