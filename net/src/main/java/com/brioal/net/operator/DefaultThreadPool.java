package com.brioal.net.operator;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Brioal on 2016/8/13.
 */

public class DefaultThreadPool {
    private static ThreadPoolExecutor threadPool;


    public static void makeRequest(Runnable runnable) {
        if (threadPool == null) {
            threadPool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
                    new ThreadPoolExecutor.DiscardOldestPolicy());
        }
        threadPool.execute(runnable);
    }
}
