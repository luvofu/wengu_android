package com.culturebud;

import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by XieWei on 2017/2/17.
 */

public class XwGe<T extends Comparable & Serializable & Parcelable> {

    @FunctionalInterface
    interface Run {
        void run(String msg);
    }

    interface Converter {
        int convert(String from);
    }

    public void tmp(Run run) {
        run.run("msg");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void test() {
        tmp((msg) -> System.out.print(msg));

        Run run = msg -> System.out.print(msg);
        run.run("sldgk");

        Converter c = Integer::valueOf;
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        tlr.nextInt(5);
    }
}
