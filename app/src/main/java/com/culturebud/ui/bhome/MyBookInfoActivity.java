package com.culturebud.ui.bhome;

import android.content.Intent;
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
import com.culturebud.ui.me.GeneralEditorActivity;
import com.culturebud.widget.SettingItemView;
import com.culturebud.widget.TagFlowLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_OBTAIN_PLACE;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_READ_PLACE;

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
        switch (v.getId()) {
            case R.id.siv_read_address:
                editAddress(REQUEST_CODE_EDIT_READ_PLACE);
                break;
            case R.id.siv_obtain_address:
                editAddress(REQUEST_CODE_EDIT_OBTAIN_PLACE);
                break;
            case R.id.siv_read_time:

                break;
            case R.id.siv_read_status:

                break;
            case R.id.siv_obtain_type:

                break;

            case R.id.siv_obtain_time:

                break;
            case R.id.siv_book_type:

                break;
        }
    }

    private void editAddress(int requestCode) {
        Intent intent = new Intent(this, GeneralEditorActivity.class);
        String content = null;
        String title = "";
        switch (requestCode) {
            case REQUEST_CODE_EDIT_READ_PLACE:
                content = sivReadAddress.getInfo();
                title = getString(R.string.read_place);
                break;
            case REQUEST_CODE_EDIT_OBTAIN_PLACE:
                content = sivObtainAddress.getInfo();
                title = getString(R.string.obtain_place);
                break;
        }
        if (content != null) {
            intent.putExtra("content", content);
        }
        intent.putExtra("title", title);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());

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

            if (userBookInfo.getPermission() == 0) {
                schPersonal.setChecked(false);
            } else {
                schPersonal.setChecked(true);
            }

            if (!TextUtils.isEmpty(userBookInfo.getReadPlace())) {
                sivReadAddress.setRightInfo(userBookInfo.getReadPlace());
            }
            if (userBookInfo.getReadTime() != 0) {
                sivReadTime.setRightInfo(sdf.format(new Date(userBookInfo.getReadTime())));
            }
            if (userBookInfo.getReadStatus() == 1) {
                sivReadStatus.setRightInfo("已读");
            } else {
                sivReadStatus.setRightInfo("未读");
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
            if (!TextUtils.isEmpty(userBookInfo.getGetPlace())) {
                sivObtainAddress.setRightInfo(userBookInfo.getGetPlace());
            }
            if (userBookInfo.getGetTime() != 0) {
                sivObtainTime.setRightInfo(sdf.format(new Date(userBookInfo.getGetTime())));
            }
            if (userBookInfo.getBookType() == 0) {
                sivBookType.setRightInfo("纸质书");
            } else {
                sivBookType.setRightInfo("电子书");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_EDIT_READ_PLACE:
                if (resultCode == RESULT_OK) {
                    String content = data.getStringExtra("content");
                    sivReadAddress.setRightInfo(content);
                    //TODO 向服务器提交修改
                }
                break;
            case REQUEST_CODE_EDIT_OBTAIN_PLACE:
                if (resultCode == RESULT_OK) {
                    String content = data.getStringExtra("content");
                    sivObtainAddress.setRightInfo(content);
                    //TODO 向服务器提交修改

                }
                break;
        }
    }
}
