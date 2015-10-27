package com.drslark.nicefileexplore;

import java.util.List;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.drslark.nicefileexplore.model.MediaStoreData;
import com.drslark.nicefileexplore.widget.TitleController;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by zhutiantao on 2015/10/26.
 */
public class PhotoViewActivity extends TitleControlBaseActivity {
    public static final String CURRENT_POSITION = "photoview_current_position";

    public static void actionShow(Context context, int position) {
        Intent i = new Intent(context, PhotoViewActivity.class);
        i.putExtra(CURRENT_POSITION, position);
        context.startActivity(i);
    }

    private int currentPosition;
    private List<MediaStoreData> allPics;
    private ViewPager pager;
    private TitleController controller;
    private TextView mainTitle;
    private String showTitle;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPosition = getIntent().getIntExtra(CURRENT_POSITION, 0);
        allPics = (List<MediaStoreData>) MiddleRole.getInstance().getData();
        setContentView(R.layout.activity_photo_view);
        pager = (ViewPager) findViewById(R.id.pic_view_pager);
        pager.setAdapter(new PhotoPagerAdapter());
        pager.setCurrentItem(currentPosition);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        controller = getTitleController();
        mainTitle = controller.getMainTitle();
        showTitle = currentPosition + "/" + allPics.size();
        mainTitle.setText(showTitle);
        controller.getSearchIcon().setVisibility(View.GONE);
        controller.getSearchIcon().setImageBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.actionbar_more_icon));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);
    }

    private class PhotoPagerAdapter extends PagerAdapter {
        LayoutInflater inflater;

        PhotoPagerAdapter() {
            inflater = PhotoViewActivity.this.getLayoutInflater();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final MediaStoreData data = allPics.get(position);
            PhotoView photoView;
            View v = inflater.inflate(R.layout.item_photo_view, container, false);
            photoView = (PhotoView) (v.findViewById(R.id
                    .item_photoview));
            //  photoView = new PhotoView(PhotoViewActivity.this);
            ((ViewGroup) v).removeAllViews();
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            DrawableTypeRequest<Uri> request = Glide.with(PhotoViewActivity.this).load(data.uri);
            request.asBitmap().fitCenter().into(photoView);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return allPics.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

    }
}
