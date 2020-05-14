package com.tt52.host;

import android.support.test.runner.AndroidJUnit4;

import com.tt52.moduleevent.utils.AppUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by liaohailiang on 2019/3/20.
 */
@RunWith(AndroidJUnit4.class)
public class AppUtilsTest {

    @Test
    public void testGetApplicationContext() throws Exception {
        Assert.assertNotNull(AppUtils.getApp());
    }
}