package com.culturebud.ui.bhome;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.adapter.CollectedBooksAdapter;
import com.culturebud.adapter.CollectedBooksVerticalAdapter;
import com.culturebud.adapter.CustomCategoriesAdapter;
import com.culturebud.adapter.MoreOperaItemsAdapter;
import com.culturebud.adapter.WhiteTagAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookCategoryGroup;
import com.culturebud.bean.Category;
import com.culturebud.bean.CollectedBook;
import com.culturebud.contract.CollectedBooksContract;
import com.culturebud.presenter.CollectedBooksPresenter;
import com.culturebud.ui.front.BookDetailActivity;
import com.culturebud.util.WidgetUtil;
import com.culturebud.widget.DividerItemDecoration;
import com.culturebud.widget.RecyclerViewDivider;
import com.culturebud.widget.TagFlowLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by XieWei on 2016/11/9.
 */

@PresenterInject(CollectedBooksPresenter.class)
public class CollectedBooksActivity extends BaseActivity<CollectedBooksContract.Presenter> implements
        CollectedBooksContract.View, CollectedBooksAdapter.OnItemClickListener, CollectedBooksVerticalAdapter
        .OnItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView
        .OnNavigationItemReselectedListener {
    public static final int TYPE_SELECT = 1;
    public static final String TYPE_KEY = "opera_type";
    private RecyclerView rvBooks;
    private int currentPage;
    private int currentCategoryType = CommonConst.UserBookCategoryType.TYPE_ALL;
    private String currentCategory = "全部";
    private boolean loading = true;
    private int opreaType;
    private BottomSheetDialog bsdMoreOperas;
    private RecyclerView rvOperaItems;
    private TextView tvCancel;
    private FloatingActionButton fabEditBooks;
    private PopupWindow ppwCategory;
    private BottomNavigationView bnvOperas;
    private BottomSheetDialog bsdCustomCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collected_books);
        presenter.setView(this);
        opreaType = getIntent().getIntExtra(TYPE_KEY, 0);
        showTitlebar();
        setTitle(R.string.book_shelf);
        setTitleRightIcon(R.mipmap.ic_arrow_white_down);
        setOperasDrawable(R.drawable.titlebar_add_selector);
        rvBooks = obtainViewById(R.id.rv_collected_books);
        fabEditBooks = obtainViewById(R.id.fab_edit_books);
        bnvOperas = obtainViewById(R.id.bnv_operas);
        bnvOperas.setOnNavigationItemReselectedListener(this);
        bnvOperas.setOnNavigationItemSelectedListener(this);
        fabEditBooks.setOnClickListener(this);
        initList();
        presenter.getMyBooks(currentPage);
    }

    private void initMoreOperas(List<MoreOperaItemsAdapter.MoreOperaItemBean> items) {
        if (bsdMoreOperas == null) {
            bsdMoreOperas = new BottomSheetDialog(this);
            bsdMoreOperas.setContentView(R.layout.bottom_sheet_dialog_multi);
            bsdMoreOperas.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet)
                    .setBackgroundResource(android.R.color.transparent);
            rvOperaItems = (RecyclerView) bsdMoreOperas.getWindow().findViewById(R.id.rv_opera_items);
            tvCancel = (TextView) bsdMoreOperas.getWindow().findViewById(R.id.tv_cancel);
            tvCancel.setOnClickListener(v -> {
                hideMoreOperas();
            });
            rvOperaItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvOperaItems.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL));
        }
        MoreOperaItemsAdapter adapter = new MoreOperaItemsAdapter();
        adapter.setItems(items);
        adapter.setOnMoreOperaItemClickListener((v, item, position) -> {
            if (item.getType() == 1) {
                switch (position) {
                    case 0: {
                        Intent intent = new Intent(this, BookScanActivity.class);
                        startActivityForResult(intent, CommonConst.RequestCode.REQUEST_CODE_ENTERING_NEW_BOOK);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(this, MyCreatedBooksActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(this, CustomCategoriesActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            } else if (item.getType() == 2) {
                Set<CollectedBook> checkedItems = ((CollectedBooksAdapter) rvBooks.getAdapter()).getCheckedBooks();
                presenter.alterReadStatus(checkedItems, item.getReadStatus());
                setOperasText(null);
                setOperasDrawable(R.drawable.titlebar_add_selector);
                fabEditBooks.show();
                bnvOperas.setVisibility(View.GONE);
                ((CollectedBooksAdapter) rvBooks.getAdapter()).setModel(CollectedBooksAdapter.MODEL_EDIT, true);
                ((CollectedBooksAdapter) rvBooks.getAdapter()).clearCheckedStatus();
            }
            hideMoreOperas();
        });
        rvOperaItems.setAdapter(adapter);
    }

    public void showMoreOperas(List<MoreOperaItemsAdapter.MoreOperaItemBean> items) {
        if (bsdMoreOperas != null && bsdMoreOperas.isShowing()) {
            return;
        }
        initMoreOperas(items);
        bsdMoreOperas.show();
    }

    public void hideMoreOperas() {
        if (bsdMoreOperas != null && bsdMoreOperas.isShowing()) {
            bsdMoreOperas.dismiss();
        }
    }

    private void initList() {
        if (opreaType == TYPE_SELECT) {
            LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvBooks.setLayoutManager(llm);
            RecyclerViewDivider hdivider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
            rvBooks.addItemDecoration(hdivider);
            CollectedBooksVerticalAdapter adapter = new CollectedBooksVerticalAdapter();
            adapter.setOnItemClickListener(this);
            rvBooks.setAdapter(adapter);
        } else {
            GridLayoutManager glm = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
            rvBooks.setLayoutManager(glm);
            RecyclerViewDivider hdivider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
            Drawable horizontal = getResources().getDrawable(R.mipmap.book_sheet_divider);
            hdivider.setHorizontalDrawable(horizontal);
            hdivider.setDividerHeight(20);
            rvBooks.addItemDecoration(hdivider);
            rvBooks.addOnScrollListener(recyclerScrollerListener);
            CollectedBooksAdapter adapter = new CollectedBooksAdapter();
            adapter.setOnItemClickListener(this);
            rvBooks.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fab_edit_books:
                fabEditBooks.hide();
                bnvOperas.setVisibility(View.VISIBLE);
                setOperasDrawable(null);
                setOperasText("完成");
                ((CollectedBooksAdapter) rvBooks.getAdapter()).setModel(CollectedBooksAdapter.MODEL_CHECK, true);
                break;
        }
    }

    @Override
    protected void onTitleOptions(View view) {
        super.onTitleOptions(view);
        switchCategoryDlg();
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        TextView tv = (TextView) view;
        if ("完成".equals(tv.getText())) {
            setOperasText(null);
            setOperasDrawable(R.drawable.titlebar_add_selector);
            fabEditBooks.show();
            bnvOperas.setVisibility(View.GONE);
            ((CollectedBooksAdapter) rvBooks.getAdapter()).setModel(CollectedBooksAdapter.MODEL_EDIT, true);
            ((CollectedBooksAdapter) rvBooks.getAdapter()).clearCheckedStatus();
        } else {
            List<MoreOperaItemsAdapter.MoreOperaItemBean> items = new ArrayList<>();
            items.add(new MoreOperaItemsAdapter.MoreOperaItemBean(1, "录入新书") {
            });
            items.add(new MoreOperaItemsAdapter.MoreOperaItemBean(1, "我创建的") {
            });
            items.add(new MoreOperaItemsAdapter.MoreOperaItemBean(1, "管理自定义分类") {
            });
            showMoreOperas(items);
        }
    }

    private Button btnAll;
    private TagFlowLayout tflClc, tflCustom, tflOther;

    private void initCategoryDlg() {
        if (ppwCategory == null) {
            ppwCategory = new PopupWindow(this, null, R.style.PopupWindow);
            ppwCategory.setBackgroundDrawable(new ColorDrawable(0x55333333));
            ppwCategory.setOutsideTouchable(true);
            View view = getLayoutInflater().inflate(R.layout.dlg_user_book_category_type, null);
            btnAll = WidgetUtil.obtainViewById(view, R.id.btn_all);
            tflClc = WidgetUtil.obtainViewById(view, R.id.tfl_clc);
            tflCustom = WidgetUtil.obtainViewById(view, R.id.tfl_custom);
            tflOther = WidgetUtil.obtainViewById(view, R.id.tfl_other);

            btnAll.setOnClickListener(v -> {
                setTitle(btnAll.getText());
                setTitleRightIcon(R.mipmap.ic_arrow_white_down);
                ppwCategory.dismiss();
                currentPage = 0;
                currentCategoryType = CommonConst.UserBookCategoryType.TYPE_ALL;
                currentCategory = "全部";
                presenter.getMyBooks(currentPage, CommonConst.UserBookCategoryType.TYPE_ALL, "全部");
            });

            tflClc.setOnSelectListener(selectPosSet -> {
                ppwCategory.dismiss();
                int index = selectPosSet.iterator().next();
                BookCategoryGroup.Category category = ((WhiteTagAdapter) tflClc.getAdapter()).getItem(index);
                setTitle(category.getCategory() + "(" + category.getStatistics() + ")");
                setTitleRightIcon(R.mipmap.ic_arrow_white_down);
                currentPage = 0;
                currentCategoryType = CommonConst.UserBookCategoryType.TYPE_NORMAL;
                currentCategory = category.getCategory();
                presenter.getMyBooks(currentPage, CommonConst.UserBookCategoryType.TYPE_NORMAL, category.getCategory());
            });

            tflCustom.setOnSelectListener(selectPosSet -> {
                ppwCategory.dismiss();
                int index = selectPosSet.iterator().next();
                BookCategoryGroup.Category category = ((WhiteTagAdapter) tflCustom.getAdapter()).getItem(index);
                setTitle(category.getCategory() + "(" + category.getStatistics() + ")");
                setTitleRightIcon(R.mipmap.ic_arrow_white_down);
                currentPage = 0;
                currentCategoryType = CommonConst.UserBookCategoryType.TYPE_CUSTOM;
                currentCategory = category.getCategory();
                presenter.getMyBooks(currentPage, CommonConst.UserBookCategoryType.TYPE_CUSTOM, category.getCategory());
            });

            tflOther.setOnSelectListener(selectPosSet -> {
                ppwCategory.dismiss();
                int index = selectPosSet.iterator().next();
                BookCategoryGroup.Category category = ((WhiteTagAdapter) tflOther.getAdapter()).getItem(index);
                setTitle(category.getCategory() + "(" + category.getStatistics() + ")");
                setTitleRightIcon(R.mipmap.ic_arrow_white_down);
                currentPage = 0;
                currentCategoryType = CommonConst.UserBookCategoryType.TYPE_OTHER;
                currentCategory = category.getCategory();
                presenter.getMyBooks(currentPage, CommonConst.UserBookCategoryType.TYPE_OTHER, category.getCategory());
            });

            ppwCategory.setContentView(view);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            ppwCategory.setWidth(dm.widthPixels);
            ppwCategory.setOnDismissListener(() -> setTitleRightIcon(R.mipmap.ic_arrow_white_down));
        }
    }

    private void switchCategoryDlg() {
        if (ppwCategory == null || !ppwCategory.isShowing()) {
            showCategoryDlg();
            presenter.getCategoryStatistics();
        } else {
            hideCategoryDlg();
        }
    }

    private void showCategoryDlg() {
        setTitleRightIcon(R.mipmap.ic_arrow_white_up);
        initCategoryDlg();
        if (!ppwCategory.isShowing()) {
            ppwCategory.showAsDropDown(getToolbar());
        }
    }

    private void hideCategoryDlg() {
        setTitleRightIcon(R.mipmap.ic_arrow_white_down);
        if (ppwCategory != null && ppwCategory.isShowing()) {
            ppwCategory.dismiss();
        }
    }

    @Override
    public void onBooks(List<CollectedBook> books) {
        if (opreaType == TYPE_SELECT) {
            if (currentPage == 0) {
                ((CollectedBooksVerticalAdapter) rvBooks.getAdapter()).clearData();
            }
            ((CollectedBooksVerticalAdapter) rvBooks.getAdapter()).addItems(books);
        } else {
            if (currentPage == 0) {
                ((CollectedBooksAdapter) rvBooks.getAdapter()).clearData();
            }
            ((CollectedBooksAdapter) rvBooks.getAdapter()).addItems(books);
        }
        loading = false;
    }

    private BookCategoryGroup categoryGroup;

    @Override
    public void onCategoryStatistics(BookCategoryGroup categoryGroup) {
        if (categoryGroup != null) {
            this.categoryGroup = categoryGroup;
            btnAll.setText("全部（" + categoryGroup.getTotal() + "）");
            List<BookCategoryGroup.CategoryGroup> categoryGroups = categoryGroup.getCategoryGroups();
            for (BookCategoryGroup.CategoryGroup cg : categoryGroups) {
                if (cg.getCategoryType() == 1) {
                    tflClc.setAdapter(new WhiteTagAdapter(cg.getCategoryStatistics()));
                } else if (cg.getCategoryType() == 2) {
                    tflCustom.setAdapter(new WhiteTagAdapter(cg.getCategoryStatistics()));
                } else if (cg.getCategoryType() == 3) {
                    tflOther.setAdapter(new WhiteTagAdapter(cg.getCategoryStatistics()));
                }
            }
        }
    }

    @Override
    public void onDeleteUserBooks(Set<CollectedBook> books, boolean success) {
        if (success) {
            presenter.getMyBooks(currentPage, currentCategoryType, currentCategory);
        }
    }

    @Override
    public void onCustomCategories(List<Category> categories) {
        if (rvCategories == null) {
            return;
        }
        ((CustomCategoriesAdapter) rvCategories.getAdapter()).clearData();
        ((CustomCategoriesAdapter) rvCategories.getAdapter()).addItems(categories);
        tvCategoriesCount.setText(String.format(Locale.getDefault(), getString(R.string.txt_custom_categories_count),
                rvCategories.getAdapter().getItemCount() - 1));
    }

    @Override
    public void onItemClick(View v, int position, CollectedBook book, int operaType) {
        switch (operaType) {
            case CollectedBooksAdapter.OPERA_TYPE_DETAIL:
                Intent intent = new Intent(this, BookDetailActivity.class);
                intent.putExtra("bookId", book.getBookId());
                startActivity(intent);
                break;
            case CollectedBooksAdapter.OPERA_TYPE_EDIT:
                Intent myBookInfo = new Intent(this, MyBookInfoActivity.class);
                myBookInfo.putExtra("userBookId", book.getUserBookId());
                myBookInfo.putExtra("book_title", book.getTitle());
                startActivity(myBookInfo);
                break;
        }
    }

    private RecyclerView.OnScrollListener recyclerScrollerListener = new RecyclerView.OnScrollListener() {
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
                if (!loading && (lastPosition + 1 >= total)) {
                    presenter.getMyBooks(++currentPage);
                    loading = true;
                }
            } else {

            }
        }
    };

    @Override
    public void onItemClick(int position, View v, CollectedBook book) {
        Intent intent = getIntent();
        intent.putExtra("book", new Gson().toJson(book));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Set<CollectedBook> checkedItems = ((CollectedBooksAdapter) rvBooks.getAdapter()).getCheckedBooks();
        if (!checkedItems.isEmpty()) {
            switch (item.getItemId()) {
                case R.id.menu_item_del:
                    deleteBooks(checkedItems);
                    break;
                case R.id.menu_item_read_status:
                    alterReadStatus();
                    break;
                case R.id.menu_item_custom:
                    showCustomCategoriesDlg();
                    break;
            }
        }
        return true;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        Set<CollectedBook> checkedItems = ((CollectedBooksAdapter) rvBooks.getAdapter()).getCheckedBooks();
        if (!checkedItems.isEmpty()) {
            switch (item.getItemId()) {
                case R.id.menu_item_del:
                    deleteBooks(checkedItems);
                    break;
                case R.id.menu_item_read_status:
                    alterReadStatus();
                    break;
                case R.id.menu_item_custom:
                    showCustomCategoriesDlg();
                    break;
            }
        }
    }

    private void alterReadStatus() {
        List<MoreOperaItemsAdapter.MoreOperaItemBean> items = new ArrayList<>();
        items.add(new MoreOperaItemsAdapter.MoreOperaItemBean(2, "已读", 0) {
        });
        items.add(new MoreOperaItemsAdapter.MoreOperaItemBean(2, "在读", 2) {
        });
        items.add(new MoreOperaItemsAdapter.MoreOperaItemBean(2, "未读", 1) {
        });
        showMoreOperas(items);
    }

    private void deleteBooks(Set<CollectedBook> checkedItems) {
        String msg;
        if (checkedItems.size() > 1) {
            msg = checkedItems.size() + "本书";
        } else {
            msg = checkedItems.iterator().next().getTitle();
        }
        new AlertDialog.Builder(this).setMessage("是否从书架上删除" + msg)
                .setPositiveButton("删除", (dialog, which) -> {
                    setOperasText(null);
                    setOperasDrawable(R.drawable.titlebar_add_selector);
                    fabEditBooks.show();
                    bnvOperas.setVisibility(View.GONE);
                    ((CollectedBooksAdapter) rvBooks.getAdapter()).setModel(CollectedBooksAdapter.MODEL_EDIT, true);
                    ((CollectedBooksAdapter) rvBooks.getAdapter()).clearCheckedStatus();
                    presenter.deleteUserBooks(checkedItems);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private TextView tvCategoriesCount;
    private RecyclerView rvCategories;

    private void initCustomCategoriesDlg() {
        if (bsdCustomCategories == null) {
            bsdCustomCategories = new BottomSheetDialog(this);
            bsdCustomCategories.setContentView(R.layout.dlg_custom_categories);
//            bsdCustomCategories.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet)
//                    .setBackgroundResource(android.R.color.transparent);
            tvCategoriesCount = (TextView) bsdCustomCategories.getWindow().findViewById(R.id.tv_categories_count);
            rvCategories = (RecyclerView) bsdCustomCategories.getWindow().findViewById(R.id.rv_custom_categories);
            rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            DividerItemDecoration divider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, true);
            rvCategories.addItemDecoration(divider);
            CustomCategoriesAdapter adapter = new CustomCategoriesAdapter();
            adapter.setAsDlg();
            rvCategories.setAdapter(adapter);
        }
        presenter.customCategories();
    }

    public void showCustomCategoriesDlg() {
        initCustomCategoriesDlg();
        if (!bsdCustomCategories.isShowing()) {
            bsdCustomCategories.show();
        }
    }

    public void hideCustomCategoriesDlg() {
        if (bsdCustomCategories != null && bsdCustomCategories.isShowing()) {
            bsdCustomCategories.dismiss();
        }
    }
}
