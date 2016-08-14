package com.brioal.net.operator;

import android.content.Context;

import com.brioal.net.callback.RequestCallback;
import com.brioal.net.entity.RequestParameter;
import com.brioal.net.entity.URLData;
import com.brioal.net.util.UrlConfigManager;

import java.util.ArrayList;

/**
 * Created by Brioal on 2016/8/13.
 */

public class RemoteService {
    private static RemoteService sRemoteService;

    private static String mXMlName;
    private static Context mContext;

    public static RemoteService getInstance() {
        if (sRemoteService == null) {
            sRemoteService = new RemoteService();
        }
        return sRemoteService;
    }

    public static void inited(Context context, String xmlName) {
        mXMlName = xmlName;
        mContext = context;
    }

    //调用API请求 Context
    public String invoke(String mApiKey, ArrayList<RequestParameter> parameters, RequestCallback callback) {
        URLData urlData = UrlConfigManager.findUrl(mContext, mApiKey, mXMlName);
        HttpRequest request = new HttpRequest(parameters, urlData, callback);
        RequestManager.addRequest(request);
        DefaultThreadPool.makeRequest(request);
        return null;
    }
}
