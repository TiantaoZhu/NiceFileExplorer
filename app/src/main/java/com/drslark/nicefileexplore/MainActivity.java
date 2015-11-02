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
        findViewById(R.id.category_document).setOnClickListener(this);
        findViewById(R.id.category_zip).setOnClickListener(this);
        findViewById(R.id.category_apk).setOnClickListener(this);
        findViewById(R.id.category_music).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.category_pic: MediaCategoryActivity.actionShow(this,MediaCategoryActivity.SHOW_PIC);break;
            case R.id.category_video: MediaCategoryActivity.actionShow(this,MediaCategoryActivity.SHOW_VIDEO);break;
            case R.id.category_document:
                GeneralCategoryActivity.actionShow(this, FileCategoryHelper.Doc);
                break;
            case R.id.category_zip:
                GeneralCategoryActivity.actionShow(this, FileCategoryHelper.Zip);
                break;
            case R.id.category_music:
                GeneralCategoryActivity.actionShow(this, FileCategoryHelper.Music);
                break;
            case R.id.category_apk:
                GeneralCategoryActivity.actionShow(this, FileCategoryHelper.Apk);
                break;


        }
    }
}
