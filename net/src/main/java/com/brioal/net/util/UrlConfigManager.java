package com.brioal.net.util;

import android.content.Context;

import com.brioal.net.entity.URLData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * API地址管理类,从xml文件中提取APi信息
 * Created by Brioal on 2016/8/13.
 */

public class UrlConfigManager {
    private static List<URLData> sDatas;

    public static URLData findUrl(Context context, String urlKey, String path) {
        if (sDatas == null) {
            sDatas = fetchDataFrom(context, path);
        }
        for (int i = 0; i < sDatas.size(); i++) {
            if (sDatas.get(i).getKey().equals(urlKey)) {
                return sDatas.get(i);
            }
        }
        return null;
    }

    private static List<URLData> fetchDataFrom(Context context, String filename) {
        List<URLData> list = new ArrayList<>();
        InputStreamReader streamReader = null;
        try {
            streamReader = new InputStreamReader(context.getResources().getAssets().open(filename));
            BufferedReader reader = new BufferedReader(streamReader);
            String str = "";
            StringBuilder sb = new StringBuilder();
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            String jsonStr = sb.toString();
            JSONArray array = new JSONArray(jsonStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                URLData data = new URLData(object.getString("Key"), object.getInt("Expires"), object.getBoolean("IsGet"), object.getString("Url"));
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
