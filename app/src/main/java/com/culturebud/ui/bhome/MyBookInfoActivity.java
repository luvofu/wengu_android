package com.culturebud.ui.bhome;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.StringTagsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.UserBookInfo;
import com.culturebud.contract.MyBookInfoContract;
import com.culturebud.presenter.MyBookInfoPresenter;
import com.culturebud.widget.SettingItemView;
import com.culturebud.widget.TagFlowLayout;

/**
 * Created by XieWei on 2017/2/28.
 */

@PresenterInject(MyBookInfoPresenter.class)
public class MyBookInfoActivity extends BaseActivity<MyBookInfoContract.Presenter> implements MyBookInfoContract.View {
    private RatingBar rbRating;
    private TextView tvBookComment;
    private TagFlowLayout tflTags;
    private Switch schPersonal;
    private SettingItemView sivReadAddress, sivReadTime, sivReadStatus;
    private SettingItemView sivObtainType, sivObtainAddress, sivObtainTime, sivBookType;
    private SettingItemView sivOther;

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
        schPersonal = obtainViewById(R.id.sch_permission);
        schPersonal.setChecked(true);

        sivReadAddress = obtainViewById(R.id.siv_read_address);
        sivReadTime = obtainViewById(R.id.siv_read_time);
        sivReadStatus = obtainViewById(R.id.siv_read_status);

        sivObtainAddress = obtainViewById(R.id.siv_obtain_address);
        sivObtainType = obtainViewById(R.id.siv_obtain_type);
        sivObtainTime = obtainViewById(R.id.siv_obtain_time);
        sivBookType = obtainViewById(R.id.siv_book_type);
        sivOther = obtainViewById(R.id.siv_other);

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
            if (userBookInfo.getReadStatus() == 1) {
                sivReadStatus.setRightInfo("已读");
            } else {
                sivReadStatus.setRightInfo("未读");
            }
            if (userBookInfo.getBookType() == 0) {
                sivBookType.setRightInfo("纸质书");
            } else {
                sivBookType.setRightInfo("电子书");
            }
            switch (userBookInfo.getGetType()) {
                case 0:
                    sivObtainType.setRightInfo("其他");
                    break;
                case 1:
                    sivObtainType.setRightInfo("购买");
                    break;
                case 2:
                    sivObtainType.setRightInfo("赠予");
                    break;
                default:
                    sivObtainType.setRightInfo("其他");
            }
        }
    }
}
