/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.drslark.nicefileexplore.model;


/**
 * Created by zhutiantao on 2015/10/15.
 */
public class User {
    public long id;
    public String name;
    public String accountName;
    public boolean gender;
    public String[] group; // 为文件分组做准备 加密中可见
    public boolean isRoot;
    public String phoneNumber;
    public String email;
}
