package blink.com.blinkcard320.Moudle.skin;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/3/20.
 */
public class SkinBean {
    private Drawable drawable = null;
    private String text = null;
    private int height = 0;
    private Drawable ItemClickBack = null;

    private int SkinBackColor = 0;
    private int SkinIDColor = 0;

    public int getSkinBackColor() {
        return SkinBackColor;
    }

    public void setSkinBackColor(int skinBackColor) {
        SkinBackColor = skinBackColor;
    }

    public int getSkinIDColor() {
        return SkinIDColor;
    }

    public void setSkinIDColor(int skinIDColor) {
        SkinIDColor = skinIDColor;
    }

    public Drawable getItemClickBack() {
        return ItemClickBack;
    }

    public void setItemClickBack(Drawable itemClickBack) {
        ItemClickBack = itemClickBack;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
