/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.drslark.nicefileexplore.fileloader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by zhutiantao on 2015/10/22.
 */
public class PicLoader {
    private PicLoader instance;
    private LruCache<String,Bitmap> imageCache;
    private PicLoader(){
        int cacheMemory = ((int) Runtime.getRuntime().maxMemory())/8;
        imageCache = new LruCache<String,Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public PicLoader getInstance() {
        if (instance == null) {
            synchronized(PicLoader.class) {
                if (instance == null) {
                    instance = new PicLoader();
                }
            }
        }
        return instance;
    }


    private Bitmap getBitmapFromCache(String path) {
        return imageCache.get(path);
    }

}
