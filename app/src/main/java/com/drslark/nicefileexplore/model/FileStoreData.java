package com.drslark.nicefileexplore.model;

import android.net.Uri;

/**
 * Created by zhutiantao on 2015/11/2.
 */
public class FileStoreData {

    public final long rowId;
    public final String name;
    public final long dateAdd;
    public final long dateModified;
    public final String mimeType;
    public final long fileSize;
    public final Uri uri;

    public FileStoreData(Uri uri, long rowId, String name, long dateAdd, long dateModified, String mimeType,
                         long fileSize) {
        this.uri = uri;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.dateModified = dateModified;
        this.dateAdd = dateAdd;
        this.name = name;
        this.rowId = rowId;
    }
}
