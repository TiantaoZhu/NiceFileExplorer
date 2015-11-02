/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.drslark.nicefileexplore.utils;

import android.util.SparseArray;

import java.io.File;
import java.util.Comparator;

public class FileSortHelper {


    public static final int NAME = 9090;
    public static final int SIZE = 9091;
    public static final int DATE = 9092;
    public static final int TYPE = 9093;

    private int mSort;


    private boolean mFileFirst;
    private SparseArray<Comparator> mComparatorList = new SparseArray<>();

    //  private HashMap<SortMethod, Comparator> mComparatorList = new HashMap<SortMethod, Comparator>();

    public FileSortHelper() {
        mSort = NAME;
        mComparatorList.put(NAME, cmpName);
        mComparatorList.put(SIZE, cmpSize);
        mComparatorList.put(DATE, cmpDate);
        mComparatorList.put(TYPE, cmpType);
    }

    public void setSortMethog(int s) {
        mSort = s;
    }

    public int getSortMethod() {
        return mSort;
    }

    public void setFileFirst(boolean f) {
        mFileFirst = f;
    }

    public Comparator getComparator() {
        return mComparatorList.get(mSort);
    }

    private abstract class FileComparator implements Comparator<File> {

        @Override
        public int compare(File object1, File object2) {
            if (object1.isDirectory() == object2.isDirectory()) {
                return doCompare(object1, object2);
            }

            if (mFileFirst) {
                // the files are listed before the dirs
                return (object1.isDirectory() ? 1 : -1);
            } else {
                // the dir-s are listed before the files
                return object1.isDirectory() ? -1 : 1;
            }
        }

        protected abstract int doCompare(File object1, File object2);
    }

    private Comparator cmpName = new FileComparator() {
        @Override
        public int doCompare(File object1, File object2) {
            return object1.getName().compareToIgnoreCase(object2.getName());
        }
    };

    private Comparator cmpSize = new FileComparator() {
        @Override
        public int doCompare(File object1, File object2) {
            return longToCompareInt(object1.length() - object2.length());
        }
    };

    private Comparator cmpDate = new FileComparator() {
        @Override
        public int doCompare(File object1, File object2) {
            return longToCompareInt(object2.lastModified() - object1.lastModified());
        }
    };

    private int longToCompareInt(long result) {
        return result > 0 ? 1 : (result < 0 ? -1 : 0);
    }

    private Comparator cmpType = new FileComparator() {
        @Override
        public int doCompare(File object1, File object2) {
            int result = Util.getExtFromFilename(object1.getName()).compareToIgnoreCase(
                    Util.getExtFromFilename(object2.getName()));
            if (result != 0)
                return result;

            return Util.getNameFromFilename(object1.getName()).compareToIgnoreCase(
                    Util.getNameFromFilename(object2.getName()));
        }
    };
}
