package com.culturebud.ui.bcircle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.CommonConst.DeleteType;
import com.culturebud.CommonConst.DynamicReplyType;
import com.culturebud.R;
import com.culturebud.adapter.BookCircleDynamicAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.bean.User;
import com.culturebud.contract.BookCircleContract;
import com.culturebud.presenter.BookCirclePresenter;
import com.culturebud.ui.bhome.DynamicDetailActivity;
import com.culturebud.ui.bhome.UserBookHomeActivity;
import com.culturebud.ui.community.CommentDetailActivity;
import com.culturebud.ui.front.BookDetailActivity;
import com.culturebud.ui.front.BookSheetDetailActivity;
import com.culturebud.ui.image.PreviewBigImgActivity;
import com.culturebud.ui.me.FriendsActivity;
import com.culturebud.util.ClassUtil;
import com.culturebud.util.SystemParameterUtil;
import com.culturebud.util.WidgetUtil;
import com.culturebud.widget.BookCycleTopView;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.List;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_LOGIN;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_PHOTO_CROP;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_PUBLISH_DYNAMIC;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_SELECT_USER;

/**
 * Created by XieWei on 2016/11/20.
 */

@PresenterInject(BookCirclePresenter.class)
public class BookCircleActivity extends BaseActivity<BookCircleContract.Presenter>
        implements BookCircleContract.View, BookCircleDynamicAdapter.OnItemClickListener,
        View.OnFocusChangeListener, BaseActivity.OnSoftKeyboardStateChangedListener, SwipeRefreshLayout
                .OnRefreshListener, BookCycleTopView.BookCycleTopViewListeners {
    private RecyclerView rvDynamics;
    private AppBarLayout abl;
    private BookCycleTopView bcTopView;
    private PopupWindow pwReply;
    private EditText etReplyInput;
    private TextView tvSend;
    private InputMethodManager imm;
    private int screenHeight;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar tlb;
    private CollapsingToolbarLayout ctl;
    private TextView titleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_circle_activity);
        presenter.setView(this);

        tlb = obtainViewById(R.id.tlb);
        tlb.setTitle("");
        ctl = obtainViewById(R.id.collapsing_toolbar_layout);
        ctl.setTitle("");
        ctl.setExpandedTitleColor(Color.TRANSPARENT);
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

        abl = obtainViewById(R.id.abl);
        bcTopView = obtainViewById(R.id.topview);
        swipeRefreshLayout = obtainViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setProgressViewOffset(true, -20, 100);
        titleview = obtainViewById(R.id.bctitleview);

        rvDynamics = obtainViewById(R.id.rv_content);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDynamics.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        divider.setDividerHeight(1);
        rvDynamics.addItemDecoration(divider);
        BookCircleDynamicAdapter adapter = new BookCircleDynamicAdapter();
        adapter.setOnItemClickListener(this);
        rvDynamics.setAdapter(adapter);
        initPopupWindow();
        setListeners();
        currentPage = 0;
