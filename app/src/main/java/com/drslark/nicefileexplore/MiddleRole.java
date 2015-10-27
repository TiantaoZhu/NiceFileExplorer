package com.drslark.nicefileexplore;

/**
 * Created by zhutiantao on 2015/10/26.
 */
public class MiddleRole {
    private Object data;
    private Class clazz;
    private static MiddleRole instance;
    private MiddleRole(){}
    public static MiddleRole getInstance() {
        if (instance == null) {
            synchronized(MiddleRole.class) {
                if(instance == null) {
                    instance = new MiddleRole();
                }
            }
        }
        return instance;
    }
//    public <C extends Class,T extends C> void setData(C cls,T data) {
//        clazz = cls;
//        this.data = data;
//    }
//
//    public <T> T getData() {
//        return (T)data;
//    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
