package com.culturebud.ui.community;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.CommonConst.CommentOperaType;
import com.culturebud.CommonConst.DeleteType;
import com.culturebud.R;
import com.culturebud.adapter.CommentDetailAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Comment;
import com.culturebud.bean.CommentReply;
import com.culturebud.contract.CommentDetailContract;
import com.culturebud.presenter.CommentDetailPresenter;
import com.google.gson.Gson;

import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by XieWei on 2016/11/9.
 */

@PresenterInject(CommentDetailPresenter.class)
public class CommentDetailActivity extends BaseActivity<CommentDetailContract.Presenter>
        implements CommentDetailContract.View, CommentDetailAdapter.OnCommentOperaListener,
        CommentDetailAdapter.OnItemClickListener, BaseActivity.OnSoftKeyboardStateChangedListener, View.OnFocusChangeListener {
    private static final String TAG = CommentDetailActivity.class.getSimpleName();
    private RecyclerView rvDetail;
    private InputMethodManager imm;
    private TextView tvToReply;
    private EditText etReplyInput;
    private TextView tvSend;
    private PopupWindow pwReply;
    private int screenHeight;
    private int position;

    private BottomSheetDialog delDialog;
    private TextView tvDialogMsg, tvDel, tvCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_detail);
        presenter.setView(this);
        showTitlebar();
        setTitle("短评详情");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        rvDetail = obtainViewById(R.id.rv_replies);
        tvToReply = obtainViewById(R.id.tv_to_reply);
        initList();
        initData();
        initPopupWindow();
        initDelDialog();
        addSoftKeyboardChangedListener(this);
        setListeners();
    }

    private void setListeners() {
        tvToReply.setOnClickListener(this);
        tvSend.setOnClickListener(this);
    }

    private void initList() {
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDetail.setLayoutManager(llm);
        CommentDetailAdapter adapter = new CommentDetailAdapter();
        adapter.setOnThumbListener(this);
        adapter.setOnItemClickListener(this);
        rvDetail.setAdapter(adapter);
    }

    private void initPopupWindow() {
        pwReply = new PopupWindow(this, null, R.style.PopupWindow);
        View view = getLayoutInflater().inflate(R.layout.reply_input_pop, null);
        etReplyInput = obtainViewById(view, R.id.et_reply_input);
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
    }

    private void initDelDialog() {
        delDialog = new BottomSheetDialog(this);
        delDialog.setContentView(R.layout.bottom_sheet_dialog);
        delDialog.setCancelable(true);
        delDialog.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);
        tvDialogMsg = (TextView) delDialog.getWindow().findViewById(R.id.tv_opera_content);
        tvDel = (TextView) delDialog.getWindow().findViewById(R.id.tv_del);
        tvCancel = (TextView) delDialog.getWindow().findViewById(R.id.tv_cancel);
        tvDialogMsg.setText("删除此条回复？删除后将不可恢复！");
        tvDel.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        String commentJson = intent.getStringExtra("comment");
        if (!TextUtils.isEmpty(commentJson)) {
            Gson gson = new Gson();
            Comment comment = gson.fromJson(commentJson, Comment.class);
            ((CommentDetailAdapter) rvDetail.getAdapter()).setComment(comment);
            presenter.getReplies(comment.getCommentId(), 0);
        }
    }

    @Override
    public void onReplies(List<CommentReply> replies) {
        ((CommentDetailAdapter) rvDetail.getAdapter()).addReplies(replies);
    }

    @Override
    public void onReply(CommentReply reply) {
        ((CommentDetailAdapter) rvDetail.getAdapter()).addReply(reply);
    }

    @Override
    public void onThumbUp(boolean res) {
        ((CommentDetailAdapter) rvDetail.getAdapter()).setGood(res);
    }

    @Override
    public void onDelReply(boolean opera, int deleteType, long deleteObjId) {
        if (opera) {
            switch (deleteType) {
                case DeleteType.TYPE_COMMENT:
                    finish();
                    break;
                case DeleteType.TYPE_COMMENT_REPLY:
                    ((CommentDetailAdapter) rvDetail.getAdapter()).delReply(deleteObjId);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_to_reply:
                showPop();
                etReplyInput.setHint("客官，说点什么吧...");
                break;
            case R.id.tv_send:
                String content = etReplyInput.getText().toString();
                CommentDetailAdapter adapter = ((CommentDetailAdapter) rvDetail.getAdapter());
                Comment comment = adapter.getComment();
                pwReply.dismiss();
                etReplyInput.setText("");
                if (position > 0) {
                    CommentReply reply = adapter.getItem(position);
                    presenter.addReply(comment.getCommentId(), 1, reply.getReplyId(), content);
                } else {
                    presenter.addReply(comment.getCommentId(), 0, comment.getCommentId(), content);
                }
                break;
            case R.id.tv_del://删除回复
                presenter.delReply(DeleteType.TYPE_COMMENT_REPLY, Long.valueOf(tvDel.getTag().toString()));
                delDialog.dismiss();
                break;
            case R.id.tv_cancel:
                delDialog.dismiss();
                break;
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onCommentOpera(View v, int type, Comment comment) {
        switch (type) {
            case CommentOperaType.TYPE_THUMB_UP://点赞
                presenter.thumbUp(comment.getCommentId());
                break;
            case CommentOperaType.TYPE_REPLY://评论
                showPop();
                etReplyInput.setHint("客官，说点什么吧...");
                break;
            case CommentOperaType.TYPE_SHARE://分享
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();

                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle(comment.getTitle());
                // text是分享文本，所有平台都需要这个字段
                oks.setText(comment.getTitle());
                // url仅在微信（包括好友和朋友圈）中使用
                oks.setUrl("http://sharesdk.cn");

                // 启动分享GUI
                oks.show(this);
                break;
            case CommentOperaType.TYPE_DELETE://删除（仅限 userId 是自己）
                presenter.delReply(DeleteType.TYPE_COMMENT, comment.getCommentId());
                break;
        }
    }

    @Override
    public void onItemClick(View v, int position, Comment comment, CommentReply reply) {
        this.position = position;
        switch (position) {
            case 0://回复 comment
                etReplyInput.setHint("客官，说点什么吧...");
                break;
            default://回复 commentReply
                if (BaseApp.getInstance().isMe(reply.getUserId())) {//删除
                    tvDel.setTag(reply.getReplyId());
                    delDialog.show();
                    return;
                }
                etReplyInput.setHint("回复：" + reply.getNickname());
                break;
        }
        showPop();
    }

    private void showPop() {
        View dv = getWindow().getDecorView();
        pwReply.showAtLocation(dv, Gravity.NO_GRAVITY, 0, dv.getHeight() / 2);
        etReplyInput.setFocusable(true);
        etReplyInput.requestFocus();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (pwReply.isShowing()) {
                pwReply.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        removeSoftKeyboardChangedListener(this);
        imm.hideSoftInputFromWindow(etReplyInput.getWindowToken(), 0);
        super.onDestroy();
    }

    @Override
    public void OnSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight) {
        if (isKeyBoardShow) {
            pwReply.update(0, screenHeight - (keyboardHeight + pwReply.getContentView().getMeasuredHeight()),
                    pwReply.getWidth(), pwReply.getHeight(), true);
        } else {
            pwReply.update(0, screenHeight - pwReply.getContentView().getMeasuredHeight(),
                    pwReply.getWidth(), pwReply.getHeight(), true);
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
}
