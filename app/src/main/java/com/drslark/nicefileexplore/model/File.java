/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.drslark.nicefileexplore.model;


/**
 * Created by zhutiantao on 2015/10/14.
 */
public class File {
    private int id;
    private String name;
    private String relativePath;
    private String parentPath;
    protected String absolutePath;
    private String[] tags;
    private String blongApp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getBlongApp() {
        return blongApp;
    }

    public void setBlongApp(String blongApp) {
        this.blongApp = blongApp;
    }
}
