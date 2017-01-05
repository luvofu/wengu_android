package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.CommonConst.LinkType;
import com.culturebud.R;
import com.culturebud.adapter.DynamicDetailCommentAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.contract.DynamicDetailContract;
import com.culturebud.presenter.DynamicDetailPresenter;
import com.culturebud.widget.RecyclerViewDivider;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2017/1/3.
 */

@PresenterInject(DynamicDetailPresenter.class)
public class DynamicDetailActivity extends BaseActivity<DynamicDetailContract.Presenter> implements DynamicDetailContract.View {
    private static final String TAG = DynamicDetailActivity.class.getSimpleName();
    private BookCircleDynamic bcd;
    private SimpleDraweeView sdvFace;
    private TextView tvNick, tvContent;
    private ViewStub vsImg, vsLinkedType;
    private SimpleDraweeView sdvImg;
    private TextView tvCreateTime, tvThumbNum, tvReplyNum;
    private RecyclerView rvReplies;

    private SimpleDraweeView sdvBookCover;
    private TextView tvBookTitle;

    private SimpleDraweeView sdvSheetCover;
    private TextView tvSheet;

    private TextView tvCommentContent, tvCommunityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_detail);
        presenter.setView(this);
        sdvFace = obtainViewById(R.id.sdv_face);
        tvNick = obtainViewById(R.id.tv_nick_name);
        tvContent = obtainViewById(R.id.tv_content);
        tvCreateTime = obtainViewById(R.id.tv_create_time);
        tvThumbNum = obtainViewById(R.id.tv_good_num);
        tvReplyNum = obtainViewById(R.id.tv_reply_num);

        vsImg = obtainViewById(R.id.vs_image);
        vsLinkedType = obtainViewById(R.id.vs_type_holder);

        rvReplies = obtainViewById(R.id.rv_replies);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvReplies.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvReplies.addItemDecoration(divider);
        DynamicDetailCommentAdapter adapter = new DynamicDetailCommentAdapter();
        rvReplies.setAdapter(adapter);

        showTitlebar();
        showBack();
        setTitle("动态详情");
        initData();
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

    private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd kk:mm", Locale.getDefault());

    private void showDynamic() {
        if (bcd != null) {
            sdvFace.setImageURI(bcd.getAvatar());
            tvNick.setText(bcd.getNickname());
            if (!TextUtils.isEmpty(bcd.getContent())) {
                tvContent.setText(bcd.getContent());
            }
            tvCreateTime.setText(sdf.format(new Date(bcd.getCreatedTime())));
            tvThumbNum.setText("" + bcd.getGoodNum());
            tvReplyNum.setText("" + bcd.getReplyNum());
            if (!TextUtils.isEmpty(bcd.getImage())) {
                if (sdvImg == null) {
                    vsImg.setLayoutResource(R.layout.book_circle_item_img);
                    View view = vsImg.inflate();
                    sdvImg = (SimpleDraweeView) view.findViewById(R.id.sdv_img);
                }
                sdvImg.setImageURI(bcd.getImage());
            }
            switch (bcd.getLinkType()) {
                case LinkType.TYPE_COMMON:
                    break;
                case LinkType.TYPE_BOOK:
                    infalteBookView();
                    if (!TextUtils.isEmpty(bcd.getBookCover())) {
                        sdvBookCover.setImageURI(bcd.getBookCover());
                    }
                    if (!TextUtils.isEmpty(bcd.getTitle())) {
                        tvBookTitle.setText(bcd.getTitle());
                    }
                    break;
                case LinkType.TYPE_BOOK_SHEET:
                    infalteBookSheetView();
                    if (!TextUtils.isEmpty(bcd.getBookSheetCover())) {
                        sdvSheetCover.setImageURI(bcd.getBookSheetCover());
                    }
                    if (!TextUtils.isEmpty(bcd.getName())) {
                        tvSheet.setText(bcd.getName());
                    }
                    break;
                case LinkType.TYPE_COMMENT:
                    infalteCommentView();
                    String cnick = bcd.getCommentNickname();
                    if (TextUtils.isEmpty(cnick)) {
                        cnick = "";
                    }
                    String cconten = bcd.getCommentContent();
                    if (TextUtils.isEmpty(cconten)) {
                        cconten = "";
                    }
                    setCommentNickAndContent(cnick, cconten);
                    if (!TextUtils.isEmpty(bcd.getCommunityTitle())) {
                        tvCommunityTitle.setText(bcd.getCommunityTitle());
                    }
                    break;
                case LinkType.TYPE_DELETED:
                    inflateDeletedView();
                    break;
            }
        }
    }

    public void setCommentNickAndContent(CharSequence nick, CharSequence content) {
        SpannableString ss = new SpannableString(nick + "：" + content);
        ss.setSpan(new ForegroundColorSpan(BaseApp.getInstance()
                        .getResources().getColor(R.color.front_hot_font)),
                0, nick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvCommentContent.setText(ss);
    }

    public void inflateDeletedView() {
        vsLinkedType.setLayoutResource(R.layout.book_circle_item_deleted);
        View view = vsLinkedType.inflate();
    }

    public void infalteCommentView() {
        vsLinkedType.setLayoutResource(R.layout.book_circle_item_comment);
        View view = vsLinkedType.inflate();
        tvCommentContent = (TextView) view.findViewById(R.id.tv_comment);
        tvCommunityTitle = (TextView) view.findViewById(R.id.tv_community_title);
    }

    public void infalteBookView() {
        vsLinkedType.setLayoutResource(R.layout.book_circle_item_book);
        View view = vsLinkedType.inflate();
        sdvBookCover = (SimpleDraweeView) view.findViewById(R.id.sdv_book_cover);
        tvBookTitle = (TextView) view.findViewById(R.id.tv_type_book);
    }

    public void infalteBookSheetView() {
        vsLinkedType.setLayoutResource(R.layout.book_circle_item_sheet);
        View view = vsLinkedType.inflate();
        sdvSheetCover = (SimpleDraweeView) view.findViewById(R.id.sdv_book_sheet_cover);
        tvSheet = (TextView) view.findViewById(R.id.tv_type_sheet);
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onDynamic(BookCircleDynamic dynamic) {
        showDynamic();
        if (dynamic.getDynamicReplies() == null) {
            return;
        }
        presenter.processReplies(dynamic.getDynamicReplies());
    }

    @Override
    public void onReplies(List<DynamicReply> replies) {
        ((DynamicDetailCommentAdapter) rvReplies.getAdapter()).addItems(replies);
    }
}
