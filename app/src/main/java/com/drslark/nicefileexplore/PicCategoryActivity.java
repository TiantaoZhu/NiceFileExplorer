/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.drslark.nicefileexplore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.drslark.nicefileexplore.utils.FileSortHelper;
import com.drslark.nicefileexplore.widget.TitleController;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhutiantao on 2015/10/22.
 */
public class PicCategoryActivity extends TitleControlBaseActivity {
    private static final String TAG = PicCategoryActivity.class.getName();
    private TitleController titleController ;
    private FileCategoryHelper fileCategoryHelper;
    private RecyclerView picRecyclerView;
    private PicGridAdapter adapter;
    // 最近的100张照片
    private List<String> picDataSource;
    private boolean muiltChoiceMode;
    // 结果是目录名字和其目录下的文件名 1对多
    Map<String,List<String>> allPicMap = new HashMap<>();
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_category);
        fileCategoryHelper = new FileCategoryHelper(this);
        initData();
        initView();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initTitle();
    }

    private void initData() {
        picDataSource = new ArrayList<>(100);
        Observable.create(new Observable.OnSubscribe<Map>() {
            @Override
            public void call(Subscriber<? super Map> subscriber) {
                subscriber.onStart();
                Cursor cursor = fileCategoryHelper.query(FileCategoryHelper.Picture, FileSortHelper.DATE);
                int i = 0;
                while (cursor.moveToNext()) {

                    String path = cursor.getString(1);
                    Log.d(TAG, "FilePath: " + path);
                    File file = new File(path);
                    if (i < 100) {
                        picDataSource.add(path);
                        i++;
                    }
                    List<String> files = allPicMap.get(file.getParentFile().getAbsolutePath());
                    if (files == null) {
                        files = new LinkedList<>();
                        files.add(file.getName());
                    }
                }
                cursor.close();
                subscriber.onCompleted();
            }
        }).subscribe(new Subscriber<Map>() {
            @Override
            public void onStart() {
                super.onStart();
                if (dialog == null) {
                    dialog = new ProgressDialog(PicCategoryActivity.this);
                    dialog.setIndeterminate(true);
                    dialog.show();
                }
            }

            @Override
            public void onCompleted() {
                if (dialog != null) {
                    dialog.dismiss();
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map map) {

            }
        });

    }

    private void initView() {
        picRecyclerView = (RecyclerView) findViewById(R.id.picview_recyler);
        picRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        adapter = new PicGridAdapter();
        adapter.setDataSource(picDataSource);
        picRecyclerView.setAdapter(adapter);
    }

    private void initTitle() {
        titleController = getTitleController();
        titleController.getMainTitle().setText("图片");
        titleController.getUserIcon().setImageBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.actionbar_more_icon));
    }

    private class PicGridAdapter extends RecyclerView.Adapter<ViewHolder>{
        LayoutInflater inflater;
        List<String> dataSource;

        public PicGridAdapter() {
            this.inflater = LayoutInflater.from(PicCategoryActivity.this);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_pic_catrgory,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String path = dataSource.get(position);
            holder.pic_check.setVisibility(View.GONE);
            Glide.with(PicCategoryActivity.this)
                    .load(new File(path))
                    .placeholder(R.drawable.category_file_icon_pic)
                    .into(holder.pic_view);
        }

        @Override
        public int getItemCount() {
            return dataSource.size();
        }

        public void setDataSource(List<String> dataSource) {
            this.dataSource = dataSource;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic_view;
        AppCompatCheckBox pic_check;

        public ViewHolder(View itemView) {
            super(itemView);
            pic_view = (ImageView) itemView.findViewById(R.id.item_pic_view);
            pic_check = (AppCompatCheckBox) itemView.findViewById(R.id.item_pic_check);
        }
    }


}
