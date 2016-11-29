package com.culturebud.ui.bcircle;

import android.os.Bundle;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.TitleBarActivity;

/**
 * Created by XieWei on 2016/11/2.
 */

public class PublishDynamicActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_dynamic);
        showTitlebar();
        setBgColor(R.color.litter_gray_bg_border);
        setTitle(getString(R.string.publish_dynamic));
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }
}
