package com.example.administrator.data_sdk.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/3/15.
 * <p/>
 * 这个类用于图片的转换
 */
public class ImageTransformation {

    /**
     * 这个是drawable转换bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap Drawable2Bitmap(Drawable drawable) {
        //因为BitmapDrawable是drawable的子类
        return ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * 这个是bitmap转Drawable
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Drawable Bitmap2Drawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * 这个是Bitmap转Drawable
     *
     * @param bitmap
     * @return
     */
    @Deprecated
    public static Drawable Bitmap2Drawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    /**
     * 这个是官方推荐的方法
     *
     * @param context
     * @param reseouce
     * @return
     */
    public static Drawable Resouce2Drawable(Context context, int reseouce) {
        return ContextCompat.getDrawable(context, reseouce);
        //getResouce.getDrawable()已经过期;
    }

    /**
     * 这个是资源图片转Bitmap
     * <p/>
     * 这种方法不适合加载大量的图片和比较大的图片因为消耗大量的内存
     *
     * @param context
     * @param reseouce
     * @return
     */
    @Deprecated
    public static Bitmap Resouce2Bitmap(Context context, int reseouce) {
        return BitmapFactory.decodeResource(context.getResources(), reseouce);
    }

    /**
     * 字节流就是图片的二进制流转Drawable
     *
     * @param context
     * @param inputStream
     * @return
     */
    public static Drawable InputStream2Drawable(Context context, InputStream inputStream) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        return new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(inputStream, null, options));
    }

    /**
     * 字节流就是图片的二进制流转Bitmap
     *
     * @param inputStream
     * @return
     */
    @Deprecated
    public static Bitmap InputStream2Bitmap(InputStream inputStream) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    /**
     * 路径转化成图片
     *
     * @param context
     * @param url
     * @return
     */
    public static Drawable URL2Drawable(Context context, String url) {
        String path = url; //获得SDCard目录
        Bitmap bmpDefaultPic = BitmapFactory.decodeFile(path, null);
        return Bitmap2Drawable(context, bmpDefaultPic);
    }

    /**
     * 回收设置背景图片的内存
     *
     * @param imageView
     */
    public static void releaseImageViewResouce(View imageView) {
        if (imageView == null)
            return;
        Drawable drawable = imageView.getBackground();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    /**
     * 这个是保存二进制的图片
     *
     * @param bitmap
     * @param path
     */
    public static void savePictureByte(Bitmap bitmap, String path) {
        File file = new File(path);


        //将图片写成二进制字节的方式
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);

        //将图片写入文件中
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(byteArrayOutputStream.toByteArray());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
