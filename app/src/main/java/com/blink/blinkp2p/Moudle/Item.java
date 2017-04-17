package com.blink.blinkp2p.Moudle;

import android.graphics.drawable.Drawable;

/**
 * Created by Soft on 2016/7/9.
 */
public class Item {

    public int id;

    private String delete = null;
    private String edit = null;
    private String Title = null;
    private String Subtitle = null;
    private Drawable Logo = null;
    private int height = 0;
    private String RightTitle = null;
    private boolean isShare = false;

    private Drawable gridImage = null;
    private String gridText = null;
    private Drawable gridCenterImage = null;
    private int gridImageSize = 0;

    private Drawable listright = null;
    private Drawable listImage = null;
    private String listText = null;
    private String listSubText = null;
    private String listRightText = null;
    private String listRightText1 = null;
    private String FLAG = null;
    private boolean isVisiable = false;
    private boolean isCheck = false;
    private int RightImageSize = 0;
    private int ImageSize = 0;
    private int selector = 0;
    private int listTextColor = 0;


    private Drawable homeImage = null;
    private String homeText = null;
    private String homeSubText = null;
    private Drawable homeRightImage = null;
    private String homeRight = null;
    private String homeRight1 = null;
    private int homeRelative = 0;

    private int progressBar = 0;

    private String languageText = null;
    private boolean languageRadio = false;


    public int getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(int progressBar) {
        this.progressBar = progressBar;
    }

    public int getListTextColor() {
        return listTextColor;
    }

    public void setListTextColor(int listTextColor) {
        this.listTextColor = listTextColor;
    }

    public int getSelector() {
        return selector;
    }

    public void setSelector(int selector) {
        this.selector = selector;
    }

    public int getImageSize() {
        return ImageSize;
    }

    public void setImageSize(int imageSize) {
        ImageSize = imageSize;
    }

    public int getGridImageSize() {
        return gridImageSize;
    }

    public void setGridImageSize(int gridImageSize) {
        this.gridImageSize = gridImageSize;
    }

    public int getRightImageSize() {
        return RightImageSize;
    }

    public void setRightImageSize(int rightImageSize) {
        RightImageSize = rightImageSize;
    }

    public String getListRightText1() {
        return listRightText1;
    }

    public void setListRightText1(String listRightText1) {
        this.listRightText1 = listRightText1;
    }

    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
    }

    public boolean isLanguageRadio() {
        return languageRadio;
    }

    public void setLanguageRadio(boolean languageRadio) {
        this.languageRadio = languageRadio;
    }

    public String getLanguageText() {
        return languageText;
    }

    public void setLanguageText(String languageText) {
        this.languageText = languageText;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isVisiable() {
        return isVisiable;
    }

    public void setVisiable(boolean visiable) {
        isVisiable = visiable;
    }

    public Drawable getGridCenterImage() {
        return gridCenterImage;
    }

    public void setGridCenterImage(Drawable gridCenterImage) {
        this.gridCenterImage = gridCenterImage;
    }

    public Drawable getHomeImage() {
        return homeImage;
    }

    public void setHomeImage(Drawable homeImage) {
        this.homeImage = homeImage;
    }

    public String getHomeText() {
        return homeText;
    }

    public void setHomeText(String homeText) {
        this.homeText = homeText;
    }

    public String getHomeSubText() {
        return homeSubText;
    }

    public void setHomeSubText(String homeSubText) {
        this.homeSubText = homeSubText;
    }

    public Drawable getHomeRightImage() {
        return homeRightImage;
    }

    public void setHomeRightImage(Drawable homeRightImage) {
        this.homeRightImage = homeRightImage;
    }

    public String getHomeRight() {
        return homeRight;
    }

    public void setHomeRight(String homeRight) {
        this.homeRight = homeRight;
    }

    public String getHomeRight1() {
        return homeRight1;
    }

    public void setHomeRight1(String homeRight1) {
        this.homeRight1 = homeRight1;
    }

    public int getHomeRelative() {
        return homeRelative;
    }

    public void setHomeRelative(int homeRelative) {
        this.homeRelative = homeRelative;
    }

    public String getRightTitle() {
        return RightTitle;
    }

    public void setRightTitle(String rightTitle) {
        RightTitle = rightTitle;
    }

    public String getListRightText() {
        return listRightText;
    }

    public void setListRightText(String listRightText) {
        this.listRightText = listRightText;
    }

    public String getFLAG() {
        return FLAG;
    }

    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }

    public String getListSubText() {
        return listSubText;
    }

    public void setListSubText(String listSubText) {
        this.listSubText = listSubText;
    }

    public Drawable getListright() {
        return listright;
    }

    public void setListright(Drawable listright) {
        this.listright = listright;
    }

    public Drawable getListImage() {
        return listImage;
    }

    public void setListImage(Drawable listImage) {
        this.listImage = listImage;
    }

    public String getListText() {
        return listText;
    }

    public void setListText(String listText) {
        this.listText = listText;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Drawable getGridImage() {
        return gridImage;
    }

    public void setGridImage(Drawable gridImage) {
        this.gridImage = gridImage;
    }

    public String getGridText() {
        return gridText;
    }

    public void setGridText(String gridText) {
        this.gridText = gridText;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubtitle() {
        return Subtitle;
    }

    public void setSubtitle(String subtitle) {
        Subtitle = subtitle;
    }

    public Drawable getLogo() {
        return Logo;
    }

    public void setLogo(Drawable logo) {
        Logo = logo;
    }
}
