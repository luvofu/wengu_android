package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.adapter.MyMsgsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.UserMessage;
import com.culturebud.contract.MyMsgsContract;
import com.culturebud.presenter.MyMsgsPresenter;
import com.culturebud.ui.bhome.DynamicDetailActivity;
import com.culturebud.ui.bhome.MyCreatedBooksActivity;
import com.culturebud.ui.bhome.UserBookHomeActivity;
import com.culturebud.ui.front.BookSheetDetailActivity;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

/**
 * Created by XieWei on 2016/11/16.
 */

@PresenterInject(MyMsgsPresenter.class)
public class MyMsgActivity extends BaseActivity<MyMsgsContract.Presenter>
        implements MyMsgsContract.View, MyMsgsAdapter.OnAgreeListener, MyMsgsAdapter.OnItemClickListener {
    private static final String TAG = MyMsgActivity.class.getSimpleName();
    private RecyclerView rvMyMsgs;
    private int currentPage;
    private int msgGetType = CommonConst.UserMsgGetType.ByTypes;
    private String msgTypes = CommonConst.UserMsgType.TYPE_FRIEND_CONCERN + "|2|3|5|6|8|9|10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_msg);
        showTitlebar();
        presenter.setView(this);
        setTitle(R.string.my_msg);
        rvMyMsgs = obtainViewById(R.id.content_view);

        setNoDataView(obtainViewById(R.id.main_multiplestatusview));

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMyMsgs.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvMyMsgs.addItemDecoration(divider);
        MyMsgsAdapter adapter = new MyMsgsAdapter();
        adapter.setOnAgreeListener(this);
        adapter.setOnItemClickListener(this);
        rvMyMsgs.setAdapter(adapter);

        presenter.getMsgs(msgGetType, null, msgTypes, currentPage = 0);
    }

    @Override
    public void onMsgs(List<UserMessage> msgs) {
        if (currentPage == 0) {
            ((MyMsgsAdapter) rvMyMsgs.getAdapter()).clearData();
        }
        ((MyMsgsAdapter) rvMyMsgs.getAdapter()).addItems(msgs);
    }

    @Override
    public void onAgreeInvite(UserMessage userMessage) {
        userMessage.setDealStatus(CommonConst.MessageDealStatus.STATUS_ACCEPT);
        rvMyMsgs.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onAgree(View v, UserMessage userMessage) {
        presenter.agreeInvite(userMessage);
    }

    @Override
    public void onDeleteUserMessage(UserMessage userMessage, boolean success) {
        if (success) {
            ((MyMsgsAdapter) rvMyMsgs.getAdapter()).deleteItem(userMessage);
        }
    }

    @Override
    public void onItemClick(int position, View v, UserMessage userMessage, int operaType) {
        switch (operaType) {
            case 0://user home
                long userId = userMessage.getSendUserId();
                Intent intent = new Intent(this, UserBookHomeActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
                break;
            case 1://del user msg
                presenter.deleteUserMessage(userMessage);
                break;
            case 2://msg obj detail
                switch (userMessage.getMessageType()) {
                    case CommonConst.UserMsgType.TYPE_GOOD_DYNAMIC:
                    case CommonConst.UserMsgType.TYPE_CIRCLE_REPLY:
                        intent = new Intent(this, DynamicDetailActivity.class);
                        intent.putExtra("dynamic_id", userMessage.getMsgLinkId());
                        startActivity(intent);
                        break;
                    case CommonConst.UserMsgType.TYPE_COLLECT_BOOKSHEET:
                        intent = new Intent(this, BookSheetDetailActivity.class);
                        intent.putExtra("sheetId", userMessage.getMsgLinkId());
                        startActivity(intent);
                        break;
                    case CommonConst.UserMsgType.TYPE_BOOK_CHECK:
                        intent = new Intent(this, MyCreatedBooksActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        userId = userMessage.getSendUserId();
                        intent = new Intent(this, UserBookHomeActivity.class);
                        intent.putExtra("user_id", userId);
                        startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onRetryData() {
        presenter.getMsgs(msgGetType, null, msgTypes, currentPage = 0);
    }

    @Override
    protected void onBack() {
        super.onBack();

        finish();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

}
