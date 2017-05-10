package com.culturebud;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by XieWei on 2017/5/8.
 */

@RunWith(AndroidJUnit4.class)
public class TestTxt {

    @Test
    public void testStr() {
        String str[] = "----- pid 10574 at 2017-04-10 17:23:37 -----".split(" ");
        for (String s : str) {
            Log.i(ExampleInstrumentedTest.class.getSimpleName(), ("" + s).trim());
        }

    }
}
