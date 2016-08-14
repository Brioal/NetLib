package com.brioal.net.callback;

/**请求结果回调
 * 实际项目中对onFail统一处理,建立抽象类
 * Created by Brioal on 2016/8/13.
 */

public interface RequestCallback {
    void onSuccess(String content , String apiUrl , long expries);

    void onFail(String errorMessage);
}
