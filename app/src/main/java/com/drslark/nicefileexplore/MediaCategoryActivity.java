package com.drslark.nicefileexplore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.drslark.nicefileexplore.model.MediaStoreData;
import com.drslark.nicefileexplore.utils.IntentBuilder;
import com.drslark.nicefileexplore.widget.TitleController;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhutiantao on 2015/10/22.
 * 用来显示图片和视频的Activity
 */
public class MediaCategoryActivity extends TitleControlBaseActivity {
    private static final String TAG = MediaCategoryActivity.class.getName();
    public static final String SHOW_WHAT = "show_what";
    public static final int SHOW_PIC = 8888;
    public static final int SHOW_VIDEO = 8889;
    public static final int SPAN_COUNT = 3;
    public static final int GRID_MARGIN = 1;
    private static final String STATE_POSITION_INDEX = "state_position_index";
    public static final String CURRENT_DIR = "CurrentDir";

    private TitleController titleController;
    private FileCategoryHelper fileCategoryHelper;
    private RecyclerView picRecyclerView;
    private PicGridAdapter adapter;

    // 结果是目录名字和其目录下的文件名 1对多 记录所有文件夹下的图片 第一次扫描的时候 建立
    // 后期可以点击强制刷新来扫描
    ArrayMap<String, List<MediaStoreData>> allMediaMap;
    private String currentDir;
    private TextView currentDirTxt;

    private int imageWidth;
    private int[] actualDimensions;
    // 最近的100张多媒体
    private List<MediaStoreData> mediaDataSource;
    private boolean muiltChoiceMode;

    private ProgressDialog dialog;
    private int showWhat;

    private ListView popDirListView;
    private PopupWindow popupWindow;

    public static void actionShow(Context context, int showWhat) {
        Intent intent = new Intent(context, MediaCategoryActivity.class);
        intent.putExtra(SHOW_WHAT, showWhat);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDir = "";
        showWhat = getIntent().getIntExtra(SHOW_WHAT, SHOW_PIC);
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
        allMediaMap = new ArrayMap<>();

        Observable.create(new Observable.OnSubscribe<Map>() {
            @Override
            public void call(Subscriber<? super Map> subscriber) {
                subscriber.onStart();
                mediaDataSource = showWhat == SHOW_PIC
                        ? fileCategoryHelper.queryImages()
                        : fileCategoryHelper.queryVideos();
                for (MediaStoreData m : mediaDataSource) {
                    String parentPath = m.name.substring(0, m.name.lastIndexOf(File.separator));
                    List<MediaStoreData> list = allMediaMap.get(parentPath);
                    if (list == null) {
                        list = new ArrayList<MediaStoreData>();
                        allMediaMap.put(parentPath, list);
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
        picRecyclerView.setAdapter(adapter);
        adapter.setDataSource(mediaDataSource);

        picRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(GRID_MARGIN, GRID_MARGIN, GRID_MARGIN, GRID_MARGIN);
            }
        });

        //        if (savedInstanceState != null) {
        //            int index = savedInstanceState.getInt(STATE_POSITION_INDEX);
        //            picRecyclerView.scrollToPosition(index);
        //        }

        currentDirTxt = (TextView) findViewById(R.id.image_dir);
        currentDirTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(MediaCategoryActivity.this);
                final View popView = inflater.inflate(R.layout.media_popup_window, null);
                popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, currentDirTxt.getTop() -
                        picRecyclerView.getTop() - 100);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
                popupWindow.showAtLocation(currentDirTxt, Gravity.BOTTOM, 0, 0);
                initPopViewListView(popView);

            }
        });

    }

    private void initPopViewListView(View popView) {
        popDirListView = (ListView) popView.findViewById(R.id.pop_media_dirs);
        final List<String> allDirs = new ArrayList<>(allMediaMap.keySet());
        popDirListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return allDirs.size();
            }

            @Override
            public Object getItem(int position) {
                return allDirs.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_popupwindow,parent,false);
                }
                ImageView cover = (ImageView) convertView.findViewById(R.id.dir_cover_img);
                TextView name = (TextView) convertView.findViewById(R.id.dir_name_txt);
                TextView count = (TextView) convertView.findViewById(R.id.dir_count_txt);
                ImageView check = (ImageView) convertView.findViewById(R.id.dir_check_img);
                final String dir = allDirs.get(position);
                check.setVisibility(dir.equals(currentDir)?View.VISIBLE:View.GONE);
                name.setText(dir);
                count.setText(allMediaMap.get(dir).size() + "项");
                Glide.with(MediaCategoryActivity.this)
                        .load(allMediaMap.get(dir).get(0).uri)
                        .centerCrop()
                        .into(cover);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentDir = dir;
                        adapter.setDataSource(allMediaMap.get(dir));
                        adapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }
                });

                
                return convertView;
            }
        });

    }

    private void initTitle() {
        titleController = getTitleController();

        titleController.getMainTitle().setText(showWhat == SHOW_PIC ? "图片" : "视频");
        titleController.getUserIcon().setImageBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.actionbar_more_icon));
    }

    private class PicGridAdapter extends RecyclerView.Adapter<ViewHolder> {
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

                    //                    IntentBuilder.viewFile(MediaCategoryActivity.this,
                    //                            dataSource.get(position).name,
                    //                            dataSource.get(position).mimeType);
                    if (!muiltChoiceMode) {
                        if (showWhat == SHOW_VIDEO) {
                            IntentBuilder.viewFile(MediaCategoryActivity.this,
                                    dataSource.get(position).name,
                                    dataSource.get(position).mimeType);
                        } else {
                            MiddleRole.getInstance().setData(dataSource);
                            PhotoViewActivity.actionShow(MediaCategoryActivity.this, position);
                        }
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

        //        @Override
        //        public List<MediaStoreData> getPreloadItems(int position) {
        //            return dataSource.subList(position,position + SPAN_COUNT);
        //        }
        //
        //        @Override
        //        public GenericRequestBuilder getPreloadRequestBuilder(MediaStoreData item) {
        //            return Glide.with(MediaCategoryActivity.this).load(item.uri);
        //        }

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
        Log.d(TAG, "screenWidth" + result);
        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (picRecyclerView != null) {
            int index = ((GridLayoutManager) picRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            outState.putInt(STATE_POSITION_INDEX, index);
        }
        outState.putString(CURRENT_DIR, currentDir);
    }

}