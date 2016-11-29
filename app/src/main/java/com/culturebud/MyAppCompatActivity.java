package com.culturebud;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by XieWei on 2016/11/1.
 */

public class MyAppCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected <T extends View> T obtainViewById(@IdRes int resId) {
        return (T) findViewById(resId);
    }

    protected <T extends View> T obtainViewById(View parent, @IdRes int resId) {
        return (T) parent.findViewById(resId);
    }
}
