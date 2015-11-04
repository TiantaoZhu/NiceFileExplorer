package com.drslark.nicefileexplore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.drslark.nicefileexplore.model.FileStoreData;
import com.drslark.nicefileexplore.utils.FileSortHelper;
import com.drslark.nicefileexplore.utils.IntentBuilder;
import com.drslark.nicefileexplore.utils.Util;
import com.drslark.nicefileexplore.widget.GeneralViewHolder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by zhutiantao on 2015/11/2.
 */
public class GeneralCategoryActivity extends TitleControlBaseActivity {
    private static final String TAG = "GeneralCategoryActivity";
    private static final String CATEGORY_DATA = "CATEGORY_DATA";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<FileStoreData> files = new ArrayList<>();
    private FileCategoryHelper fileCategoryHelper;
    private int categoryKind;
    private ProgressDialog mProgressDialog;
    private boolean isMutilsChoiceMode = false;

    public static void actionShow(Context context, int kind) {
        Intent intent = new Intent(context, GeneralCategoryActivity.class);
        intent.putExtra(CATEGORY_DATA, kind);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryKind = getIntent().getIntExtra(CATEGORY_DATA, FileCategoryHelper.Doc);
        setContentView(R.layout.activity_pic_category);
        recyclerView = (RecyclerView) findViewById(R.id.picview_recyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, 1);
            }
        });
        fileCategoryHelper = new FileCategoryHelper(this);
        adapter = new FileRecylerViewAdapter();
        recyclerView.setAdapter(adapter);

        refreshFiles();

    }

    private void refreshFiles() {
        Observable.create(new Observable.OnSubscribe<List<FileStoreData>>() {
            @Override
            public void call(Subscriber<? super List<FileStoreData>> subscriber) {
                subscriber.onStart();
                subscriber.onNext(fileCategoryHelper.query(categoryKind, FileSortHelper.DATE, null));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).doOnTerminate(new Action0() {
            @Override
            public void call() {
                if (Looper.myLooper() == Looper.getMainLooper()
                        && mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<FileStoreData>>() {
                    @Override
                    public void onStart() {
                        if (mProgressDialog == null) {
                            mProgressDialog = new ProgressDialog(GeneralCategoryActivity.this);
                            mProgressDialog.setIndeterminate(false);
                            mProgressDialog.setTitle("正在加载...");
                            mProgressDialog.show();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(List<FileStoreData> fileStoreDatas) {
                        files = fileStoreDatas;
                        adapter.notifyDataSetChanged();
                    }
                });
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
        public void onBindViewHolder(final GeneralViewHolder holder, int position) {
            final FileStoreData data = files.get(position);

            holder.file_name.setText(data.name.substring(data.name.lastIndexOf("/") + 1));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
            final String dateAdd = dateFormat.format(new Date(data.dateAdd));
            String fileSize = Util.convertStorage(data.fileSize);
            String fileDesc = dateAdd + " | " + fileSize;
            holder.file_desc.setText(fileDesc);
            holder.file_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isMutilsChoiceMode) {
                        IntentBuilder.viewFile(GeneralCategoryActivity.this, data.name, data.mimeType);
                    }
                }
            });
            switch (categoryKind) {
                case FileCategoryHelper.Doc:
                    holder.file_icon.setImageResource(R.drawable.category_file_icon_doc);
                    break;
                case FileCategoryHelper.Music:
                    holder.file_icon.setImageResource(R.drawable.category_file_icon_music);
                    break;
                case FileCategoryHelper.Zip:
                    holder.file_icon.setImageResource(R.drawable.category_file_icon_zip);
                    break;
                case FileCategoryHelper.Apk:
//                    final LruCache<String, DrawableHolder> cache = new LruCache<String, DrawableHolder>(1024 * 1024) {
//                        @Override
//                        protected int sizeOf(String key, DrawableHolder value) {
//                            return 100 * 1024;
//                        }
//                    };

                    Drawable drawable = null;
                    String appName = null;
                    String versionCode = null;
                    holder.file_icon.setImageResource(R.drawable.category_file_icon_apk);
                    holder.file_icon.setTag(data.name);
                    final PackageManager pm = GeneralCategoryActivity.this.getPackageManager();
                    PackageInfo info = pm.getPackageArchiveInfo(data.name,
                            PackageManager.GET_ACTIVITIES);
                    Log.d(TAG, "ICONTIEM loadIcon start");
                    if (info != null) {
                        final ApplicationInfo appInfo = info.applicationInfo;
                        appInfo.sourceDir = data.name;
                        appInfo.publicSourceDir = data.name;

                        // 这一步需要50-100ms是很耗时的 会丢帧
                        drawable = appInfo.loadIcon(pm);
//                        Observable.create(new Observable.OnSubscribe<DrawableHolder>() {
//                            @Override
//                            public void call(Subscriber<? super DrawableHolder> subscriber) {
//
//                                DrawableHolder holder1 = cache.get(data.name);
//                                if (holder1 == null) {
//                                    holder1 = new DrawableHolder();
//                                    holder1.setName(data.name);
//                                    Drawable drawable1 = appInfo.loadIcon(pm);
//                                    holder1.setDrawable(drawable1);
//                                    cache.put(data.name, holder1);
//                                }
//                                subscriber.onNext(holder1);
//                            }
//                        })
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread()).subscribe(
//                                new Action1<DrawableHolder>() {
//                                    @Override
//                                    public void call(DrawableHolder drawable) {
//                                        if (drawable != null && drawable.getDrawable() != null && drawable.getName() ==
//                                                data.name) {
//                                            holder.file_icon.setImageDrawable(drawable.getDrawable());
//                                        }
//                                    }
//                                });

                        appName = (String) pm.getApplicationLabel(appInfo);
                        versionCode = info.versionName;
                    }
                    Log.d(TAG, "ICONTIEM loadIcon end");
                    if (drawable != null) {
                        holder.file_icon.setImageDrawable(drawable);
                    } else {
                        holder.file_icon.setImageResource(R.drawable.category_file_icon_apk);
                    }
                    Log.d(TAG, "ICONTIEM IntoImage start");

                    if (appName != null) {
                        holder.file_name.setText(appName);
                    }
                    if (versionCode != null) {
                        holder.file_desc.append(" | " + "版本号 : " + versionCode);
                    }
                    break;
                default:
                    break;
            }

        }

        @Override
        public int getItemCount() {
            return files.size();
        }

    }

}