//        presenter.downloadBgImg();
        presenter.loadDynamics(currentPage);
    }

    private BottomSheetDialog deleteDialog;
    private TextView tvContent, tvDel, tvCancel;

    private void showDeleteDialog() {
        initDeleteDialog();
        deleteDialog.show();
    }

    private void showDeleteDialog(String content) {
        initDeleteDialog();
        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        }
        deleteDialog.show();
    }

    private int currDeleteType = DeleteType.TYPE_DYNAMIC;

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
                presenter.deleteDynamicOrReply(currClickBcd.getDynamicId(), currDeleteType,
                        currDeleteType == DeleteType.TYPE_DYNAMIC ? currClickBcd.getDynamicId()
                                : currClickDr.getReplyId());
                deleteDialog.dismiss();
            });
            tvCancel.setOnClickListener(v -> {
                deleteDialog.dismiss();
            });
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

    @Override
    protected void onResume() {
        super.onResume();
        User user = BaseApp.getInstance().getUser();
        if (user != null) {
            bcTopView.setUserInfo(user);
        }
    }

    private void setListeners() {
        bcTopView.setTopViewListeners(this);
        rvDynamics.setOnScrollListener(listener);
        addSoftKeyboardChangedListener(this);

        swipeRefreshLayout.setOnRefreshListener(this);
        abl.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            rvDynamics.setEnabled(verticalOffset == 0);
            swipeRefreshLayout.setEnabled(verticalOffset == 0);

            User user = BaseApp.getInstance().getUser();
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
    }

    private int currentPage;
    private boolean loading = true;
    private RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findLastVisibleItemPosition();
            if (dy > 0) {
                int total = recyclerView.getLayoutManager().getItemCount();
                if (dy > 0 && (lastPosition + 1 >= total) && !loading) {
                    loading = true;
                    presenter.loadDynamics(++currentPage);
                }
            } else {

            }

            swipeRefreshLayout.setEnabled(dy == 0);
        }
    };

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        Intent intent = new Intent(this, PublishDynamicActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PUBLISH_DYNAMIC);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bcback: {
                finish();
                break;
            }
            case R.id.iv_operas: {
                onOptions(v);
                break;
            }
            case R.id.iv_publish: {
                Intent intent = new Intent(this, PublishDynamicActivity.class);
                startActivityForResult(intent, REQUEST_CODE_PUBLISH_DYNAMIC);
                break;
            }
            case R.id.tv_my_publish: {
                Intent intent = new Intent(this, MyDynamicActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            }
            case R.id.tv_related_me: {
                Intent intent = new Intent(this, MyDynamicActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            }
            case R.id.iv_at_friend: {
                Intent intent = new Intent(this, FriendsActivity.class);
                intent.putExtra("opt_type", 1);
                startActivityForResult(intent, REQUEST_CODE_SELECT_USER);
                break;
            }
            case R.id.tv_send: {
                if (currClickBcd != null) {
                    presenter.replyDynamic(currClickBcd.getDynamicId(),
                            etReplyInput.getText().toString(),
                            currClickDr == null ? DynamicReplyType.TYPE_DYNAMIC : DynamicReplyType.TYPE_REPLY,
                            currClickDr == null ? -1 : currClickDr.getReplyId());
                    imm.hideSoftInputFromWindow(etReplyInput.getWindowToken(), 0);
                    pwReply.dismiss();
                    etReplyInput.setText("");
                }
                break;
            }
        }
    }

    @Override
    public void onDynamics(List<BookCircleDynamic> dynamics) {
        swipeRefreshLayout.setRefreshing(false);

        if (dynamics == null || dynamics.isEmpty()) {
            Log.d("bCircle", "没有更多了");
            return;
        }
        loading = false;
        if (currentPage == 0) {
            ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).clearData();
        }
        ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).addItems(dynamics);
    }

    @Override
    public void onBgImg(Bitmap bitmap) {

    }

    @Override
    public void onUploadBgImg(String url) {
        if (!TextUtils.isEmpty(url)) {
            BaseApp.getInstance().getUser().setBackground(url);
            bcTopView.setUserInfo(BaseApp.getInstance().getUser());
//            presenter.downloadBgImg();
        }
    }

    @Override
    public void onThumbUp(long dynamicId, boolean result) {
        ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).onThumbUpResult(dynamicId, result);
    }

    @Override
    public void onDynamicReply(DynamicReply dynamicReply) {
        BookCircleDynamic bcd = ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).getDynamicById(currClickBcd
                .getDynamicId());
        if (bcd != null) {
            bcd.getDynamicReplies().add(dynamicReply);
            bcd.setReplyNum(bcd.getReplyNum() + 1);
            int index = ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).getItemIndex(bcd);
            rvDynamics.getAdapter().notifyItemChanged(index);
        }
    }

    @Override
    public void onDeleteResult(long dynamicId, int deleteType, long deleteObjId, boolean res) {
        BookCircleDynamic bcd = ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).getDynamicById(currClickBcd
                .getDynamicId());
        if (bcd != null && res) {
            if (deleteType == DeleteType.TYPE_DYNAMIC) {
                ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).deleteItem(deleteObjId);
            } else {
                List<DynamicReply> replies = bcd.getDynamicReplies();
                DynamicReply dr = null;
                for (DynamicReply r : replies) {
                    if (r.getReplyId() == deleteObjId) {
                        dr = r;
                        break;
                    }
                }
                replies.remove(dr);
                bcd.setReplyNum(bcd.getReplyNum() - 1);
                int index = ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).getItemIndex(bcd);
                rvDynamics.getAdapter().notifyItemChanged(index);
            }
        }
    }

    private BookCircleDynamic currClickBcd;
    private DynamicReply currClickDr;

    @Override
    public void onItemClick(View v, int type, BookCircleDynamic bcd, DynamicReply dy) {
        currClickBcd = null;
        currClickDr = null;
        switch (type) {
            case BookCircleDynamicAdapter.ONCLICK_TYPE_FACE: {
                Intent intent = new Intent(this, UserBookHomeActivity.class);
                intent.putExtra("user_id", bcd.getUserId());
                startActivity(intent);
                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_DYNAMIC: {
                Intent intent = new Intent(this, DynamicDetailActivity.class);
//                intent.putExtra("dynamic", new Gson().toJson(bcd));
                intent.putExtra("dynamic_id", bcd.getDynamicId());
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
                currDeleteType = DeleteType.TYPE_DYNAMIC;
                showPop();
                etReplyInput.setHint("");
                break;
            case BookCircleDynamicAdapter.ONCLICK_TYPE_REPLY_REPLY:
                currClickBcd = bcd;
                currClickDr = dy;
                currDeleteType = DeleteType.TYPE_DYNAMIC_REPLY;
                if (BaseApp.getInstance().getUser().getUserId() == dy.getUserId()) {
                    showDeleteDialog("删除此条回复？删除后将不可恢复");
                } else {
                    showPop();
                    etReplyInput.setHint("回复：" + currClickDr.getNickname());
                }
                break;
            case BookCircleDynamicAdapter.ONCLICK_TYPE_THUMB:
                presenter.thumbUp(bcd.getDynamicId());
                break;
            case BookCircleDynamicAdapter.ONCLICK_TYPE_DELETE_DYNAMIC:
                currClickBcd = bcd;
                currDeleteType = DeleteType.TYPE_DYNAMIC;
                showDelDynamicDlg(v);
                break;
        }
    }

    private PopupWindow pwDeleteDynamic;
    private TextView tvDeleteDynamic;

    private void initDelDynamicDlg() {
        if (pwDeleteDynamic == null) {
            pwDeleteDynamic = new PopupWindow(this, null, R.style.PopupWindow);
            tvDeleteDynamic = new TextView(this);
            tvDeleteDynamic.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                    .LayoutParams.WRAP_CONTENT));
            tvDeleteDynamic.setBackgroundResource(R.drawable.popup_menu_bg);
            tvDeleteDynamic.setText(R.string.delete);
            tvDeleteDynamic.setGravity(Gravity.CENTER);
            WidgetUtil.setRawTextSize(tvDeleteDynamic, getResources().getDimensionPixelSize(R.dimen.font_default));
            tvDeleteDynamic.setTextColor(getResources().getColor(R.color.title_font_white));
            pwDeleteDynamic.setContentView(tvDeleteDynamic);
            pwDeleteDynamic.setBackgroundDrawable(new BitmapDrawable());
            pwDeleteDynamic.setOutsideTouchable(true);
            tvDeleteDynamic.setOnClickListener(v -> {
                pwDeleteDynamic.dismiss();
                presenter.deleteDynamicOrReply(currClickBcd.getDynamicId(), currDeleteType, currClickBcd.getDynamicId
                        ());
            });
            pwDeleteDynamic.setWidth(getResources().getDimensionPixelSize(R.dimen.dynamic_del_pw_width));
            pwDeleteDynamic.setHeight(getResources().getDimensionPixelSize(R.dimen.dynamic_del_pw_height));
        }
    }

    public void showDelDynamicDlg(View view) {
        if (view == null) {
            return;
        }
        initDelDynamicDlg();
        if (pwDeleteDynamic.isShowing()) {
            pwDeleteDynamic.dismiss();
        }
        pwDeleteDynamic.showAsDropDown(view, -(view.getWidth() * 2), -view.getHeight());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                if (RESULT_OK == resultCode) {
                    presenter.loadDynamics(0);
                    presenter.downloadBgImg();
                }
                break;
            case REQUEST_CODE_SELECT_USER: {
                if (RESULT_OK == resultCode && data.hasExtra("user")) {
                    User user = new Gson().fromJson(data.getStringExtra("user"), User.class);
                    etReplyInput.setText(etReplyInput.getText() + " @" + user.getNickname() + " ");
                    etReplyInput.setSelection(etReplyInput.getText().length());
                }
                break;
            }
            case REQUEST_CODE_PHOTO_CROP:
                if (resultCode == RESULT_OK) {
                    presenter.uploadBgImg(photoUri, true);
                }
                break;
            case REQUEST_CODE_PUBLISH_DYNAMIC:
                if (resultCode == RESULT_OK) {
                    currentPage = 0;
                    presenter.loadDynamics(currentPage);
                }
                break;
        }
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
                imm.hideSoftInputFromWindow(etReplyInput.getWindowToken(), 0);
                pwReply.dismiss();
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);

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

    @Override
    public void onSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight) {
        if (isKeyBoardShow) {
            pwReply.update(0, screenHeight - (keyboardHeight + pwReply.getContentView()
                            .getMeasuredHeight()/* + getTitleBarHeight()*/),
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

    @Override
    public void onRefresh() {
        currentPage = 0;
        presenter.downloadBgImg();
        presenter.loadDynamics(currentPage);
    }

    @Override
    public void onBackgroundClicked(ImageView backgroundImageView) {
        aspectX = 350;
        aspectY = 254;
        outX = 0;
        outY = 0;
        showPhotoDialog();
    }

    @Override
    public void onAvatarClicked() {
        //暂时不实现换头像.
    }

    @Override
    public void onMyFollowedClicked() {
        //我关注的.
        Intent intent = new Intent(this, FriendsActivity.class);
        intent.putExtra("is_concern", true);
        intent.putExtra("user_Id", BaseApp.getInstance().getUser().getUserId());
        intent.putExtra("title", getString(R.string.my_concern_pagetitle));
        startActivity(intent);
    }

    @Override
    public void onFollowedClicked() {
        //关注我的
        Intent intent = new Intent(this, FriendsActivity.class);
        intent.putExtra("is_concern", false);
        intent.putExtra("user_Id", BaseApp.getInstance().getUser().getUserId());
        intent.putExtra("title", getString(R.string.my_concerned_pagetitle));
        startActivity(intent);
    }
}
