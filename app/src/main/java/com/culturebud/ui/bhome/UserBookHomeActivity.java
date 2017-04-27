package com.culturebud.ui.bhome;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.culturebud.adapter.BookCircleDynamicAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.bean.User;
import com.culturebud.contract.UserBookHomeContract;
import com.culturebud.presenter.UserBookHomePresenter;
import com.culturebud.ui.community.CommentDetailActivity;
import com.culturebud.ui.front.BookDetailActivity;
import com.culturebud.ui.front.BookSheetDetailActivity;
import com.culturebud.ui.image.PreviewBigImgActivity;
import com.culturebud.widget.RecyclerViewDivider;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by XieWei on 2016/12/29.
 */

@PresenterInject(UserBookHomePresenter.class)
public class UserBookHomeActivity extends BaseActivity<UserBookHomeContract.Presenter>
        implements UserBookHomeContract.View, BookCircleDynamicAdapter.OnItemClickListener, View.OnFocusChangeListener, BaseActivity.OnSoftKeyboardStateChangedListener {
    private static final String TAG = UserBookHomeActivity.class.getSimpleName();
    private SimpleDraweeView sdvBg, sdvFace;
    private TextView tvNick;

    private RecyclerView rvDynamics;
    private User user;

    private PopupWindow pwReply;
    private EditText etReplyInput;
    private TextView tvSend;
    private InputMethodManager imm;
    private int screenHeight;
    private int currDeleteType = CommonConst.DeleteType.TYPE_DYNAMIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_book_home);
        presenter.setView(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        rvDynamics = obtainViewById(R.id.rv_dynamics);
        sdvBg = obtainViewById(R.id.sdv_bg);
        sdvFace = obtainViewById(R.id.sdv_face);
        tvNick = obtainViewById(R.id.tv_nick);

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDynamics.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvDynamics.addItemDecoration(divider);
        BookCircleDynamicAdapter adapter = new BookCircleDynamicAdapter();
        adapter.setOnItemClickListener(this);
        rvDynamics.setAdapter(adapter);
        addSoftKeyboardChangedListener(this);
        initData();
    }

    private void initData() {
        long userId = getIntent().getLongExtra("user_id", -1);
        if (userId == -1) {
            finish();
        } else {
            presenter.getUserProfile(userId);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_book_shelf: {
                if (user == null) {
                    break;
                }
                Intent intent = new Intent(this, CollectedBooksActivity.class);
                intent.putExtra(CollectedBooksActivity.USER_ID_KEY, user.getUserId());
                startActivity(intent);
                break;
            }
            case R.id.tv_note: {
                if (user == null) {
                    break;
                }
                Intent intent = new Intent(this, NotebookActivity.class);
                intent.putExtra("user_id", user.getUserId());
                startActivity(intent);
                break;
            }
            case R.id.tv_book_sheet: {
                if (user == null) {
                    break;
                }
                Intent intent = new Intent(this, BookSheetsActivity.class);
                intent.putExtra("user_id", user.getUserId());
                startActivity(intent);
                break;
            }
            case R.id.tv_send: {
                if (currClickBcd != null) {
                    presenter.replyDynamic(currClickBcd.getDynamicId(),
                            etReplyInput.getText().toString(),
                            currClickDr == null ? CommonConst.DynamicReplyType.TYPE_DYNAMIC : CommonConst
                                    .DynamicReplyType.TYPE_REPLY,
                            currClickDr == null ? -1 : currClickDr.getReplyId());
                    imm.hideSoftInputFromWindow(etReplyInput.getWindowToken(), 0);
                    pwReply.dismiss();
                    etReplyInput.setText("");
                }
                break;
            }
        }
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

    @Override
    public void onUser(User user) {
        this.user = user;
        tvNick.setText(user.getNickname());
        sdvBg.setImageURI(user.getBackground());
        sdvFace.setImageURI(user.getAvatar());
        presenter.getDynamics(user.getUserId(), currPage);
    }

    private int currPage;

    @Override
    public void onDynamics(List<BookCircleDynamic> dynamics) {
        if (currPage == 0) {
            ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).clearData();
        }
        if (dynamics != null && !dynamics.isEmpty()) {
            ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).addItems(dynamics);
        }
    }

    @Override
    public void onThumbUp(long dynamicId, boolean isGood) {
        ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).onThumbUpResult(dynamicId, isGood);
    }

    @Override
    public void onDynamicReply(DynamicReply dynamicReply) {
        BookCircleDynamic bcd = ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).getDynamicById(currClickBcd.getDynamicId());
        if (bcd != null) {
            bcd.getDynamicReplies().add(dynamicReply);
            bcd.setReplyNum(bcd.getReplyNum() + 1);
            int index = ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).getItemIndex(bcd);
            rvDynamics.getAdapter().notifyItemChanged(index);
        }
    }

    private BookCircleDynamic currClickBcd;
    private DynamicReply currClickDr;

    @Override
    public void onItemClick(View v, int type, BookCircleDynamic bcd, DynamicReply dy) {
        currClickBcd = null;
        currClickDr = null;
        switch (type) {
            case BookCircleDynamicAdapter.ONCLICK_TYPE_DYNAMIC: {
                Intent intent = new Intent(this, DynamicDetailActivity.class);
                intent.putExtra("dynamic", new Gson().toJson(bcd));
                startActivity(intent);
                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_BOOK: {
                Intent intent = new Intent(this, BookDetailActivity.class);
                intent.putExtra("bookId", bcd.getLinkId());
                startActivity(intent);
                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_BOOK_SHEET: {
                Intent intent = new Intent(this, BookSheetDetailActivity.class);
                intent.putExtra("sheetId", bcd.getLinkId());
                startActivity(intent);
                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_SHORT_COMMENT: {
                Intent intent = new Intent(this, CommentDetailActivity.class);
                intent.putExtra("commentId", bcd.getLinkId());

                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_IMG: {
                Intent intent = new Intent(this, PreviewBigImgActivity.class);
                intent.putExtra("img_url", bcd.getImage());
                startActivity(intent);
                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_REPLY:
                currClickBcd = bcd;
                currDeleteType = CommonConst.DeleteType.TYPE_DYNAMIC;
                showPop();
                etReplyInput.setHint("");
                break;
            case BookCircleDynamicAdapter.ONCLICK_TYPE_REPLY_REPLY:
                currClickBcd = bcd;
                currClickDr = dy;
                currDeleteType = CommonConst.DeleteType.TYPE_DYNAMIC_REPLY;
                if (BaseApp.getInstance().getUser().getUserId() == dy.getUserId()) {
                    //showDeleteDialog("删除此条回复？删除后将不可恢复");
                } else {
                    showPop();
                    etReplyInput.setHint("回复：" + currClickDr.getNickname());
                }
                break;
            case BookCircleDynamicAdapter.ONCLICK_TYPE_THUMB:
                presenter.thumbUpDynamic(bcd.getDynamicId());
                break;
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
                            .getMeasuredHeight() + getTitleBarHeight()),
                    pwReply.getWidth(), pwReply.getHeight(), true);
        } else {
            pwReply.update(0, screenHeight - pwReply.getContentView().getMeasuredHeight(),
                    pwReply.getWidth(), pwReply.getHeight(), true);
        }
    }
}
