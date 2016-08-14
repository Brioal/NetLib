package com.brioal.net.cache;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Brioal on 2016/8/14.
 */

public class CacheManager {
    private static CacheManager sCacheManager;
    private String mCacheFileDir;

    public static CacheManager getInstance() {
        if (sCacheManager == null) {
            sCacheManager = new CacheManager();
        }
        return sCacheManager;
    }

    //放置缓存 api地址加请求参数 , 返回的json数组 , 保存时间
    public void putCache(String url, String jsonString, long expires) {
        checkInited();
        File file = new File(mCacheFileDir + "/" + url.replaceAll("\\/", ""));
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
            br.write(expires + "");
            br.newLine();
            br.write(System.currentTimeMillis() + "");
            br.newLine();
            br.write(jsonString);
            br.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //删除所有的缓存APi
    public boolean isDeleteAllCache() {
        checkInited();
        if (mCacheFileDir.isEmpty()) {
            try {
                throw new Exception("必须先初始化缓存空间");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            File file = new File(mCacheFileDir);
            for (File apifile : file.listFiles()) {
                apifile.delete();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    //获取指定Url的缓存 api地址及数据 , 是否检测缓存时间
    public String getCache(String url, boolean isCheckExpires) {
        checkInited();
        File file = new File(mCacheFileDir + "/" + url.replaceAll("\\/", ""));
        if (!file.exists()) {
            return null;
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String expriesString = br.readLine(); //缓存时间
                if (isCheckExpires) { //检查缓存时间
                    String timeString = br.readLine(); //保存时候的时间
                    long timr = timeString != null ? Long.valueOf(timeString) : 0;
                    long expries = expriesString != null ? Integer.valueOf(expriesString) : 0;
                    if ((System.currentTimeMillis() - timr) < expries) { //缓存可用
                        StringBuilder sb = new StringBuilder();
                        String str = "";
                        while ((str = br.readLine()) != null) {
                            sb.append(str);
                        }
                        return sb.toString(); //返回缓存的json数据

                    } else {
                        return null;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //初始化缓存目录
    public void initCacheDir(Context context) {
        File fileDir = context.getFilesDir();
        File cacheDir = new File(fileDir.toString(), "/APICache");
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        mCacheFileDir = cacheDir.getAbsolutePath();
    }

    public void checkInited() {
        if (mCacheFileDir == null) {
            try {
                throw new Exception("必须先初始化缓存空间");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
