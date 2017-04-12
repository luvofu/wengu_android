package com.culturebud.ui.bcircle;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.adapter.BookCircleDynamicAdapter;
import com.culturebud.adapter.RelationMeBookCircleDynamicAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.BookCircleDynamicRelationMe;
import com.culturebud.bean.DynamicReply;
import com.culturebud.bean.User;
import com.culturebud.contract.MyDynamicsContract;
import com.culturebud.presenter.MyDynamicsPresenter;
import com.culturebud.ui.bhome.DynamicDetailActivity;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.List;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_PHOTO_CROP;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_SELECT_USER;

/**
 * Created by XieWei on 2017/1/12.
 */

@PresenterInject(MyDynamicsPresenter.class)
public class MyDynamicActivity extends BaseActivity<MyDynamicsContract.Presenter>
        implements MyDynamicsContract.View, BookCircleDynamicAdapter.OnItemClickListener, View.OnFocusChangeListener,
        BaseActivity.OnSoftKeyboardStateChangedListener {
    private RecyclerView rvDynamics;
    private BookCircleDynamicAdapter adapter;
    private RelationMeBookCircleDynamicAdapter rmbcdAdapter;
    private int type;

    private PopupWindow pwReply;
    private EditText etReplyInput;
    private TextView tvSend;
    private InputMethodManager imm;
    private int screenHeight;
    private BookCircleDynamic currClickBcd;
    private DynamicReply currClickDr;
    private int currDeleteType = CommonConst.DeleteType.TYPE_DYNAMIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dynamics);
        presenter.setView(this);
        showTitlebar();
        showBack();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        rvDynamics = obtainViewById(R.id.rv_my_dynamics);
        rvDynamics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvDynamics.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL));

        rvDynamics.setOnScrollListener(listener);
        addSoftKeyboardChangedListener(this);

        initPopupWindow();
        initData();

    }

    private void initData() {
        type = getIntent().getIntExtra("type", 0);//0表示我发布的 1表示与我相关的
        if (type == 0) {
            adapter = new BookCircleDynamicAdapter();
            adapter.setOnItemClickListener(this);
            rvDynamics.setAdapter(adapter);
            setTitle(R.string.my_published_dynamic);
            presenter.myPublished(0);
        } else {
            rmbcdAdapter = new RelationMeBookCircleDynamicAdapter();
            rmbcdAdapter.setOnItemClickListener((v, type1, bcd, dy) -> {
                currClickBcd = null;
                currClickDr = null;
                switch (type1) {
                    case RelationMeBookCircleDynamicAdapter.ONCLICK_TYPE_DYNAMIC:
                    case RelationMeBookCircleDynamicAdapter.ONCLICK_TYPE_BOOK:
                    case RelationMeBookCircleDynamicAdapter.ONCLICK_TYPE_BOOK_SHEET:
                    case RelationMeBookCircleDynamicAdapter.ONCLICK_TYPE_FACE:
                    case RelationMeBookCircleDynamicAdapter.ONCLICK_TYPE_IMG:
                    case RelationMeBookCircleDynamicAdapter.ONCLICK_TYPE_SHORT_COMMENT:
                        Intent intent = new Intent(this, DynamicDetailActivity.class);
                        intent.putExtra("dynamic_id", bcd.getDynamicId());
                        startActivity(intent);
                        break;
                    case RelationMeBookCircleDynamicAdapter.ONCLICK_TYPE_REPLY:
//                        currClickBcd = bcd;
//                        currDeleteType = CommonConst.DeleteType.TYPE_DYNAMIC;
//                        showPop();
//                        etReplyInput.setHint("");
//                        break;
                    case RelationMeBookCircleDynamicAdapter.ONCLICK_TYPE_REPLY_REPLY:
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
                }
            });
            rvDynamics.setAdapter(rmbcdAdapter);
            setTitle(R.string.related_me);
            presenter.myRelations(0);
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
        View dv = getWindow().getDecorView();
        pwReply.showAtLocation(dv, Gravity.NO_GRAVITY, 0, dv.getHeight() / 2);
        etReplyInput.setFocusable(true);
        etReplyInput.requestFocus();
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
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

    @Override
    public void onDynamics(List<BookCircleDynamic> dynamics) {
        if (dynamics == null || dynamics.isEmpty()) {
            Log.d("bCircle", "没有更多了");
            return;
        }
        loading = false;
        if (adapter != null) {
            if (currentPage == 0) {
                adapter.clearData();
            }
            adapter.addItems(dynamics);
        }
    }

    @Override
    public void onRelations(List<BookCircleDynamicRelationMe> dynamics) {
        if (dynamics == null || dynamics.isEmpty()) {
            Log.d("bCircle", "没有更多了");
            return;
        }
        loading = false;
        if (rmbcdAdapter != null) {
            if (currentPage == 0) {
                rmbcdAdapter.clearData();
            }
            rmbcdAdapter.addItems(dynamics);
        }
    }

    @Override
    public void onDynamicReply(DynamicReply reply) {

    }

    @Override
    public void onItemClick(View v, int type, BookCircleDynamic bcd, DynamicReply dy) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_USER: {
                if (RESULT_OK == resultCode && data.hasExtra("user")) {
                    User user = new Gson().fromJson(data.getStringExtra("user"), User.class);
                    etReplyInput.setText(etReplyInput.getText() + " @" + user.getNickname() + " ");
                    etReplyInput.setSelection(etReplyInput.getText().length());
                }
                break;
            }
//            case REQUEST_CODE_PHOTO_CROP:
//                if (resultCode == RESULT_OK) {
//                    presenter.uploadBgImg(photoUri, true);
//                }
//                break;
        }
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
            if (dy > 0) {
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();
                int total = recyclerView.getLayoutManager().getItemCount();
                if (dy > 0 && (lastPosition + 1 >= total) && !loading) {
                    loading = true;
                    if (type == 0) {
                        presenter.myPublished(++currentPage);
                    } else {
                        presenter.myRelations(++currentPage);
                    }
                }
            } else {

            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (pwReply.isShowing()) {
                imm.hideSoftInputFromWindow(etReplyInput.getWindowToken(), 0);
                pwReply.dismiss();
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
}
