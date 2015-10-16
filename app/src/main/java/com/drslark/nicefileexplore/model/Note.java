/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.drslark.nicefileexplore.model;

import java.util.Date;

/**
 * Created by zhutiantao on 2015/10/14.
 */
public class Note {
    public static final int RED_LEVER = 3;
    public static final int YELLOW_LEVER = 2;
    public static final int GREEN_LEVER = 1;
    private int id;
    private String title;
    private String content;
    private int level;
    private boolean star;
    private Date createDate;
    private Date lastModifiedDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (level<GREEN_LEVER || level >RED_LEVER) {
            throw new RuntimeException("level must in [1-3]");
        }
        this.level = level;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }
}
