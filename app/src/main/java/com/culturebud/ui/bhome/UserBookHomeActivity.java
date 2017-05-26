package com.culturebud.ui.bhome;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.culturebud.bean.UserProfileInfo;
import com.culturebud.contract.UserBookHomeContract;
import com.culturebud.presenter.UserBookHomePresenter;
import com.culturebud.ui.community.CommentDetailActivity;
import com.culturebud.ui.front.BookDetailActivity;
import com.culturebud.ui.front.BookSheetDetailActivity;
import com.culturebud.ui.image.PreviewBigImgActivity;
import com.culturebud.ui.me.FriendsActivity;
import com.culturebud.util.ClassUtil;
import com.culturebud.util.SystemParameterUtil;
import com.culturebud.widget.BookCycleTopView;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by XieWei on 2016/12/29.
 */

@PresenterInject(UserBookHomePresenter.class)
public class UserBookHomeActivity extends BaseActivity<UserBookHomeContract.Presenter>
        implements UserBookHomeContract.View, BookCircleDynamicAdapter.OnItemClickListener, View.OnFocusChangeListener, BaseActivity.OnSoftKeyboardStateChangedListener, BookCycleTopView.BookCycleTopViewListeners {
    private static final String TAG = UserBookHomeActivity.class.getSimpleName();

    private BookCycleTopView topView;

    private RecyclerView rvDynamics;
    private UserProfileInfo user;

    private PopupWindow pwReply;
    private EditText etReplyInput;
    private TextView tvSend;
    private InputMethodManager imm;
    private int screenHeight;
    private int currDeleteType = CommonConst.DeleteType.TYPE_DYNAMIC;
    private Toolbar tlb;
    private AppBarLayout abl;
    private TextView titleview;
    private RelativeLayout concerncontainview;
    private TextView concernview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_book_home);
        presenter.setView(this);

        tlb = obtainViewById(R.id.tlb);
        tlb.setTitle("");
        setSupportActionBar(tlb);

        View view = obtainViewById(R.id.toolbarContent);
        int topMargin = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            int statusBarheight = SystemParameterUtil.getStatusHeight(this);
            topMargin = statusBarheight;
        }
        ClassUtil.setMargins(view, 0, topMargin, 0, 0);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        rvDynamics = obtainViewById(R.id.rv_dynamics);
        topView = obtainViewById(R.id.topview);

        concerncontainview = obtainViewById(R.id.concerncontainview);
        concernview = obtainViewById(R.id.concernview);

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDynamics.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvDynamics.addItemDecoration(divider);
        BookCircleDynamicAdapter adapter = new BookCircleDynamicAdapter();
        adapter.setOnItemClickListener(this);
        rvDynamics.setAdapter(adapter);
        addSoftKeyboardChangedListener(this);
        topView.setTopViewListeners(this);
        initData();

        titleview = obtainViewById(R.id.bctitleview);
        abl = obtainViewById(R.id.abl);
        abl.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            User user = this.user;
            if (user == null) {
                return;
            }
            int appbarHeight = abl.getHeight();

            if (verticalOffset < -appbarHeight / 2) {
                //显示title.
                titleview.setText(user.getNickname());

                if (titleview.getVisibility() != View.VISIBLE) {
                    titleview.setVisibility(View.VISIBLE);
                }
            } else {
                titleview.setVisibility(View.INVISIBLE);
            }
        });

        rvDynamics.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (user.getRelationType() == CommonConst.RelationType.PERSONAL) {
                    //自己的，不显示底部的.
                    return;
                }
                if (Math.abs(dy) < 3)
                    return;
                if (dy > 0) {
                    if (concerncontainview.getVisibility() == View.GONE)
                        return;

                    bottomOutAnimation();
                    concerncontainview.setVisibility(View.GONE);
                } else if (dy < 0) {
                    if (concerncontainview.getVisibility() == View.VISIBLE)
                        return;

                    bottomInAnimation();
                    concerncontainview.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void bottomOutAnimation() {
//        float height = SystemParameterUtil.getScreenHeight();
//        ObjectAnimator anim1 = ObjectAnimator.ofFloat(concerncontainview, "y",
//                height - concerncontainview.getWidth(), height);
//        ObjectAnimator anim2 = ObjectAnimator.ofFloat(concerncontainview, "alpha",
//                1.0f, 0.5f);
//        AnimatorSet animSet = new AnimatorSet();
//        animSet.setDuration(300);
//        animSet.setInterpolator(new LinearInterpolator());
//        //两个动画同时执行
//        animSet.playTogether(anim1, anim2);
//        animSet.start();
    }

    private void bottomInAnimation() {
//        float height = SystemParameterUtil.getScreenHeight();
//        ObjectAnimator anim1 = ObjectAnimator.ofFloat(concerncontainview, "y",
//                height, height - concerncontainview.getWidth());
//        ObjectAnimator anim2 = ObjectAnimator.ofFloat(concerncontainview, "alpha",
//                0.5f, 1f);
//        AnimatorSet animSet = new AnimatorSet();
//        animSet.setDuration(300);
//        animSet.setInterpolator(new LinearInterpolator());
//        //两个动画同时执行
//        animSet.playTogether(anim1, anim2);
//        animSet.start();
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
            case R.id.bcback:
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
            case R.id.concerncontainview: {
                //关注，取消关注.
                handleConcernClick();
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
    public void onUser(UserProfileInfo user) {
        this.user = user;

        topView.setUserInfo(user);

        if (user.getRelationType() == CommonConst.RelationType.PERSONAL) {
            //自己的，不显示底部的.
            concerncontainview.setVisibility(View.GONE);
        }

        updateConcernView();

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

    @Override
    public void onConcern(long concernNum, long fanNum, int status) {
        user.setConcernStatus(status);

        updateConcernView();
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

    private void handleConcernClick() {
        //加关注、已关注、互相关注
        if (user.getConcernStatus() == CommonConst.ConcernStatus.SINGLE_CONCERN_STATUS || user.getConcernStatus() == CommonConst.ConcernStatus.EACH_CONCERN_STATUS) {
            //需要提示确定取消. cancel_concern_notice
            new AlertDialog.Builder(this).setMessage(getString(R.string.cancel_concern_notice))
                    .setPositiveButton(R.string.confirm, (dialog, which) -> {
                        presenter.concern(user.getUserId());
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        } else {
            presenter.concern(user.getUserId());
        }
    }

    private void updateConcernView() {
        int concernresid = 0;
        switch (user.getConcernStatus()) {
            case CommonConst.ConcernStatus.NO_EACHCONCERN_STATUS:
            case CommonConst.ConcernStatus.SINGLE_BECONVERNED_STATUS: {
                concernresid = R.mipmap.concern_add;
                break;
            }
            case CommonConst.ConcernStatus.SINGLE_CONCERN_STATUS: {
                concernresid = R.mipmap.concerned;
                break;
            }
            case CommonConst.ConcernStatus.EACH_CONCERN_STATUS:
                concernresid = R.mipmap.concern_each;
                break;
            default:
                break;
        }
        Drawable drawable = AppCompatResources.getDrawable(this, concernresid);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        concernview.setCompoundDrawables(drawable, null, null, null);

        String concerntitle = CommonConst.getConcernTitle(user.getConcernStatus());
        concernview.setText(concerntitle);
    }

    @Override
    public void onBackgroundClicked(ImageView backgroundImageView) {
        //不能做操作.
    }

    @Override
    public void onAvatarClicked() {
        //不能做操作.
    }

    @Override
    public void onMyFollowedClicked() {
        //我关注的.
        Intent intent = new Intent(this, FriendsActivity.class);
        intent.putExtra("is_concern", true);
        intent.putExtra("user_Id", user.getUserId());
        intent.putExtra("title",getString(R.string.other_concern_pagetitle));
        startActivity(intent);
    }

    @Override
    public void onFollowedClicked() {
        //关注我的
        Intent intent = new Intent(this, FriendsActivity.class);
        intent.putExtra("is_concern", false);
        intent.putExtra("user_Id", user.getUserId());
        intent.putExtra("title",getString(R.string.other_concerned_pagetitle));
        startActivity(intent);
    }
}
