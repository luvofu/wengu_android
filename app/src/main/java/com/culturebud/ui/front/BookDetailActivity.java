package com.culturebud.ui.front;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.MyBookSheetAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookDetail;
import com.culturebud.bean.BookSheet;
import com.culturebud.contract.BookDetailContract;
import com.culturebud.presenter.BookDetailPresenter;
import com.culturebud.ui.community.BookCommunityActivity;
import com.culturebud.widget.RecyclerViewDivider;
import com.culturebud.widget.SettingItemView;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2016/11/7.
 */

@PresenterInject(BookDetailPresenter.class)
public class BookDetailActivity extends BaseActivity<BookDetailContract.Presenter> implements BookDetailContract
        .View, MyBookSheetAdapter.OnItemClickListener {
    private SimpleDraweeView sdvCover;
    private TextView tvBookName, tvRating, tvPublisherInfo;
    private RatingBar rbRating;
    private TextView tvCollect, tvAdd, tvCommunity;
    private TextView tvSummary, tvAuthorInfo;
    private ImageView ivOpenSummary, ivOpenAuthorInfo;
    private SettingItemView sivMore;
    private RelativeLayout rlDetailTop;

    private BottomSheetDialog bsdDialog;
    private RecyclerView rvBookSheets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail);
        showTitlebar();
        setBackGroundColor(getResources().getColor(R.color.light_gray));
        presenter.setView(this);
        initView();
        setListeners();
        initData();
    }

    private void initView() {
        sdvCover = obtainViewById(R.id.sdv_book_sheet_cover);
        rlDetailTop = obtainViewById(R.id.rl_book_detail_top);
        tvBookName = obtainViewById(R.id.tv_book_name);
        tvRating = obtainViewById(R.id.tv_rating_num);
        tvPublisherInfo = obtainViewById(R.id.tv_publisher_info);
        rbRating = obtainViewById(R.id.rb_rating);
        tvCollect = obtainViewById(R.id.tv_collect);
        tvAdd = obtainViewById(R.id.tv_add);
        tvCommunity = obtainViewById(R.id.tv_community);
        tvSummary = obtainViewById(R.id.tv_summary);
        tvAuthorInfo = obtainViewById(R.id.tv_author_info);
        ivOpenSummary = obtainViewById(R.id.iv_open_summary);
        ivOpenAuthorInfo = obtainViewById(R.id.iv_open_info);
        sivMore = obtainViewById(R.id.siv_more);

        LayerDrawable ld = (LayerDrawable) rbRating.getProgressDrawable();
        ld.getDrawable(0).setColorFilter(getResources().getColor(R.color.font_black_light), PorterDuff.Mode.SRC_ATOP);
        ld.getDrawable(1).setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        ld.getDrawable(2).setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);

    }

    private void setListeners() {
        tvCollect.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        tvCommunity.setOnClickListener(this);
        ivOpenSummary.setOnClickListener(this);
        ivOpenAuthorInfo.setOnClickListener(this);
        sivMore.setOnClickListener(this);
    }

    private void initData() {
        long bookId = getIntent().getLongExtra("bookId", -1);
        if (bookId <= 0) {
            return;
        }
        presenter.getBookDetail(bookId);
    }

    private void initBottomDialog() {
        if (bsdDialog == null) {
            bsdDialog = new BottomSheetDialog(this);
            bsdDialog.setContentView(R.layout.add_to_book_sheet);
            rvBookSheets = (RecyclerView) bsdDialog.getWindow().findViewById(R.id.rv_book_sheets);
            LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvBookSheets.setLayoutManager(llm);
            RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
            rvBookSheets.addItemDecoration(divider);
            MyBookSheetAdapter mbsAdapter = new MyBookSheetAdapter();
            mbsAdapter.setOnItemClickListener(this);
            rvBookSheets.setAdapter(mbsAdapter);
            bsdDialog.setCancelable(true);
        }
    }

    private void showBottomDialog() {
        initBottomDialog();
        if (!bsdDialog.isShowing()) {
            bsdDialog.show();
        }
    }

    private void hideBottomDialog() {
        if (bsdDialog != null && bsdDialog.isShowing()) {
            bsdDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_open_summary: {
                Object tag = ivOpenSummary.getTag();
                if ("1".equals(tag)) {
                    tvSummary.setMaxLines(Integer.MAX_VALUE);
                    ivOpenSummary.setTag("0");
                    ivOpenSummary.setImageResource(R.mipmap.arrow_up);
                } else {
                    tvSummary.setMaxLines(2);
                    ivOpenSummary.setTag("1");
                    ivOpenSummary.setImageResource(R.mipmap.arrow_down);
                }
                break;
            }
            case R.id.iv_open_info: {
                Object tag = ivOpenAuthorInfo.getTag();
                if ("1".equals(tag)) {
                    tvAuthorInfo.setMaxLines(Integer.MAX_VALUE);
                    ivOpenAuthorInfo.setTag("0");
                    ivOpenAuthorInfo.setImageResource(R.mipmap.arrow_up);
                } else {
                    tvAuthorInfo.setMaxLines(2);
                    ivOpenAuthorInfo.setTag("1");
                    ivOpenAuthorInfo.setImageResource(R.mipmap.arrow_down);
                }
                break;
            }
            case R.id.siv_more: {
                if (bookDetail != null) {
                    Intent intent = new Intent(this, BookBaseInfoActivity.class);
                    intent.putExtra("bookName", bookDetail.getTitle());
                    intent.putExtra("author", bookDetail.getAuthor());
                    intent.putExtra("translator", bookDetail.getTranslator());
                    intent.putExtra("isbn", bookDetail.getIsbn10() != null ?
                            bookDetail.getIsbn10() : bookDetail.getIsbn13());
                    intent.putExtra("price", bookDetail.getPrice());
                    intent.putExtra("publisher", bookDetail.getPublisher());
                    intent.putExtra("pubDate", bookDetail.getPubDate());
                    intent.putExtra("binding", bookDetail.getBinding());
                    intent.putExtra("pages", bookDetail.getPages());
                    startActivity(intent);
                }
                break;
            }
            case R.id.tv_collect: {
                if (bookDetail != null) {
                    if (bookDetail.isCollect()) {
                        presenter.collectDel(bookDetail.getBookId());
                    } else {
                        presenter.collectAdd(bookDetail.getBookId());
                    }
                }
                break;
            }
            case R.id.tv_community: {
                Intent intent = new Intent(this, BookCommunityActivity.class);
                intent.putExtra("communityId", bookDetail.getCommunityId());
                startActivity(intent);
                break;
            }
            case R.id.tv_add: {
                showBottomDialog();
                presenter.getMySheets();
                break;
            }
        }

    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onBookDetail(BookDetail detail) {
        if (detail != null) {
            bookDetail = detail;
            if (!TextUtils.isEmpty(detail.getCover())) {
                presenter.requestCoverImg(detail.getCover(), sdvCover.getController());
            }
            tvBookName.setText(detail.getTitle());
            rbRating.setRating(detail.getRating() / 2F);
            tvRating.setText(String.format(Locale.getDefault(),
                    getString(R.string.fill_score), detail.getRating()));
            String pinfo = detail.getAuthor() + " 著 / ";
            pinfo += detail.getTranslator() + " 译 / ";
            pinfo += detail.getPublisher() + " ";
            pinfo += detail.getPubDate();
            tvPublisherInfo.setText(pinfo);
            tvSummary.setText(detail.getSummary());
            tvAuthorInfo.setText(detail.getAuthorInfo());
            refreshCollectView();
        }
    }

    private void refreshCollectView() {
        Drawable collDrawable;
        if (bookDetail.isCollect()) {
            collDrawable = getResources().getDrawable(R.mipmap.collect_icon_checked);
        } else {
            collDrawable = getResources().getDrawable(R.mipmap.collect_icon_default);
        }
        collDrawable.setBounds(0, 0, collDrawable.getIntrinsicWidth(), collDrawable.getIntrinsicHeight());
        tvCollect.setCompoundDrawables(collDrawable, null, null, null);
    }

    @Override
    public void onDraweeController(PipelineDraweeController controller) {
        sdvCover.setController(controller);
    }

    @Override
    public void onBlurCover(Bitmap bitmap) {
        rlDetailTop.setBackgroundDrawable(new BitmapDrawable(bitmap));
    }

    @Override
    public void onCollect(boolean success) {
        bookDetail.setCollect(success);
        refreshCollectView();
    }

    @Override
    public void onMySheets(List<BookSheet> bookSheets) {
        if (rvBookSheets != null && rvBookSheets.getAdapter() != null) {
            ((MyBookSheetAdapter) rvBookSheets.getAdapter()).clearData();
            if (bookSheets.size() > 3) {
                ViewGroup.LayoutParams params = rvBookSheets.getLayoutParams();
                params.height = getResources().getDimensionPixelSize(R.dimen.my_sheet_max_height);
                rvBookSheets.setLayoutParams(params);
            }
            ((MyBookSheetAdapter) rvBookSheets.getAdapter()).addItems(bookSheets);
        }
    }

    @Override
    public void onSheetAddBook(long sheetId, long bookId, boolean success) {

    }

    private BookDetail bookDetail;

    @Override
    public void onItemClick(View v, BookSheet bookSheet) {
        hideBottomDialog();
        if (bookSheet != null && bookDetail != null) {
            presenter.bookSheetAddBook(bookSheet.getSheetId(), bookDetail.getBookId());
        }
    }
}
