package com.brioal.net.operator;

import android.content.Context;

import com.brioal.net.callback.RequestCallback;
import com.brioal.net.entity.RequestParameter;
import com.brioal.net.entity.APIData;
import com.brioal.net.util.ApiManager;

import java.util.ArrayList;

/**
 * Created by Brioal on 2016/8/13.
 */

public class NetService {
    private static NetService sNetService;

    private static String mXMlName;
    private static Context mContext;

    public static NetService getInstance() {
        if (sNetService == null) {
            sNetService = new NetService();
        }
        return sNetService;
    }

    public static void inited(Context context, String xmlName) {
        mXMlName = xmlName;
        mContext = context;
    }

    //调用API请求 Context
    public String invoke(String mApiKey, ArrayList<RequestParameter> parameters, RequestCallback callback) {
        APIData APIData = ApiManager.findUrl(mContext, mApiKey, mXMlName);
        APIRequest request = new APIRequest(parameters, APIData, callback);
        RequestManager.addRequest(request);
        DefaultThreadPool.makeRequest(request);
        return null;
    }

    //调用API请求 Context
    public String invoke(APIData apiData, ArrayList<RequestParameter> parameters, RequestCallback callback) {
        APIRequest request = new APIRequest(parameters, apiData, callback);
        RequestManager.addRequest(request);
        DefaultThreadPool.makeRequest(request);
        return null;
    }
}
