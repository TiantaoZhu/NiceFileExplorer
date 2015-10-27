package com.drslark.nicefileexplore;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.test.InstrumentationTestCase;

/**
 * Created by zhutiantao on 2015/10/26.
 */
public class MergeSortTest extends InstrumentationTestCase {
    public void testMergeSort() {

        List<Integer> a = Arrays.asList(4,6,7,9,12);
        List<Integer> b = Arrays.asList(1,2,5,8,10);
        List<Integer> result = FileCategoryHelper.mergeSort(a, b, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return lhs.compareTo(rhs);
            }
        });
        boolean success = true;
        for ( int  i = 0 ; i< result.size() -1;i++) {
            if ( result.get(i) > result.get(i+1)) {
                success = false;
                break;
            }
        }
        assertEquals(true,success);
    }

}
