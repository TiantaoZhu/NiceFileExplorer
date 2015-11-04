package com.drslark.nicefileexplore;

import java.lang.ref.SoftReference;

import android.graphics.drawable.Drawable;

/**
 * Created by zhutiantao on 2015/11/4.
 */
public class DrawableHolder {
    private SoftReference<Drawable> drawableSoftReference;
    private String name;

    public DrawableHolder() {
    }

    public DrawableHolder(Drawable drawable, String name) {
        this.drawableSoftReference = new SoftReference<Drawable>(drawable);
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawableSoftReference.get();
    }

    public void setDrawable(
            Drawable drawable) {
        this.drawableSoftReference = new SoftReference<Drawable>(drawable);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
