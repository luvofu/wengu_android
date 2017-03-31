package com.culturebud.ui.bhome;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.CustomCategoriesAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Category;
import com.culturebud.contract.CustomCategoriesContract;
import com.culturebud.presenter.CustomCategoriesPresenter;
import com.culturebud.widget.DividerItemDecoration;

import java.util.List;

/**
 * Created by XieWei on 2017/3/31.
 */

@PresenterInject(CustomCategoriesPresenter.class)
public class CustomCategoriesActivity extends BaseActivity<CustomCategoriesContract.Presenter> implements
        CustomCategoriesContract.View {
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
        rvCustomCategories.setAdapter(new CustomCategoriesAdapter());
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
}
