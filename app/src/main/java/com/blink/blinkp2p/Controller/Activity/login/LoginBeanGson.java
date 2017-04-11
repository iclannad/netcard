package com.blink.blinkp2p.Controller.Activity.login;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/4/11.
 */
public class LoginBeanGson {

    private boolean isRemeber;
    private boolean isAutologin;
    private String username;
    private String password;
    private long time;

    public LoginBeanGson() {
    }

    public LoginBeanGson(String username, String password, boolean isRemeberm, boolean isAutologin) {
        this.username = username;
        this.password = password;
        this.isRemeber = isRemeberm;
        this.isAutologin = isAutologin;
        this.time = System.currentTimeMillis();
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public boolean isRemeber() {
        return isRemeber;
    }

    public void setRemeber(boolean isRemeber) {
        this.isRemeber = isRemeber;
    }

    public boolean isAutologin() {
        return isAutologin;
    }

    public void setAutologin(boolean isAutologin) {
        this.isAutologin = isAutologin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long l) {
        this.time = l;
    }

}
