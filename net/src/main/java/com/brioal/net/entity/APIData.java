package com.brioal.net.entity;

/**
 * API接口实体类
 * Created by Brioal on 2016/8/13.
 */

public class APIData {
    private String mKey;
    private long mExpires;
    private boolean isGet;
    private String mUrl;

    public APIData(String key, long expires, boolean netType, String url) {
        mKey = key;
        mExpires = expires;
        isGet = netType;
        mUrl = url;
    }

    public boolean isGet() {
        return isGet;
    }

    public void setGet(boolean get) {
        isGet = get;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public long getExpires() {
        return mExpires;
    }

    public void setExpires(long expires) {
        mExpires = expires;
    }


    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
