package com.waibao.qualityCertification.base;

import android.app.Application;
import android.os.Handler;

/**
 * Application的父类
 */
public class BaseApplication extends Application {
    private static BaseApplication application;
    private static int mainTid;
    private static Handler handler;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        mainTid = android.os.Process.myTid();
        handler = new Handler();
    }
    public static BaseApplication getInstance() {

        return application;
    }
    public static int getMainTid() {
        return mainTid;
    }

    public static Handler getHandler() {
        return handler;
    }
}
