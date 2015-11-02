package com.drslark.nicefileexplore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.bumptech.glide.Glide;
import com.drslark.nicefileexplore.model.FileStoreData;
import com.drslark.nicefileexplore.utils.FileSortHelper;
import com.drslark.nicefileexplore.utils.Util;
import com.drslark.nicefileexplore.widget.GeneralViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import rx.Observable;

/**
 * Created by zhutiantao on 2015/11/2.
 */
public class GeneralCategoryActivity extends TitleControlBaseActivity {
    public static final int DOC = 9090;
    public static final int MUSIC = 9091;
    public static final int APK = 9092;
    public static final int ZIP = 9093;
    private static final String CATEGORY_DATA = "CATEGORY_DATA";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<FileStoreData> files = new ArrayList<>();
    private FileCategoryHelper fileCategoryHelper;
    private int categoryKind;

    public static void actionShow(Context context, int kind) {
        Intent intent = new Intent(context, GeneralCategoryActivity.class);
        intent.putExtra(CATEGORY_DATA, kind);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryKind = getIntent().getIntExtra(CATEGORY_DATA, DOC);
        setContentView(R.layout.activity_pic_category);
        recyclerView = (RecyclerView) findViewById(R.id.picview_recyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fileCategoryHelper = new FileCategoryHelper(this);
        adapter = new FileRecylerViewAdapter();
        recyclerView.setAdapter(adapter);

        refreshFiles();

    }

    private void refreshFiles() {
        switch (categoryKind) {
            case DOC:
                files = fileCategoryHelper.queryDoc();
                break;
        }
    }

    private class FileRecylerViewAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

        @Override
        public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(GeneralCategoryActivity.this)
                    .inflate(R.layout.item_general_view, parent, false);
            GeneralViewHolder viewHolder = new GeneralViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(GeneralViewHolder holder, int position) {
            FileStoreData data = files.get(position);
            if (data.fileSize == 0) {
                return;
            }
            switch (categoryKind) {
                case DOC:
                    Glide.with(GeneralCategoryActivity.this).load(R.drawable.category_file_icon_doc).into(holder
                            .file_icon);
                    break;
                case MUSIC:
                    Glide.with(GeneralCategoryActivity.this).load(R.drawable.category_file_icon_music)
                            .into(holder.file_icon);
                    break;
                case ZIP:
                    Glide.with(GeneralCategoryActivity.this).load(R.drawable.category_file_icon_zip).into(holder
                            .file_icon);
                    break;
                case APK:
                    Glide.with(GeneralCategoryActivity.this).load(R.drawable.category_file_icon_apk).into(holder
                            .file_icon);
                    break;
                default:
                    break;
            }
            holder.file_name.setText(data.name.substring(data.name.lastIndexOf("/") + 1));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
            String dateAdd = dateFormat.format(new Date(data.dateAdd));
            String fileSize = Util.convertStorage(data.fileSize);
            String fileDesc = dateAdd + " | " + fileSize;
            holder.file_desc.setText(fileDesc);

        }

        @Override
        public int getItemCount() {
            return files.size();
        }
    }

}
