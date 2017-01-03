package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.bean.BookCircleDynamic;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by XieWei on 2017/1/3.
 */

public class DynamicDetailActivity extends BaseActivity {
    private BookCircleDynamic bcd;
    private SimpleDraweeView sdvFace;
    private TextView tvNick, tvContent;
    private ViewStub vsImg, vsLinkedType;
    private SimpleDraweeView sdvImg;
    private TextView tvCreateTime, tvThumbNum, tvReplyNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_detail);
        sdvFace = obtainViewById(R.id.sdv_face);
        tvNick = obtainViewById(R.id.tv_nick_name);
        tvContent = obtainViewById(R.id.tv_content);
        tvCreateTime = obtainViewById(R.id.tv_create_time);
        tvThumbNum = obtainViewById(R.id.tv_good_num);
        tvReplyNum = obtainViewById(R.id.tv_reply_num);

        vsImg = obtainViewById(R.id.vs_image);
        vsLinkedType = obtainViewById(R.id.vs_type_holder);

        showTitlebar();
        showBack();
        setTitle("动态详情");
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String dynamic = intent.getStringExtra("dynamic");
        if (!TextUtils.isEmpty(dynamic)) {
            bcd = new Gson().fromJson(dynamic, BookCircleDynamic.class);
            showDynamic();
            return;
        }
        String dynamicId = intent.getStringExtra("dynamic_id");
        if (TextUtils.isEmpty(dynamicId)) {
            finish();
        }

    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd kk:mm", Locale.getDefault());

    private void showDynamic() {
        if (bcd != null) {
            sdvFace.setImageURI(bcd.getAvatar());
            tvNick.setText(bcd.getNickname());
            if (!TextUtils.isEmpty(bcd.getContent())) {
                tvContent.setText(bcd.getContent());
            }
            tvCreateTime.setText(sdf.format(new Date(bcd.getCreatedTime())));
            tvThumbNum.setText("" + bcd.getGoodNum());
            tvReplyNum.setText("" + bcd.getReplyNum());
            if (!TextUtils.isEmpty(bcd.getImage())) {
                if (sdvImg == null) {
                    vsImg.setLayoutResource(R.layout.book_circle_item_img);
                    View view = vsImg.inflate();
                    sdvImg = (SimpleDraweeView) view.findViewById(R.id.sdv_img);
                }
                sdvImg.setImageURI(bcd.getImage());
            }
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }
}
