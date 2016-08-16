package com.brioal.net.operator;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.brioal.net.cache.CacheManager;
import com.brioal.net.callback.RequestCallback;
import com.brioal.net.entity.RequestParameter;
import com.brioal.net.entity.APIData;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * 请求方法
 * Created by Brioal on 2016/8/13.
 */

public class APIRequest implements Runnable {
    public static String type = "GBk";
    private ArrayList<RequestParameter> mParameters;
    private RequestCallback mCallback;
    private APIData mAPIData;
    private URL mURL;
    private InputStream mInputStream;
    private HttpURLConnection mConnection;
    private Handler mHandler;

    public APIRequest(ArrayList<RequestParameter> parameters, APIData APIData, RequestCallback callback) {
        mParameters = parameters;
        mAPIData = APIData;
        mCallback = callback;
        mHandler = new Handler();
    }

    private String getParamters() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mParameters.size(); i++) {
            RequestParameter parameter = mParameters.get(i);
            sb.append(parameter.getKey() + "=" + parameter.getValue());
            if (i + 1 != mParameters.size()) {
                sb.append("&");
            }
        }
        return sb.toString();
    }

    @Override
    public void run() { //缓存时间内获取本地缓存
        if (mAPIData.getExpires() != 0) {
            try {
                String json = CacheManager.getInstance().getCache(mAPIData.getUrl() + "?" + getParamters(), true);
                if (json != null) {
                    mCallback.onSuccess(json, mAPIData.getUrl() + "?" + getParamters(), mAPIData.getExpires());
                    Log.i("Run", "return cache");
                    return;
                }
            } catch (Exception e) {

            }
        }
        try {
            mURL = new URL(mAPIData.getUrl());
            mConnection = (HttpURLConnection) mURL.openConnection();
            mConnection.setDoOutput(true);
            mConnection.setConnectTimeout(5000);
            mConnection.setReadTimeout(5000);
            mConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            mConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
            HttpConnctionSetUp(mConnection);
            if (mAPIData.isGet()) {
                mConnection.setRequestMethod("GET");
            } else {
                mConnection.setRequestMethod("POST");
            }
            mConnection.setDoOutput(true);
            mConnection.setDoInput(true);
            if (mParameters != null) {
                String params = getParamters();
                PrintWriter pw = new PrintWriter(mConnection.getOutputStream());
                pw.print(params);
                pw.flush();
                pw.close();
            }
            mInputStream = mConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(mInputStream, type);
            BufferedReader br = new BufferedReader(reader);
            String str = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
            final String result = stringBuilder.toString();
            if (!TextUtils.isEmpty(result)) {
                if (mCallback != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onSuccess(result, mAPIData.getUrl() + "?" + (mParameters == null ? "" : getParamters()), mAPIData.getExpires());
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onFail("网路请求失败,请重试");
                        }
                    });
                }
            }
        } catch (Exception eio) {
            if (mCallback != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onFail("网路请求失败,请重试");
                    }
                });
            }
        }
    }

    protected void HttpConnctionSetUp(HttpURLConnection connection) {

    }


    public void stop() {
        if (mConnection != null) {
            mConnection.disconnect();
        }
    }


}
