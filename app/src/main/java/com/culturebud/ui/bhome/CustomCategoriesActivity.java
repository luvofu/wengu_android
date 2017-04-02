package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.adapter.CustomCategoriesAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Category;
import com.culturebud.contract.CustomCategoriesContract;
import com.culturebud.presenter.CustomCategoriesPresenter;
import com.culturebud.ui.me.GeneralEditorActivity;
import com.culturebud.widget.DividerItemDecoration;

import java.util.List;

/**
 * Created by XieWei on 2017/3/31.
 */

@PresenterInject(CustomCategoriesPresenter.class)
public class CustomCategoriesActivity extends BaseActivity<CustomCategoriesContract.Presenter> implements
        CustomCategoriesContract.View, CustomCategoriesAdapter.OnItemClickListener {
    private RecyclerView rvCustomCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_categories);
        presenter.setView(this);
        showTitlebar();
        showBack();
        showOperas();
        setOperasText("完成");
        setTitle("管理自定义分类");
        rvCustomCategories = obtainViewById(R.id.rv_custom_categories);
        rvCustomCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration divider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, true);
        rvCustomCategories.addItemDecoration(divider);
        CustomCategoriesAdapter adapter = new CustomCategoriesAdapter();
        adapter.setOnItemClickListener(this);
        rvCustomCategories.setAdapter(adapter);
        presenter.customCategories();
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        finish();
    }

    @Override
    public void onCustomCategories(List<Category> categories) {
        ((CustomCategoriesAdapter)rvCustomCategories.getAdapter()).clearData();
        ((CustomCategoriesAdapter)rvCustomCategories.getAdapter()).addItems(categories);
    }

    @Override
    public void onCategoryChanged(boolean success) {
        if (success) {
            presenter.customCategories();
        }
    }

    @Override
    public void onItemClick(View view, Category category) {
        if (category == null) {
            Intent intent = new Intent(this, GeneralEditorActivity.class);
            intent.putExtra("title", "新增分类");
            intent.putExtra("hint", "输入书籍分类（不超过8个字）");
            intent.putExtra("content_length", 8);
            startActivityForResult(intent, CommonConst.RequestCode.REQUEST_CODE_ADD_CUSTOM_CATEGORY);
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CommonConst.RequestCode.REQUEST_CODE_ADD_CUSTOM_CATEGORY:
                if (RESULT_OK == resultCode) {
                    String content = data.getStringExtra("content");
                    if (!TextUtils.isEmpty(content)) {
                        presenter.addCustomCategory(content);
                    }
                }
                break;
        }
    }
}
