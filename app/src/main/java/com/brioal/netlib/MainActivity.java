package com.brioal.netlib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.brioal.net.cache.CacheManager;
import com.brioal.net.callback.RequestCallback;
import com.brioal.net.entity.RequestParameter;
import com.brioal.net.operator.RemoteService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CacheManager.getInstance().initCacheDir(this);
        RemoteService.inited(this, "apis.txt");
        ArrayList<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameter("j_username", "320130937631"));
        parameters.add(new RequestParameter("j_password", "142536"));
        final String s = RemoteService.getInstance().invoke("LZULogin", parameters, new RequestCallback() {
            @Override
            public void onSuccess(String content, String apidata, long expries) {
                Log.i("Result", content);
                CacheManager.getInstance().putCache(apidata, content, expries);
            }

            @Override
            public void onFail(String errorMessage) {
                Log.i("Result", errorMessage);

            }
        });
    }
}
