package com.culturebud.ui.image;

import android.os.Bundle;
import android.text.TextUtils;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.widget.ZoomableDraweeView;

/**
 * Created by XieWei on 2017/1/3.
 */

public class PreviewBigImgActivity extends BaseActivity {
    private ZoomableDraweeView zdvImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("img_url");
        if (TextUtils.isEmpty(url)) {
            finish();
            return;
        }
        setContentView(R.layout.preview_big_img);
        zdvImg = obtainViewById(R.id.zdv_img);
        zdvImg.setImageURI(url);
        zdvImg.setOnClickListener(() -> {
            finish();
        });
    }
}
