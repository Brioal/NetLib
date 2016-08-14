package com.brioal.net.operator;

import android.text.TextUtils;
import android.util.Log;

import com.brioal.net.cache.CacheManager;
import com.brioal.net.callback.RequestCallback;
import com.brioal.net.entity.RequestParameter;
import com.brioal.net.entity.URLData;

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

public class HttpRequest implements Runnable {
    public static String type = "GBk";
    private ArrayList<RequestParameter> mParameters;
    private RequestCallback mCallback;
    private URLData mURLData;
    private URL mURL;
    private InputStream mInputStream;
    private HttpURLConnection mConnection;

    public HttpRequest(ArrayList<RequestParameter> parameters, URLData urlData, RequestCallback callback) {
        mParameters = parameters;
        mURLData = urlData;
        mCallback = callback;
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
        try {
            String json = CacheManager.getInstance().getCache(mURLData.getUrl() + "?" + getParamters(), true);
            if (json != null) {
                mCallback.onSuccess(json, mURLData.getUrl() + "?" + getParamters(), mURLData.getExpires());
                Log.i("Run", "return cache");
                return;
            }
            String params = getParamters();
            mURL = new URL(mURLData.getUrl());
            mConnection = (HttpURLConnection) mURL.openConnection();
            mConnection.setDoOutput(true);
            mConnection.setConnectTimeout(5000);
            mConnection.setReadTimeout(5000);
            if (mURLData.isGet()) {
                mConnection.setRequestMethod("GET");
            } else {
                mConnection.setRequestMethod("POST");
            }
            mConnection.setDoOutput(true);
            mConnection.setDoInput(true);
            PrintWriter pw = new PrintWriter(mConnection.getOutputStream());
            pw.print(params);
            pw.flush();
            pw.close();
            mInputStream = mConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(mInputStream, type);
            BufferedReader br = new BufferedReader(reader);
            String str = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
            String result = stringBuilder.toString();
            if (!TextUtils.isEmpty(result)) {
                if (mCallback != null) {
                    mCallback.onSuccess(result, mURLData.getUrl() + "?" + getParamters(), mURLData.getExpires());
                } else {
                    mCallback.onFail("获取结果出错");
                }
            }
        } catch (Exception eio) {
            if (mCallback != null) {
                mCallback.onFail("网路请求失败");
            }
        }
    }

    public void stop() {
        if (mConnection != null) {
            mConnection.disconnect();
        }
    }


}
