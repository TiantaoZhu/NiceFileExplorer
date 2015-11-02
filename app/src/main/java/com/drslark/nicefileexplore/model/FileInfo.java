/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.drslark.nicefileexplore.model;


/**
 * Created by zhutiantao on 2015/10/14.
 */
public class FileInfo {
    public int id;
    public String name;
    public String relativePath;
    public String parentPath;
    public String absolutePath;
    public String[] tags;
    public String blongApp;
    public long fileSize;
    public long modefiedDate;
    public boolean isDir;
}