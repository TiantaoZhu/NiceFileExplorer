/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.drslark.nicefileexplore.widget;

import com.drslark.nicefileexplore.R;
import com.drslark.nicefileexplore.SearchActivity;
import com.drslark.nicefileexplore.UserInfoActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zhutiantao on 2015/10/15.
 */
public class TitleController {
    private final View titleBar;
    private ImageView backIcon;
    private TextView leftText;
    private TextView mainTitle;
    private ImageView userIcon;
    private ImageView searchIcon;

    public TitleController(View titleBar) {
        this(null,titleBar);
    }
    public TitleController(final Activity activity,View titleBar) {
        this.titleBar = titleBar;
        if (titleBar != null) {
            backIcon = (ImageView) titleBar.findViewById(R.id.title_left_icon);
            leftText = (TextView) titleBar.findViewById(R.id.title_left_text);
            mainTitle = (TextView) titleBar.findViewById(R.id.title_title);
            userIcon = (ImageView) titleBar.findViewById(R.id.title_user_icon);
            searchIcon = (ImageView) titleBar.findViewById(R.id.title_search_icon);
        }
        if (activity != null) {
            backIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            });
            userIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, UserInfoActivity.class));
                }
            });
            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, SearchActivity.class));
                }
            });
        }
    }

    public ImageView getBackIcon() {
        return backIcon;
    }

    public TextView getLeftText() {
        return leftText;
    }

    public TextView getMainTitle() {
        return mainTitle;
    }

    public ImageView getUserIcon() {
        return userIcon;
    }

    public ImageView getSearchIcon() {
        return searchIcon;
    }
}
