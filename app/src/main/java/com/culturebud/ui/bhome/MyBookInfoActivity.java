package com.culturebud.ui.bhome;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
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

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_ADD_BOOK_TAGS;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_BOOK_OTHER_INFO;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_BOOK_RATING;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_OBTAIN_PLACE;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_READ_PLACE;

/**
 * Created by XieWei on 2017/2/28.
 */

@PresenterInject(MyBookInfoPresenter.class)
public class MyBookInfoActivity extends BaseActivity<MyBookInfoContract.Presenter> implements MyBookInfoContract
        .View, OptionsPickerView.OnOptionsSelectListener, TimePickerView.OnTimeSelectListener, CompoundButton
        .OnCheckedChangeListener {
    private RatingBar rbRating;
    private TextView tvBookComment;
    private TagFlowLayout tflTags;
    private Switch schPersonal;
    private SettingItemView sivReadAddress, sivReadTime, sivReadStatus;
    private SettingItemView sivObtainType, sivObtainAddress, sivObtainTime, sivBookType;
    private SettingItemView sivOther;
    private OptionsPickerView<String> singleColOpts;
    private TimePickerView timePicker;
    private String bookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_book_info);
        presenter.setView(this);

        rbRating = obtainViewById(R.id.rb_rating);
        LayerDrawable ld = (LayerDrawable) rbRating.getProgressDrawable();
        ld.getDrawable(0).setColorFilter(getResources().getColor(R.color.font_black_light), PorterDuff.Mode.SRC_ATOP);
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
        schPersonal.setOnCheckedChangeListener(this);

        showTitlebar();
        setTitle(R.string.my_book_info);
        showBack();
        long bookId = getIntent().getLongExtra("userBookId", -1);
        if (bookId == -1) {
            finish();
            return;
        }
        bookTitle = getIntent().getStringExtra("book_title");
        presenter.myBookInfo(bookId);
    }

    private int lastTimeType;

    private void initTimePicker(Date time) {
        if (timePicker == null) {
            timePicker = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
            timePicker.setTime(time);
            timePicker.setOnTimeSelectListener(this);
        }
    }

    private static final int OPTS_TYPE_READ_STATUS = 0;
    private static final int OPTS_TYPE_OBTAIN_TYPES = 1;
    private static final int OPTS_TYPE_BOOK_TYPES = 2;
    private int lastOptsType = -1;

    private void initSingleOptsDialog(int type, int defaultSelect) {
        if (singleColOpts == null) {
            singleColOpts = new OptionsPickerView<>(this);
            singleColOpts.setOnoptionsSelectListener(this);
            singleColOpts.setPicker(getPickers(type));
        } else {
            if (type != lastOptsType) {
                singleColOpts.setPicker(getPickers(type));
            }
        }
        lastOptsType = type;
        singleColOpts.setCyclic(false);
        singleColOpts.setSelectOptions(defaultSelect);
    }

    private ArrayList<String> readStatus;
    private ArrayList<String> obtainTypes;
    private ArrayList<String> bookTypes;

    private ArrayList<String> getPickers(int type) {
        switch (type) {
            case OPTS_TYPE_BOOK_TYPES:
                if (bookTypes == null) {
                    bookTypes = new ArrayList<>(2);
                    bookTypes.add("纸质书");
                    bookTypes.add("电子书");
                }
                return bookTypes;
            case OPTS_TYPE_READ_STATUS:
                if (readStatus == null) {
                    readStatus = new ArrayList<>(3);
                    readStatus.add("已读");
                    readStatus.add("未读");
                    readStatus.add("在读");
                }
                return readStatus;
            case OPTS_TYPE_OBTAIN_TYPES:
                if (obtainTypes == null) {
                    obtainTypes = new ArrayList<>(3);
                    obtainTypes.add("其他");
                    obtainTypes.add("购买");
                    obtainTypes.add("赠予");
                }
                return obtainTypes;
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_rating:
                if (userBookInfo != null) {
                    Intent intent = new Intent(this, EditBookRatingActivity.class);
                    intent.putExtra("book_title", bookTitle);
                    intent.putExtra("rating", userBookInfo.getRating());
                    intent.putExtra("comment", userBookInfo.getRemark());
                    startActivityForResult(intent, REQUEST_CODE_EDIT_BOOK_RATING);
                }
                break;
            case R.id.rl_tags:
                if (userBookInfo != null) {
                    Intent intent = new Intent(this, GeneralAddTagsActivity.class);
                    if (!TextUtils.isEmpty(userBookInfo.getTag())) {
                        intent.putExtra("tag", userBookInfo.getTag());
                    }
                    startActivityForResult(intent, REQUEST_CODE_ADD_BOOK_TAGS);
                }
                break;
            case R.id.siv_read_address:
                editAddress(REQUEST_CODE_EDIT_READ_PLACE);
                break;
            case R.id.siv_obtain_address:
                editAddress(REQUEST_CODE_EDIT_OBTAIN_PLACE);
                break;
            case R.id.siv_read_time:
                if (userBookInfo != null) {
                    lastTimeType = 0;
                    initTimePicker(userBookInfo.getReadTime() == 0 ? new Date()
                            : new Date(userBookInfo.getReadTime()));
                    timePicker.show();
                }
                break;
            case R.id.siv_read_status:
                if (userBookInfo != null) {
                    initSingleOptsDialog(OPTS_TYPE_READ_STATUS, userBookInfo.getReadStatus());
                    singleColOpts.show();
                }
                break;
            case R.id.siv_obtain_type:
                if (userBookInfo != null) {
                    initSingleOptsDialog(OPTS_TYPE_OBTAIN_TYPES, userBookInfo.getGetType());
                    singleColOpts.show();
                    break;
                }

            case R.id.siv_obtain_time:
                if (userBookInfo != null) {
                    lastTimeType = 1;
                    initTimePicker(userBookInfo.getGetTime() == 0 ? new Date()
                            : new Date(userBookInfo.getGetTime()));
                    timePicker.show();
                }
                break;
            case R.id.siv_book_type:
                if (userBookInfo != null) {
                    initSingleOptsDialog(OPTS_TYPE_BOOK_TYPES,
                            userBookInfo.getBookType() == 0 ? 0 : 1);
                    singleColOpts.show();
                }
                break;
            case R.id.siv_other: {
                if (userBookInfo != null) {
                    String content = userBookInfo.getOther();
                    Intent intent = new Intent(this, GeneralEditorActivity.class);
                    intent.putExtra("title", getString(R.string.other));
                    if (!TextUtils.isEmpty(content)) {
                        intent.putExtra("content", content);
                    }
                    intent.putExtra("type", 2);
                    intent.putExtra("content_length", 1000);
                    startActivityForResult(intent, REQUEST_CODE_EDIT_BOOK_OTHER_INFO);
                }
                break;
            }
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

    private UserBookInfo userBookInfo;

    @Override
    public void onBookInfo(UserBookInfo userBookInfo) {
        if (userBookInfo != null) {
            this.userBookInfo = userBookInfo;
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
            switch (userBookInfo.getReadStatus()) {
                case 0:
                    sivReadStatus.setRightInfo("已读");
                    break;
                case 1:
                    sivReadStatus.setRightInfo("未读");
                    break;
                case 2:
                    sivReadStatus.setRightInfo("在读");
                    break;
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
            if (!TextUtils.isEmpty(userBookInfo.getOther())) {
                sivOther.setDesc(userBookInfo.getOther());
            }
        }
    }

    @Override
    public void onAlert(long userBookId, boolean res, int readStatus) {
        if (userBookInfo != null && userBookId == userBookInfo.getUserBookId() && res) {
            userBookInfo.setReadStatus(readStatus);
        }
    }

    @Override
    public void onEditUserBookInfo(long userBookId, Map<String, Object> editContent, boolean res) {
        if (res) {
            Set<String> keys = editContent.keySet();
            for (String key : keys) {
                try {
                    Field field = userBookInfo.getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(userBookInfo, editContent.get(key));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
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
                    Map<String, Object> map = new HashMap<>();
                    map.put("readPlace", content);
                    presenter.editUserBookInfo(userBookInfo.getUserBookId(), map);
                }
                break;
            case REQUEST_CODE_EDIT_OBTAIN_PLACE:
                if (resultCode == RESULT_OK) {
                    String content = data.getStringExtra("content");
                    sivObtainAddress.setRightInfo(content);
                    Map<String, Object> map = new HashMap<>();
                    map.put("getPlace", content);
                    presenter.editUserBookInfo(userBookInfo.getUserBookId(), map);
                }
                break;
            case REQUEST_CODE_ADD_BOOK_TAGS:
                if (resultCode == RESULT_OK) {
                    String tag = data.getStringExtra("tag");
                    StringTagsAdapter tagsAdapter;
                    if (tflTags.getAdapter() != null) {
                        tagsAdapter = (StringTagsAdapter) tflTags.getAdapter();
                    } else {
                        tagsAdapter = new StringTagsAdapter();
                        tflTags.setAdapter(tagsAdapter);
                    }
                    tagsAdapter.clearData();
                    String[] tarr = tag.split("\\|");
                    List<String> ts = new ArrayList<>();
                    for (String s : tarr) {
                        ts.add(s);
                    }
                    tagsAdapter.addTags(ts);
                    Map<String, Object> map = new HashMap<>();
                    map.put("tag", tag);
                    presenter.editUserBookInfo(userBookInfo.getUserBookId(), map);
                }
                break;
            case REQUEST_CODE_EDIT_BOOK_RATING:
                if (resultCode == RESULT_OK) {
                    float rating = data.getFloatExtra("rating", -1);
                    String comment = data.getStringExtra("comment");
                    if (rating != -1 || !TextUtils.isEmpty(comment)) {
                        Map<String, Object> map = new HashMap<>();
                        if (rating != -1) {
                            rbRating.setRating(rating / 2);
                            map.put("rating", rating);
                        }
                        if (!TextUtils.isEmpty(comment)) {
                            tvBookComment.setText(comment);
                            map.put("remark", comment);
                        }
                        presenter.editUserBookInfo(userBookInfo.getUserBookId(), map);
                    }
                }
                break;
            case REQUEST_CODE_EDIT_BOOK_OTHER_INFO: {
                if (resultCode == RESULT_OK) {
                    String content = data.getStringExtra("content");
                    sivOther.setDesc(content);
                    Map<String, Object> map = new HashMap<>();
                    map.put("other", content);
                    presenter.editUserBookInfo(userBookInfo.getUserBookId(), map);
                }
            }
        }
    }

    @Override
    public void onOptionsSelect(int options1, int option2, int options3) {
        switch (lastOptsType) {
            case OPTS_TYPE_BOOK_TYPES:
                if (userBookInfo != null) {
                    sivBookType.setRightInfo(bookTypes.get(options1));
                    Map<String, Object> map = new HashMap<>();
                    map.put("bookType", options1);
                    presenter.editUserBookInfo(userBookInfo.getUserBookId(), map);
                }
                break;
            case OPTS_TYPE_READ_STATUS:
                if (userBookInfo != null) {
                    sivReadStatus.setRightInfo(readStatus.get(options1));
                    presenter.alterBookReadStatus(userBookInfo.getUserBookId(), options1);
                }
                break;
            case OPTS_TYPE_OBTAIN_TYPES:
                if (userBookInfo != null) {
                    sivObtainType.setRightInfo(obtainTypes.get(options1));
                    Map<String, Object> map = new HashMap<>();
                    map.put("getType", options1);
                    presenter.editUserBookInfo(userBookInfo.getUserBookId(), map);
                }
                break;
        }
    }

    @Override
    public void onTimeSelect(Date date) {
        if (userBookInfo != null) {
            if (lastTimeType == 0) {//阅读时间
                sivReadTime.setRightInfo(sdf.format(date));
                Map<String, Object> map = new HashMap<>();
                map.put("readTime", date.getTime());
                presenter.editUserBookInfo(userBookInfo.getUserBookId(), map);
            } else {//获取时间
                sivObtainTime.setRightInfo(sdf.format(date));
                Map<String, Object> map = new HashMap<>();
                map.put("getTime", date.getTime());
                presenter.editUserBookInfo(userBookInfo.getUserBookId(), map);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (userBookInfo == null) {
            return;
        }
        int permission;
        if (isChecked) {
            permission = 2;
        } else {
            permission = 0;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("permission", permission);
        presenter.editUserBookInfo(userBookInfo.getUserBookId(), map);
    }
}
