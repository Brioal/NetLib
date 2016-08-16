package com.brioal.net.operator;

import java.util.ArrayList;
import java.util.List;

/**请求管理器
 * Created by Brioal on 2016/8/13.
 */

public class RequestManager {
    private static List<APIRequest> sMAPIRequests;

    //添加请求
    public static void addRequest(APIRequest request) {
        if (sMAPIRequests == null) {
            sMAPIRequests = new ArrayList<>();
        }
        sMAPIRequests.add(request);
    }

    //取消所有请求
    public void cancelRequest() {
        if (sMAPIRequests != null && sMAPIRequests.size() > 0) {
            for (int i = 0; i < sMAPIRequests.size(); i++) {
                sMAPIRequests.get(i).stop();
            }
        }
        sMAPIRequests.clear();
    }


}
