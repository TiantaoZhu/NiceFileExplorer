package com.drslark.nicefileexplore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.MemoryCategory;
import com.drslark.nicefileexplore.model.MediaStoreData;
import com.drslark.nicefileexplore.utils.IntentBuilder;
import com.drslark.nicefileexplore.widget.TitleController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhutiantao on 2015/10/22.
 * 用来显示图片和视频的Activity
 */
public class MediaCategoryActivity extends TitleControlBaseActivity  {
    private static final String TAG = MediaCategoryActivity.class.getName();
    public static final String SHOW_WHAT = "show_what";
    public static final int SHOW_PIC = 8888;
    public static final int SHOW_VIDEO = 8889;
    public static final int SPAN_COUNT = 3;
    public static final int GRID_MARGIN = 1;
    private static final String STATE_POSITION_INDEX = "state_position_index";


    private TitleController titleController;
    private FileCategoryHelper fileCategoryHelper;
    private RecyclerView picRecyclerView;
    private PicGridAdapter adapter;
    private String currentDir;
    private int imageWidth;
    private int[] actualDimensions;
    // 最近的100张多媒体
    private List<MediaStoreData> mediaDataSource;
    private boolean muiltChoiceMode;
    // 结果是目录名字和其目录下的文件名 1对多 记录所有文件夹下的图片 第一次扫描的时候 建立
    // 后期可以点击强制刷新来扫描
    ArrayMap<String,List<MediaStoreData>> allMediaMap ;
    private ProgressDialog dialog;
    private int showWhat;

    public static void actionShow(Context context,int showWhat) {
        Intent intent = new Intent(context,MediaCategoryActivity.class);
        intent.putExtra(SHOW_WHAT,showWhat);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showWhat = getIntent().getIntExtra(SHOW_WHAT,SHOW_PIC);
        setContentView(R.layout.activity_pic_category);
        fileCategoryHelper = new FileCategoryHelper(this);
        initData();
        initView(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initTitle();
    }
    private void initData() {
        mediaDataSource = new ArrayList<>();
        Observable.create(new Observable.OnSubscribe<Map>() {
            @Override
            public void call(Subscriber<? super Map> subscriber) {
                subscriber.onStart();
                mediaDataSource = showWhat == SHOW_PIC
                        ? fileCategoryHelper.queryImages()
                        : fileCategoryHelper.queryVideos();
                allMediaMap = new ArrayMap<>();
                for (MediaStoreData m :mediaDataSource) {
                    String parentPath = m.name.substring(0,m.name.lastIndexOf(File.separator));
                    List<MediaStoreData> list = allMediaMap.get(parentPath);
                    if (list == null) {
                        list = new ArrayList<MediaStoreData>();
                        allMediaMap.put(parentPath,list);
                    }
                    list.add(m);
                }
                subscriber.onCompleted();
            }
        }).subscribe(new Subscriber<Map>() {
            @Override
            public void onStart() {
                super.onStart();
                if (dialog == null) {
                    dialog = new ProgressDialog(MediaCategoryActivity.this);
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

    private void initView(Bundle savedInstanceState) {
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);

        picRecyclerView = (RecyclerView) findViewById(R.id.picview_recyler);
        picRecyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
        imageWidth = getScreenWidth(this) / SPAN_COUNT - 2 * GRID_MARGIN;


        adapter = new PicGridAdapter();
        adapter.setDataSource(mediaDataSource);
        picRecyclerView.setAdapter(adapter);


        picRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(GRID_MARGIN,GRID_MARGIN,GRID_MARGIN,GRID_MARGIN);
            }
        });

        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt(STATE_POSITION_INDEX);
            picRecyclerView.scrollToPosition(index);
        }

    }

    private void initTitle() {
        titleController = getTitleController();

        titleController.getMainTitle().setText(showWhat==SHOW_PIC?"图片":"视频");
        titleController.getUserIcon().setImageBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.actionbar_more_icon));
    }

    private class PicGridAdapter extends RecyclerView.Adapter<ViewHolder> implements
            ListPreloader.PreloadModelProvider<MediaStoreData> {
        LayoutInflater inflater;
        List<MediaStoreData> dataSource;


        public PicGridAdapter() {
            this.inflater = LayoutInflater.from(MediaCategoryActivity.this);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = inflater.inflate(R.layout.item_pic_catrgory, parent, false);
//            view.getLayoutParams().width = screenWidth;

//            if (actualDimensions == null) {
//                view.getViewTreeObserver().addOnPreDrawListener(
//                        new ViewTreeObserver.OnPreDrawListener() {
//                            @Override
//                            public boolean onPreDraw() {
//                                if (actualDimensions == null) {
//                                    actualDimensions = new int[]{view.getScreenWidth(), view.getHeight()};
//                                }
//                                view.getViewTreeObserver().removeOnPreDrawListener(this);
//                                return true;
//                            }
//                        });
//            }
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            MediaStoreData mediaStoreData = dataSource.get(position);
            holder.pic_check.setVisibility(View.GONE);
            holder.pic_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!muiltChoiceMode) {
                        IntentBuilder.viewFile(MediaCategoryActivity.this,
                                dataSource.get(position).name,
                                dataSource.get(position).mimeType);
                    }
                }
            });
            Glide.with(MediaCategoryActivity.this)
                    .load(mediaStoreData.uri)
                    .centerCrop()
                    .placeholder(R.drawable.pic_place_holder)
                    .into(holder.pic_view);
        }

        @Override
        public int getItemCount() {
            return dataSource.size();
        }

        public void setDataSource(List<MediaStoreData> dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public List<MediaStoreData> getPreloadItems(int position) {
            return dataSource.subList(position,position + SPAN_COUNT);
        }

        @Override
        public GenericRequestBuilder getPreloadRequestBuilder(MediaStoreData item) {
            return Glide.with(MediaCategoryActivity.this).load(item.uri);
        }

    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic_view;
        AppCompatCheckBox pic_check;

        public ViewHolder(View itemView) {
            super(itemView);
            pic_view = (ImageView) itemView.findViewById(R.id.item_pic_view);
            pic_view.getLayoutParams().width = imageWidth;
            pic_check = (AppCompatCheckBox) itemView.findViewById(R.id.item_pic_check);
        }
    }

    private static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final int result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            result = size.x;
        } else {
            result = display.getWidth();
        }
        Log.d(TAG,"screenWidth"+ result);
        return result;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (picRecyclerView != null) {
            int index = ((GridLayoutManager) picRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            outState.putInt(STATE_POSITION_INDEX, index);
        }
    }


}
