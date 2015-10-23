/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.drslark.nicefileexplore.utils;

import java.util.Comparator;
import java.util.HashMap;

import com.drslark.nicefileexplore.model.FileInfo;

import android.util.SparseArray;

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

    private abstract class FileComparator implements Comparator<FileInfo> {

        @Override
        public int compare(FileInfo object1, FileInfo object2) {
            if (object1.isDir() == object2.isDir()) {
                return doCompare(object1, object2);
            }

            if (mFileFirst) {
                // the files are listed before the dirs
                return (object1.isDir() ? 1 : -1);
            } else {
                // the dir-s are listed before the files
                return object1.isDir() ? -1 : 1;
            }
        }

        protected abstract int doCompare(FileInfo object1, FileInfo object2);
    }

    private Comparator cmpName = new FileComparator() {
        @Override
        public int doCompare(FileInfo object1, FileInfo object2) {
            return object1.getName().compareToIgnoreCase(object2.getName());
        }
    };

    private Comparator cmpSize = new FileComparator() {
        @Override
        public int doCompare(FileInfo object1, FileInfo object2) {
            return longToCompareInt(object1.getFileSize() - object2.getFileSize());
        }
    };

    private Comparator cmpDate = new FileComparator() {
        @Override
        public int doCompare(FileInfo object1, FileInfo object2) {
            return longToCompareInt(object2.getModefiedDate() - object1.getModefiedDate());
        }
    };

    private int longToCompareInt(long result) {
        return result > 0 ? 1 : (result < 0 ? -1 : 0);
    }

    private Comparator cmpType = new FileComparator() {
        @Override
        public int doCompare(FileInfo object1, FileInfo object2) {
            int result = Util.getExtFromFilename(object1.getName()).compareToIgnoreCase(
                    Util.getExtFromFilename(object2.getName()));
            if (result != 0)
                return result;

            return Util.getNameFromFilename(object1.getName()).compareToIgnoreCase(
                    Util.getNameFromFilename(object2.getName()));
        }
    };
}
