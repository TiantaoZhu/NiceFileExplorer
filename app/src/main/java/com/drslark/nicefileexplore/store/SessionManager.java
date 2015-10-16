/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.drslark.nicefileexplore.store;

import com.drslark.nicefileexplore.model.User;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhutiantao on 2015/10/15.
 */
public class SessionManager {
    public static final String PREF_SESSION = "approval_session";
    public static final String PREF_KEY_TIME = "time";
    public static final String PREF_KEY_USERNAME = "username";
    public static final String PREF_KEY_USERNAMEE = "usernameE";

    private static SessionManager sInstance;
    private Context context;

    private SessionManager(Context context) {
        this.context = context;
    }
    public static SessionManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SessionManager(context);
        }
        return sInstance;
    }

    private User user;


    public static Session getSession(final Context context) {

        SharedPreferences sessionPref = context.getSharedPreferences(PREF_SESSION, 0);
        String username = sessionPref.getString(PREF_KEY_USERNAME, "");
        String usernameE = sessionPref.getString(PREF_KEY_USERNAMEE, "");
        // String uuid = sessionPref.getString(PREF_KEY_UUID, "");
        long time = sessionPref.getLong(PREF_KEY_TIME, -1);
        return null;
    }

    public static void setSession(final Context context, Session session) {
        SharedPreferences sessionPref = context.getSharedPreferences(PREF_SESSION, 0);
        SharedPreferences.Editor editor = sessionPref.edit();

        // editor.putString(PREF_KEY_UUID, session.uuid);
        editor.putLong(PREF_KEY_TIME, session.time);
        editor.commit();
    }

    public static void clearSession(final Context context) {
        SharedPreferences sessionPref = context.getSharedPreferences(PREF_SESSION, 0);
        SharedPreferences.Editor editor = sessionPref.edit();
        editor.clear();
        editor.commit();
    }

    public class Session {
        public long userid;
        public long time;
    }

}
