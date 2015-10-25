package com.drslark.nicefileexplore;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends TitleControlBaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.category_pic).setOnClickListener(this);
        findViewById(R.id.category_video).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.category_pic: MediaCategoryActivity.actionShow(this,MediaCategoryActivity.SHOW_PIC);break;
            case R.id.category_video: MediaCategoryActivity.actionShow(this,MediaCategoryActivity.SHOW_VIDEO);break;

        }
    }
}
