package com.example.administrator.data_sdk.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/3/15.
 * <p/>
 * 这个类是实现图片的所有操作
 */
public class ImageTool {


    /**
     * 这个是Drawable 画成圆角
     *
     * @param context
     * @param drawable
     * @return
     */
    public static Drawable CreateRoundImage(Context context, Drawable drawable) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        // 将drawable转化成bitmap
        //因为drawable不是能直接进行操作的
        Bitmap bitmap = ImageTransformation.Drawable2Bitmap(drawable);

        Bitmap target = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawRoundRect(rect, 400, 400, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return ImageTransformation.Bitmap2Drawable(context, target);
    }


    /**
     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
     *
     * @param drawable   原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Drawable centerSquareScaleDrawable(Context context, Drawable drawable, int edgeLength) {
        // 获得图片的宽高
        Bitmap bitmap = ImageTransformation.Drawable2Bitmap(drawable);
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }
        bitmap = ScaleBitmap(bitmap, edgeLength);

        return ImageTransformation.Bitmap2Drawable(context, bitmap);
    }


    /**
     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
     *
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Context context, Bitmap bitmap, int edgeLength) {
        // 获得图片的宽高
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }
        bitmap = ScaleBitmap(bitmap, edgeLength);
        return bitmap;
    }


    /**
     * 处理图片的大小
     * 缩放图片
     *
     * @param drawable  所要转换的drawable
     * @param newWidth
     * @param newHeight
     * @return 指定宽高的bitmap
     */
    public static Drawable ZoomDrawable(Context context, Drawable drawable, int newWidth, int newHeight) {
        // 获得图片的宽高
        Bitmap bm = ImageTransformation.Drawable2Bitmap(drawable);

        bm = Zoom(bm, newWidth, newHeight);

        return ImageTransformation.Bitmap2Drawable(context, bm);
    }

    /**
     * 处理图片的大小
     * 缩放图片
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return 指定宽高的bitmap
     */
    public static Bitmap ZoomBitmap(Context context, Bitmap bitmap, int newWidth, int newHeight) {
        bitmap = Zoom(bitmap, newWidth, newHeight);

        return bitmap;
    }

    /**
     * 这个是进行图片的缩放操作
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    private static Bitmap Zoom(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

//        newWidth = SystemOperation.dip2px(context, newWidth);
//        newHeight = SystemOperation.dip2px(context, newHeight);
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        return newbm;
    }


    /**
     * 这个是进行剪切的操作
     *
     * @param bitmap
     * @param edgeLength
     * @return
     */
    private static Bitmap ScaleBitmap(Bitmap bitmap, int edgeLength) {
        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }
        return result;
    }
}
