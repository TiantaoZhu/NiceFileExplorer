package com.drslark.nicefileexplore.widget;

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * This class is from the v7 samples of the Android SDK. It's not by me!
 * <p/>
 * See the license above for details.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;


    private Drawable mDivider;

    public DividerItemDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            int left = child.getRight() + params.rightMargin;
            int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
            left = child.getLeft() - params.leftMargin;
            right = child.getRight() + params.rightMargin;
            top = child.getBottom() + params.bottomMargin;
            bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }

    }

//	public void drawVertical(Canvas c, RecyclerView parent)
//	{
//		final int left = parent.getPaddingLeft();
//		final int right = parent.getWidth() - parent.getPaddingRight();
//
//		final int childCount = parent.getChildCount();
//
//		for (int i = 0; i < childCount; i++)
//		{
//			final View child = parent.getChildAt(i);
//			RecyclerView v = new RecyclerView(
//					parent.getContext());
//			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
//					.getLayoutParams();
//			final int top = child.getBottom() + params.bottomMargin;
//			final int bottom = top + mDivider.getIntrinsicHeight();
//			mDivider.setBounds(left, top, right, bottom);
//			mDivider.draw(c);
//		}
//	}
//
//	public void drawHorizontal(Canvas c, RecyclerView parent)
//	{
//
//		GridLayoutManager grid = (GridLayoutManager) parent.getLayoutManager();
//
//
//	}

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition,
                               RecyclerView parent) {
        outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
    }
}
