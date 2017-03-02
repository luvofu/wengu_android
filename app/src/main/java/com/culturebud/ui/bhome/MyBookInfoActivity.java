package com.culturebud.ui.bhome;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.StringTagsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.UserBookInfo;
import com.culturebud.contract.MyBookInfoContract;
import com.culturebud.presenter.MyBookInfoPresenter;
import com.culturebud.widget.TagFlowLayout;

/**
 * Created by XieWei on 2017/2/28.
 */

@PresenterInject(MyBookInfoPresenter.class)
public class MyBookInfoActivity extends BaseActivity<MyBookInfoContract.Presenter> implements MyBookInfoContract.View {
    private RatingBar rbRating;
    private TextView tvBookComment;
    private TagFlowLayout tflTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_book_info);
        presenter.setView(this);

        rbRating = obtainViewById(R.id.rb_rating);
        LayerDrawable ld = (LayerDrawable) rbRating.getProgressDrawable();
        ld.getDrawable(0).setColorFilter(getResources().getColor(R.color.light_font_black), PorterDuff.Mode.SRC_ATOP);
        ld.getDrawable(1).setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        ld.getDrawable(2).setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);

        tvBookComment = obtainViewById(R.id.tv_book_comment_content);
        tflTags = obtainViewById(R.id.tfl_tags);

        showTitlebar();
        setTitle(R.string.my_book_info);
        showBack();
        long bookId = getIntent().getLongExtra("userBookId", -1);
        if (bookId == -1) {
            finish();
            return;
        }
        presenter.myBookInfo(bookId);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onBookInfo(UserBookInfo userBookInfo) {
        if (userBookInfo != null) {
            rbRating.setRating(userBookInfo.getRating() / 2F);
            if (!TextUtils.isEmpty(userBookInfo.getRemark())) {
                tvBookComment.setText(userBookInfo.getRemark());
            }
            if (!TextUtils.isEmpty(userBookInfo.getTag())) {
                String[] tags = userBookInfo.getTag().split("\\|");
                if (tags != null) {
                    StringTagsAdapter tagsAdapter = new StringTagsAdapter(tags);
                    tflTags.setAdapter(tagsAdapter);
                }
            }
        }
    }
}
