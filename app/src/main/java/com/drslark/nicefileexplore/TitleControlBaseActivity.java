
package com.drslark.nicefileexplore;

import java.lang.ref.SoftReference;

import com.drslark.nicefileexplore.widget.TitleController;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by zhutiantao on 2015/10/15.
 */
public class TitleControlBaseActivity extends Activity {
    private TitleController controller;
    private View titleBar;

    private SoftReference<Activity> mSoftActivty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSoftActivty = new SoftReference<Activity>(this);
        ActivityManagerUtil.getInstance().pushActivity(mSoftActivty);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        titleBar = findViewById(R.id.titlebar);
        if (titleBar != null) {
            controller = new TitleController(this, titleBar);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkVersion();
    }

    private void checkVersion() {

    }

    public TitleController getTitleController() {
        return controller;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManagerUtil.getInstance().popActivity(mSoftActivty);
    }

}
