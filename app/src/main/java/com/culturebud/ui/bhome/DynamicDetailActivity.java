package com.culturebud.ui.bhome;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.adapter.DynamicDetailCommentAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.bean.User;
import com.culturebud.contract.DynamicDetailContract;
import com.culturebud.presenter.DynamicDetailPresenter;
import com.culturebud.ui.search.SelectUserActivity;
import com.culturebud.util.WidgetUtil;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by XieWei on 2017/1/3.
 */

@PresenterInject(DynamicDetailPresenter.class)
public class DynamicDetailActivity extends BaseActivity<DynamicDetailContract.Presenter>
        implements DynamicDetailContract.View, DynamicDetailCommentAdapter.OnItemClickListener,
        View.OnFocusChangeListener, BaseActivity.OnSoftKeyboardStateChangedListener {
    private static final String TAG = DynamicDetailActivity.class.getSimpleName();
    private BookCircleDynamic bcd;
    private RecyclerView rvReplies;

    private PopupWindow pwReply;
    private EditText etReplyInput;
    private TextView tvSend;
    private InputMethodManager imm;
    private int screenHeight;

    private BottomSheetDialog deleteDialog;
    private TextView tvContent, tvDel, tvCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_detail);
        presenter.setView(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        rvReplies = obtainViewById(R.id.rv_replies);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvReplies.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvReplies.addItemDecoration(divider);
        DynamicDetailCommentAdapter adapter = new DynamicDetailCommentAdapter();
        adapter.setOnItemClickListener(this);
        rvReplies.setAdapter(adapter);

        showTitlebar();
        showBack();
        setTitle(R.string.dynamic_detail);
        initData();
        addSoftKeyboardChangedListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        String dynamic = intent.getStringExtra("dynamic");
        if (!TextUtils.isEmpty(dynamic)) {
            bcd = new Gson().fromJson(dynamic, BookCircleDynamic.class);
            onDynamic(bcd);
            return;
        }
        long dynamicId = intent.getLongExtra("dynamic_id", -1);
        if (dynamicId == -1) {
            finish();
        }
        presenter.dynamicDetail(dynamicId);
    }

    private void initPopupWindow() {
        if (pwReply == null) {
            pwReply = new PopupWindow(this, null, R.style.PopupWindow);
            View view = getLayoutInflater().inflate(R.layout.reply_input_pop, null);
            etReplyInput = obtainViewById(view, R.id.et_reply_input);
            etReplyInput.setMinLines(3);
            tvSend = obtainViewById(view, R.id.tv_send);
            pwReply.setContentView(view);
            pwReply.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            pwReply.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            pwReply.setOutsideTouchable(true);
            pwReply.setFocusable(true);
            pwReply.setInputMethodMode(PopupWindow.INPUT_METHOD_FROM_FOCUSABLE);
            pwReply.setSoftInputMode(PopupWindow.INPUT_METHOD_FROM_FOCUSABLE);
            pwReply.setBackgroundDrawable(new BitmapDrawable());
            etReplyInput.setOnFocusChangeListener(this);
            tvSend.setOnClickListener(this);
            obtainViewById(view, R.id.iv_at_friend).setOnClickListener(this);
        }
    }

    private void showPop() {
        initPopupWindow();
        View dv = getWindow().getDecorView();
        pwReply.showAtLocation(dv, Gravity.NO_GRAVITY, 0, dv.getHeight() / 2);
        etReplyInput.setFocusable(true);
        etReplyInput.requestFocus();
    }

    private void initDeleteDialog() {
        if (deleteDialog == null) {
            deleteDialog = new BottomSheetDialog(this);
            deleteDialog.setContentView(R.layout.bottom_sheet_dialog);
            deleteDialog.setCancelable(true);
            deleteDialog.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet)
                    .setBackgroundResource(android.R.color.transparent);
            tvContent = (TextView) deleteDialog.getWindow().findViewById(R.id.tv_opera_content);
            tvDel = (TextView) deleteDialog.getWindow().findViewById(R.id.tv_del);
            tvCancel = (TextView) deleteDialog.getWindow().findViewById(R.id.tv_cancel);
            tvContent.setText("删除此条回复？删除后将不可恢复");
            tvContent.setGravity(Gravity.CENTER);
            WidgetUtil.setRawTextSize(tvContent, getResources().getDimensionPixelSize(R.dimen.dialog_opera_font_size));
            tvCancel.setTextColor(Color.BLUE);
            tvDel.setOnClickListener(v -> {
                if (hasParent) {
                    presenter.deleteReplyReply(currClickBcd.getDynamicId(), root.getReplyId(), currClickDr.getReplyId());
                } else {
                    presenter.deleteDynamicOrReply(currDeleteType, currClickBcd.getDynamicId(),
                            currClickDr == null ? -1 : currClickDr.getReplyId());
                }
                deleteDialog.dismiss();
            });
            tvCancel.setOnClickListener(v -> {
                deleteDialog.dismiss();
            });
        }
    }

    private void showDeleteDialog(String content) {
        initDeleteDialog();
        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        }
        deleteDialog.show();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_at_friend: {
                Intent intent = new Intent(this, SelectUserActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_USER);
                break;
            }
            case R.id.tv_send: {
                presenter.reply(currClickDr == null ? CommonConst.DynamicReplyType.TYPE_DYNAMIC
                                : CommonConst.DynamicReplyType.TYPE_DYNAMIC,
                        currClickBcd.getDynamicId(), currClickDr == null ? -1
                                : currClickDr.getReplyId(), etReplyInput.getText().toString());
                imm.hideSoftInputFromWindow(etReplyInput.getWindowToken(), 0);
                pwReply.dismiss();
                etReplyInput.setText("");
            }
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onDynamic(BookCircleDynamic dynamic) {
        if (dynamic == null) {
            return;
        }
        ((DynamicDetailCommentAdapter) rvReplies.getAdapter()).setDynamic(dynamic);
        if (dynamic.getDynamicReplies() == null) {
            return;
        }
        presenter.processReplies(dynamic.getDynamicReplies());
    }

    @Override
    public void onReplies(List<DynamicReply> replies) {
        ((DynamicDetailCommentAdapter) rvReplies.getAdapter()).addItems(replies);
    }

    @Override
    public void onThumbUp(long dynamicId, boolean result) {
        ((DynamicDetailCommentAdapter) rvReplies.getAdapter()).onThumbUp(dynamicId, result);
    }

    @Override
    public void onDeleteReply(long dynamicId, long replyId, boolean result) {
        if (result) {
            ((DynamicDetailCommentAdapter) rvReplies.getAdapter()).deleteItem(replyId);
        }
    }

    @Override
    public void onDeleteReplyReply(long dynamicId, long replyId, long deleteReplyId, boolean result) {
        if (result) {
            ((DynamicDetailCommentAdapter) rvReplies.getAdapter()).deleteItemItem(replyId, deleteReplyId);
        }
    }

    @Override
    public void onDeleteDynamic(long dynamicId, boolean result) {

    }

    @Override
    public void onReply(long dynamicId, long replyId, DynamicReply dynamicReply) {
        ((DynamicDetailCommentAdapter) rvReplies.getAdapter()).addItemItem(replyId, dynamicReply);
    }

    @Override
    public void onReplyDynamic(long dynamicId, DynamicReply dynamicReply) {
        ((DynamicDetailCommentAdapter) rvReplies.getAdapter()).addItem(dynamicReply);
    }

    private BookCircleDynamic currClickBcd;
    private DynamicReply currClickDr, root;
    private int currDeleteType = CommonConst.DeleteType.TYPE_DYNAMIC;
    private boolean hasParent;

    @Override
    public void onItemClick(View v, int type, BookCircleDynamic bcd, DynamicReply dynamicReply, DynamicReply root) {
        hasParent = false;
        currClickBcd = null;
        currClickDr = null;
        this.root = null;

        switch (type) {
            case DynamicDetailCommentAdapter.ITEM_CLICK_TYPE_THUMBUP:
                presenter.thumbUP(bcd.getDynamicId());
                break;
            case DynamicDetailCommentAdapter.ITEM_CLICK_TYPE_REPLY_DYNAMIC:
                currClickBcd = bcd;
                currClickDr = dynamicReply;
                showPop();
                etReplyInput.setHint("");
                break;
            case DynamicDetailCommentAdapter.ITEM_CLICK_TYPE_REPLY_REPLY:
                currClickBcd = bcd;
                currClickDr = dynamicReply;
                currDeleteType = CommonConst.DeleteType.TYPE_DYNAMIC_REPLY;
                if (BaseApp.getInstance().getUser().getUserId() == dynamicReply.getUserId()) {
                    showDeleteDialog("删除此条回复？删除后将不可恢复");
                } else {
                    showPop();
                    etReplyInput.setHint("回复：" + currClickDr.getNickname());
                }
                break;
            case DynamicDetailCommentAdapter.ITEM_CLICK_TYPE_REPLY_REPLY_REPLY:
                hasParent = true;
                this.root = root;
                currClickBcd = bcd;
                currClickDr = dynamicReply;
                currDeleteType = CommonConst.DeleteType.TYPE_DYNAMIC_REPLY;
                if (BaseApp.getInstance().getUser().getUserId() == dynamicReply.getUserId()) {
                    showDeleteDialog("删除此条回复？删除后将不可恢复");
                } else {
                    showPop();
                    etReplyInput.setHint("回复：" + currClickDr.getNickname());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_USER: {
                if (resultCode == RESULT_OK) {
                    String userJson = data.getStringExtra("user");
                    if (!TextUtils.isEmpty(userJson)) {
                        User user = new Gson().fromJson(userJson, User.class);
                        if (user != null) {
                            etReplyInput.setText(etReplyInput.getText().toString() + " @" + user.getNickname() + " ");
                        }
                    }
                }
                break;
            }
        }
    }

    private Handler handler = new Handler();

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == etReplyInput && hasFocus) {
            handler.postDelayed(() -> {
                runOnUiThread(() -> {
                    imm.showSoftInput(etReplyInput, InputMethodManager.SHOW_FORCED);
                });
            }, 100);
        }
    }

    @Override
    public void onSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight) {
        if (isKeyBoardShow) {
            pwReply.update(0, screenHeight - (keyboardHeight + pwReply.getContentView()
                            .getMeasuredHeight()),
                    pwReply.getWidth(), pwReply.getHeight(), true);
        } else {
            pwReply.update(0, screenHeight - pwReply.getContentView().getMeasuredHeight(),
                    pwReply.getWidth(), pwReply.getHeight(), true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        removeSoftKeyboardChangedListener(this);
        super.onDestroy();
    }
}
