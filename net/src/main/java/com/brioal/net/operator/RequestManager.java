package com.brioal.net.operator;

import java.util.ArrayList;
import java.util.List;

/**请求管理器
 * Created by Brioal on 2016/8/13.
 */

public class RequestManager {
    private static List<HttpRequest> mHttpRequests;

    //添加请求
    public static void addRequest(HttpRequest request) {
        if (mHttpRequests == null) {
            mHttpRequests = new ArrayList<>();
        }
        mHttpRequests.add(request);
    }

    //取消所有请求
    public void cancelRequest() {
        if (mHttpRequests != null && mHttpRequests.size() > 0) {
            for (int i = 0; i < mHttpRequests.size(); i++) {
                mHttpRequests.get(i).stop();
            }
        }
        mHttpRequests.clear();
    }


}
