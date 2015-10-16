/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.drslark.nicefileexplore;

import java.lang.ref.SoftReference;
import java.util.Stack;

import android.app.Activity;

public class ActivityManagerUtil {
    private Stack<SoftReference<Activity>> activityStack;
    private static ActivityManagerUtil instance;

    private ActivityManagerUtil() {
    }

    public synchronized static ActivityManagerUtil getInstance() {
        if (instance == null) {
            instance = new ActivityManagerUtil();
        }
        return instance;
    }

    //退出栈顶Activity
    public void popActivity(SoftReference<Activity> activity) {
        if (activity != null) {
            if ((activity.get() != null) && (!activity.get().isFinishing())) {
                activity.get().finish();
            }
            activityStack.remove(activity);
            activity = null;
        }
    }

    //获得当前栈顶Activity
    private SoftReference<Activity> currentActivity() {
        SoftReference<Activity> activity = null;
        if ((activityStack != null) && (!activityStack.empty())) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    //将当前Activity推入栈中
    public void pushActivity(SoftReference<Activity> activity) {
        if (activityStack == null) {
            activityStack = new Stack<SoftReference<Activity>>();
        }
        activityStack.add(activity);
    }

    //退出栈中所有Activity
    public void popAllActivity() {
        while (true) {
            SoftReference<Activity> activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }
}