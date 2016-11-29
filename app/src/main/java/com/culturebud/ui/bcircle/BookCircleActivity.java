package com.culturebud.ui.bcircle;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.culturebud.BaseActivity;
import com.culturebud.R;

/**
 * Created by XieWei on 2016/11/20.
 */

public class BookCircleActivity extends BaseActivity {
    private FrameLayout flContainer;
    private BookCircleFragment bcFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_circle_activity);
        flContainer = obtainViewById(R.id.fl_container);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        bcFragment = new BookCircleFragment();
        ft.replace(R.id.fl_container, bcFragment, "book_circle");
        ft.hide(bcFragment);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.show(bcFragment);
        ft.commit();
    }
}
