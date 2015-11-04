package com.drslark.nicefileexplore;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.drslark.nicefileexplore.utils.FileSortHelper;
import com.drslark.nicefileexplore.widget.GeneralViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 123 on 2015/11/1.
 */
public class FileListActivity extends TitleControlBaseActivity {
    private static final String SDCRADPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String SHOWPATH = "LASTSTAYPAT";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<File> currentFiles;
    private String currentFilePath;
    private File currentFile;
    private FileSortHelper mFileSortHelper;

    public static void actionShow(Context context, String directory) {
        Intent intent = new Intent(context, FileListActivity.class);
        intent.putExtra(SHOWPATH, directory);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_file_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.file_list);
        // 初始化数据
        currentFilePath = getIntent().getStringExtra(SHOWPATH);
        if (StringUtils.isBlank(currentFilePath)) {
            currentFilePath = SDCRADPATH;
        }
        mAdapter = new FileListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, 1);
            }
        });
        mFileSortHelper = new FileSortHelper();
        mFileSortHelper.setSortMethog(FileSortHelper.TYPE);
        refreshListView();
    }

    private void refreshListView() {
        File file = new File(currentFilePath);
        currentFiles = Arrays.asList(file.listFiles());
        Collections.sort(currentFiles, mFileSortHelper.getComparator());
        mAdapter.notifyDataSetChanged();
    }

    private class FileListAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

        @Override
        public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(GeneralViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return currentFiles.size();
        }
    }




}
