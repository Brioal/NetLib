package com.brioal.net.entity;

/**
 * 请求实体类
 * Created by Brioal on 2016/8/13.
 */

public class RequestParameter {
    private String mKey;
    private String mValue;

    public RequestParameter(String key, String value) {
        mKey = key;
        mValue = value;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }
}
