package com.drslark.nicefileexplore;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drslark.nicefileexplore.utils.FileSortHelper;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by 123 on 2015/11/1.
 */
public class FileListActivity extends TitleControlBaseActivity {
    private static final String SDCRADPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String LASTSTAYPATH = "LASTSTAYPAT";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<File> currentFiles;
    private String currentFilePath;
    private File currentFile;
    private FileSortHelper mFileSortHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_file_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.file_list);
        // 初始化数据
        currentFilePath = getIntent().getStringExtra(LASTSTAYPATH);
        if (StringUtils.isBlank(currentFilePath)) {
            currentFilePath = SDCRADPATH;
        }
        mAdapter = new FileListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
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

    private class FileListAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return currentFiles.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView fileIcon;
        TextView fileName;
        TextView fileInfo; // 子文件数量 或者 文件大小 修改时间等


        public ViewHolder(View itemView) {
            super(itemView);
            fileIcon = (ImageView) itemView.findViewById(R.id.file_icon);
            fileName = (TextView) itemView.findViewById(R.id.file_name);
            fileInfo = (TextView) itemView.findViewById(R.id.file_info);
        }
    }


}
