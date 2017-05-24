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
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
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
import com.culturebud.bean.CategoryTag;
import com.culturebud.bean.CollectedBook;
import com.culturebud.bean.User;
import com.culturebud.contract.CollectedBooksContract;
import com.culturebud.presenter.CollectedBooksPresenter;
import com.culturebud.ui.front.BookDetailActivity;
import com.culturebud.ui.me.GeneralEditorActivity;
import com.culturebud.util.WidgetUtil;
import com.culturebud.widget.DividerItemDecoration;
import com.culturebud.widget.FlowLayout;
import com.culturebud.widget.RecyclerViewDivider;
import com.culturebud.widget.TagFlowLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_USERBOOK_INFO;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_ENTERING_NEW_BOOK;
import static com.culturebud.R.id.tfl_custom;

/**
 * Created by XieWei on 2016/11/9.
 */

@PresenterInject(CollectedBooksPresenter.class)
public class CollectedBooksActivity extends BaseActivity<CollectedBooksContract.Presenter> implements
        CollectedBooksContract.View, CollectedBooksAdapter.OnItemClickListener, CollectedBooksVerticalAdapter
        .OnItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView
        .OnNavigationItemReselectedListener, TagFlowLayout.OnTagClickListener {
    public static final int TYPE_SELECT = 1;
    public static final String TYPE_KEY = "opera_type";
    public static final String USER_ID_KEY = "user_id";
    private RecyclerView rvBooks;
    private int currentPage = 0;
    private int currType = CommonConst.CategoryType.TYPE_ALL;
    private String currCategory = "全部";
    private int currStatis = 0;
    private boolean loading = true;
    private int opreaType;
    private long userId;
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

        setNoDataView(R.id.main_multiplestatusview);

        presenter.setView(this);
        Intent intent = getIntent();
        opreaType = intent.getIntExtra(TYPE_KEY, 0);
        User user = BaseApp.getInstance().getUser();
        long defaultId = user != null ? user.getUserId() : -1;
        userId = intent.getLongExtra(USER_ID_KEY, defaultId);
        showTitlebar();
        setTitle("全部");
        setTitleRightIcon(R.mipmap.ic_arrow_white_down);

        rvBooks = obtainViewById(R.id.rv_collected_books);
        fabEditBooks = obtainViewById(R.id.fab_edit_books);
        bnvOperas = obtainViewById(R.id.bnv_operas);
        switchRvMarginBottom(false);


        if (BaseApp.getInstance().isMe(userId)) {
            setOperasDrawable(R.drawable.titlebar_add_selector);
            showOperas();
        } else {
            fabEditBooks.hide();
            hideOpears();
        }

        bnvOperas.setOnNavigationItemReselectedListener(this);
        bnvOperas.setOnNavigationItemSelectedListener(this);
        fabEditBooks.setOnClickListener(this);

        initList();
        initCategoryDlg();
        initCustomCategoriesDlg();

        presenter.getCategoryStatistics(userId);
    }

    //右上角加号编辑
    private void initMoreOperas(List<MoreOperaItemsAdapter.MoreOperaItemBean> items) {
        if (bsdMoreOperas == null) {
            bsdMoreOperas = new BottomSheetDialog(this);
            bsdMoreOperas.setContentView(R.layout.bottom_sheet_dialog_multi);
            bsdMoreOperas.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet)
                    .setBackgroundResource(android.R.color.transparent);
            rvOperaItems = (RecyclerView) bsdMoreOperas.getWindow().findViewById(R.id.rv_opera_items);
            tvCancel = (TextView) bsdMoreOperas.getWindow().findViewById(R.id.tv_cancel);
            tvCancel.setOnClickListener(v -> hideMoreOperas());
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
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(this, MyCreatedBooksActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(this, CustomCategoriesActivity.class);
                        startActivityForResult(intent, CommonConst.RequestCode.REQUEST_CODE_EDIT_CUSTOMCATEGORY);
                        break;
                    }
                }
            } else if (item.getType() == 2) {
                Set<CollectedBook> checkedItems = ((CollectedBooksAdapter) rvBooks.getAdapter()).getCheckedBooks();
                presenter.alterReadStatus(checkedItems, item.getReadStatus());
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
            adapter.setMe(BaseApp.getInstance().isMe(userId));
            adapter.setOnItemClickListener(this);
            rvBooks.setAdapter(adapter);
        }
    }

    private void switchRvMarginBottom(boolean flag) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) rvBooks.getLayoutParams();
        if (flag) {
            int bm = getResources().getDimensionPixelSize(R.dimen.common_margin_bigger);
            lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin, bm);
        } else {
            lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin, 0);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fab_edit_books:
                switchModel(CollectedBooksAdapter.MODEL_CHECK);
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
        if (((CollectedBooksAdapter) rvBooks.getAdapter()).inModel(CollectedBooksAdapter.MODEL_CHECK)) {
            switchModel(CollectedBooksAdapter.MODEL_EDIT);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    public void switchModel(int model) {
        CollectedBooksAdapter adapter = (CollectedBooksAdapter) rvBooks.getAdapter();
        if (model == CollectedBooksAdapter.MODEL_EDIT) {
            setOperasText(null);
            setOperasDrawable(R.drawable.titlebar_add_selector);
            fabEditBooks.show();
            switchRvMarginBottom(false);
            bnvOperas.setVisibility(View.GONE);
            adapter.setModel(CollectedBooksAdapter.MODEL_EDIT, true);
            adapter.clearCheckedStatus();
        } else if (model == CollectedBooksAdapter.MODEL_CHECK) {
            setOperasText(getString(R.string.complete));
            setOperasDrawable(null);
            fabEditBooks.hide();
            switchRvMarginBottom(true);
            bnvOperas.setVisibility(View.VISIBLE);
            adapter.setModel(CollectedBooksAdapter.MODEL_CHECK, true);
        }
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        TextView tv = (TextView) view;
        if (tv.getText().equals(getString(R.string.complete))) {
            switchModel(CollectedBooksAdapter.MODEL_EDIT);
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

    private TagFlowLayout tflAll, tflClc, tflCustom, tflOther;

    //标题栏藏书分类
    private void initCategoryDlg() {
        if (ppwCategory == null) {
            ppwCategory = new PopupWindow(this, null, R.style.PopupWindow);
            ppwCategory.setBackgroundDrawable(new ColorDrawable(0x55333333));
            ppwCategory.setFocusable(true);
            ppwCategory.setOutsideTouchable(false);

            View view = getLayoutInflater().inflate(R.layout.dlg_user_book_category_type, null);

            tflAll = WidgetUtil.obtainViewById(view, R.id.tfl_all);
            tflClc = WidgetUtil.obtainViewById(view, R.id.tfl_clc);
            tflCustom = WidgetUtil.obtainViewById(view, tfl_custom);
            tflOther = WidgetUtil.obtainViewById(view, R.id.tfl_other);

            tflAll.setAdapter(new WhiteTagAdapter(CommonConst.CategoryType.TYPE_ALL, true));
            tflClc.setAdapter(new WhiteTagAdapter(CommonConst.CategoryType.TYPE_NORMAL, false));
            tflCustom.setAdapter(new WhiteTagAdapter(CommonConst.CategoryType.TYPE_CUSTOM, false));
            tflOther.setAdapter(new WhiteTagAdapter(CommonConst.CategoryType.TYPE_OTHER, false));

            tflClc.setOnTagClickListener(this);
            tflCustom.setOnTagClickListener(this);
            tflOther.setOnTagClickListener(this);
            tflAll.setOnTagClickListener(this);

            ppwCategory.setContentView(view);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            ppwCategory.setWidth(dm.widthPixels);
            ppwCategory.setHeight((dm.heightPixels / 4) * 3);
            ppwCategory.setOnDismissListener(() -> setTitleRightIcon(R.mipmap.ic_arrow_white_down));
        }
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        WhiteTagAdapter adapter = (WhiteTagAdapter) ((TagFlowLayout) parent).getAdapter();
        CategoryTag categoryTag = adapter.getItem(position);
        if (!categoryTag.isSelected()) {
            ((WhiteTagAdapter) tflClc.getAdapter()).unselectAll();
            ((WhiteTagAdapter) tflCustom.getAdapter()).unselectAll();
            ((WhiteTagAdapter) tflOther.getAdapter()).unselectAll();
            ((WhiteTagAdapter) tflAll.getAdapter()).unselectAll();

            adapter.doSelect(view, position, parent);

            currentPage = 0;
            currType = adapter.getCategoryType();
            currCategory = categoryTag.getCategory();
            currStatis = categoryTag.getStatis();
            setTitle(currCategory + "(" + currStatis + ")");
            setTitleRightIcon(R.mipmap.ic_arrow_white_down);
            presenter.getCollectedBooks(userId, currentPage, currType, currCategory);
        }
        ppwCategory.dismiss();
        return true;
    }

    private static long lastClickTime;

    private void switchCategoryDlg() {
        Log.d("category", "switchCategoryDlg()");
        long tmp = System.currentTimeMillis() - lastClickTime;
        Log.d("category", "tmp = " + tmp);
        if (tmp < 1000) {
            return;
        }
        Log.d("category", "is showing " + lastClickTime);
        lastClickTime = System.currentTimeMillis();
        if (ppwCategory == null || !ppwCategory.isShowing()) {
            showCategoryDlg();
        } else {
            hideCategoryDlg();
        }
    }

    private void showCategoryDlg() {
        setTitleRightIcon(R.mipmap.ic_arrow_white_up);
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
        if (currentPage == 0) {
            //第一页无数据，需要刷新.
            if (opreaType == TYPE_SELECT) {
                ((CollectedBooksVerticalAdapter) rvBooks.getAdapter()).clearData();
            } else {
                ((CollectedBooksAdapter) rvBooks.getAdapter()).clearData();
            }
        }

        //有数据，加到适配器
        if (books != null && !books.isEmpty()) {
            if (opreaType == TYPE_SELECT) {
                ((CollectedBooksVerticalAdapter) rvBooks.getAdapter()).addItems(books);
            } else {
                ((CollectedBooksAdapter) rvBooks.getAdapter()).addItems(books);
            }
        }

        loading = false;
    }

    Map<Integer, List<Category>> cgMap = new HashMap<>();

    @Override
    public void onCategoryStatistics(BookCategoryGroup categoryGroup) {
        if (categoryGroup != null) {
            for (BookCategoryGroup.CategoryGroup cg : categoryGroup.getCategoryGroups()) {
                cgMap.put(cg.getCategoryType(), cg.getCategoryStatistics());
            }
            List<Category> categorieall = new ArrayList<>();
            Category c = new Category();
            c.setCategory("全部");
            c.setStatis(categoryGroup.getTotal());
            categorieall.add(c);
            cgMap.put(CommonConst.CategoryType.TYPE_ALL, categorieall);

            ((WhiteTagAdapter) tflClc.getAdapter()).setData(cgMap.get(CommonConst.CategoryType.TYPE_NORMAL), currType, currCategory);
            ((WhiteTagAdapter) tflCustom.getAdapter()).setData(cgMap.get(CommonConst.CategoryType.TYPE_CUSTOM), currType, currCategory);
            ((WhiteTagAdapter) tflOther.getAdapter()).setData(cgMap.get(CommonConst.CategoryType.TYPE_OTHER), currType, currCategory);
            ((WhiteTagAdapter) tflAll.getAdapter()).setData(cgMap.get(CommonConst.CategoryType.TYPE_ALL), currType, currCategory);

            updateContent();
        }
    }

    void updateContent() {
        String newCategory = "";
        int newStatis = 0;
        for (Category category : cgMap.get(currType)) {
            if (currCategory.equals(category.getCategory())) {
                newCategory = category.getCategory();
                newStatis = category.getStatis();
                break;
            }
        }

        if (!currCategory.equals(newCategory) || currStatis != newStatis) {
            if (newCategory.isEmpty()) {
                currType = CommonConst.CategoryType.TYPE_ALL;
                CategoryTag all = ((CategoryTag) tflAll.getAdapter().getItem(0));
                newCategory = all.getCategory();
                newStatis = all.getStatis();
                all.setSelected(true);
                tflAll.getAdapter().notifyDataChanged();
            }
            currCategory = newCategory;
            currStatis = newStatis;
            currentPage = 0;
            presenter.getCollectedBooks(userId, currentPage, currType, currCategory);
            setTitle(currCategory + "(" + currStatis + ")");
        }
    }

    //删除藏书更新
    @Override
    public void onDeleteUserBooks(Set<CollectedBook> books, boolean success) {
        if (success) {
            presenter.getCategoryStatistics(userId);
        }
        switchModel(CollectedBooksAdapter.MODEL_EDIT);
    }

    //阅读状态改变更新
    @Override
    public void onAlterReadStatus(Set<CollectedBook> books, boolean success) {
        if (success) {
            presenter.getCategoryStatistics(userId);
        }
        switchModel(CollectedBooksAdapter.MODEL_EDIT);
    }

    //书籍设置分类更新
    @Override
    public void onMove2Category(boolean success) {
        hideCustomCategoriesDlg();
        if (getOperasView().getText().equals(getString(R.string.complete))) {
            switchModel(CollectedBooksAdapter.MODEL_EDIT);
        }
        if (success) {
            presenter.getCategoryStatistics(userId);
        }
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
                startActivityForResult(myBookInfo, REQUEST_CODE_EDIT_USERBOOK_INFO);
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
                    presenter.getCollectedBooks(userId, ++currentPage, currType, currCategory);
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
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    presenter.deleteUserBooks(checkedItems);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private TextView tvCategoriesCount;
    private RecyclerView rvCategories;

    private void initCustomCategoriesDlg() {
        if (bsdCustomCategories == null) {
            bsdCustomCategories = new BottomSheetDialog(this);
            bsdCustomCategories.setContentView(R.layout.dlg_custom_categories);
            tvCategoriesCount = (TextView) bsdCustomCategories.getWindow().findViewById(R.id.tv_categories_count);
            rvCategories = (RecyclerView) bsdCustomCategories.getWindow().findViewById(R.id.rv_custom_categories);
            rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            DividerItemDecoration divider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, true);
            rvCategories.addItemDecoration(divider);
            CustomCategoriesAdapter adapter = new CustomCategoriesAdapter();
            adapter.setAsDlg();
            adapter.setOnItemClickListener((v, category) -> {
                if (category == null) {//添加分类
                    Intent intent = new Intent(this, GeneralEditorActivity.class);
                    intent.putExtra("title", "新增分类");
                    intent.putExtra("hint", "输入书籍分类（不超过8个字）");
                    intent.putExtra("content_length", 8);
                    startActivityForResult(intent, CommonConst.RequestCode.REQUEST_CODE_MOVE_TO_NEW_CUSTOM_CATEGORY);
                } else {//把书籍添加到该分类
                    presenter.moveBook2CustomCategory(((CollectedBooksAdapter) rvBooks.getAdapter())
                            .getCheckedBooks(), category.getCategory());
                }
            });
            rvCategories.setAdapter(adapter);
        }
    }

    public void showCustomCategoriesDlg() {
        if (!bsdCustomCategories.isShowing()) {

            if (rvCategories == null) {
                return;
            }
            ((CustomCategoriesAdapter) rvCategories.getAdapter()).clearData();
            ((CustomCategoriesAdapter) rvCategories.getAdapter()).addItems(
                    cgMap.get(CommonConst.CategoryType.TYPE_CUSTOM));
            tvCategoriesCount.setText(String.format(Locale.getDefault(),
                    getString(R.string.txt_custom_categories_count),
                    rvCategories.getAdapter().getItemCount() - 1));

            bsdCustomCategories.show();
        }
    }

    public void hideCustomCategoriesDlg() {
        if (bsdCustomCategories != null && bsdCustomCategories.isShowing()) {
            bsdCustomCategories.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CommonConst.RequestCode.REQUEST_CODE_MOVE_TO_NEW_CUSTOM_CATEGORY://添加到新分类
                if (RESULT_OK == resultCode) {
                    String content = data.getStringExtra("content");
                    if (!TextUtils.isEmpty(content)) {
                        presenter.moveBook2CustomCategory(((CollectedBooksAdapter) rvBooks.getAdapter()).getCheckedBooks(), content);
                    }
                }
                break;
            case CommonConst.RequestCode.REQUEST_CODE_EDIT_CUSTOMCATEGORY:
                if (RESULT_OK == resultCode) {
                    //编辑自定义分类更新
                    presenter.getCategoryStatistics(userId);
                }
                break;
            case CommonConst.RequestCode.REQUEST_CODE_EDIT_USERBOOK_INFO:
                if (RESULT_OK == resultCode) {
                    //编辑藏书信息更新
                    presenter.getCategoryStatistics(userId);
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int requestCode = intent.getIntExtra("requestCode", 0);
        if (requestCode == REQUEST_CODE_ENTERING_NEW_BOOK) {
            //添加新藏书更新
            presenter.getCategoryStatistics(userId);
        }
    }

    @Override
    public void onRetryData() {
        presenter.getCategoryStatistics(userId);
    }


}
