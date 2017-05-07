package com.culturebud.ui.bhome;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

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

import java.util.Iterator;
import java.util.List;

/**
 * Created by XieWei on 2017/3/31.
 */

@PresenterInject(CustomCategoriesPresenter.class)
public class CustomCategoriesActivity extends BaseActivity<CustomCategoriesContract.Presenter> implements
        CustomCategoriesContract.View, CustomCategoriesAdapter.OnItemClickListener, CustomCategoriesAdapter
        .OnItemDeleteListener, CustomCategoriesAdapter.OnItemEditCategoryListener {
    private RecyclerView rvCustomCategories;
    CustomCategoriesAdapter adapter = new CustomCategoriesAdapter();

    private boolean hasMoved = false; //是否排过序
    private boolean shouldFinish = false; //是否应该结束页面.

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
        adapter.setDeleteListener(this);
        adapter.setOnItemClickListener(this);
        adapter.setEditCategoryListener(this);
        rvCustomCategories.setAdapter(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(itemTouchCallback);
        helper.attachToRecyclerView(rvCustomCategories);
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

        shouldFinish = true;

        //关闭键盘.
        if (softKeyboardHasShowing()) {
            View view1 = rvCustomCategories.findFocus();
            if (view1 != null) {
                view1.clearFocus();
            }
        }

        //是否有排序过
        if (hasMoved) {
            /*
                提交新的排序.
                1. 获取现在的数据.
                2. 遍历，把categoryId通过|分割
             */
            List<Category> categories = adapter.getData();
            String categoryIdListString = "";

            if (categories != null && categories.size() > 0) {
                Iterator<Category> cgs = categories.iterator();
                while (cgs.hasNext()) {
                    Category cg = cgs.next();
                    categoryIdListString = categoryIdListString + cg.getCategoryId() + "|";
                }
                categoryIdListString = categoryIdListString.substring(0, categoryIdListString.lastIndexOf("|"));
            }

            presenter.sortCustomCategory(categoryIdListString);
        }

        if (!softKeyboardHasShowing() && !hasMoved) {
            //没有做改变,直接退出.
            shouldFinish = false;
            finish();
        }
    }

    @Override
    public void onCustomCategories(List<Category> categories) {
        ((CustomCategoriesAdapter) rvCustomCategories.getAdapter()).clearData();
        ((CustomCategoriesAdapter) rvCustomCategories.getAdapter()).addItems(categories);
    }

    @Override
    public void onCategoryChanged(boolean success) {
        if (success) {
            if (shouldFinish) {
                finish();
            }
        } else {
            shouldFinish = false;
        }
    }

    @Override
    public void onAddCategory(boolean success) {
        if (success) {
            presenter.customCategories();
        }
    }

    @Override
    public void onDeleteCategory(boolean success) {
        if (success) {
            presenter.customCategories();
        }
    }

    @Override
    public void onCategorySorted(boolean success) {
        if (success) {
            //排序提交成功.返回书架页面.
            finish();
        } else {
            shouldFinish = false;
            hasMoved = false;
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
            //修改分类

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

    @Override
    public void onItemDelete(int position, Category category) {
        if (category != null) {
            new AlertDialog.Builder(this).setMessage("删除分类将会连同分类里面的书籍一起删除，您确定要删除吗？")
                    .setPositiveButton("删除", (dialog, which) -> {
                        ((CustomCategoriesAdapter) rvCustomCategories.getAdapter()).deleteItem(category);
                        presenter.deleteCustomCategory(category.getCategoryId());
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

    private ItemTouchHelper.Callback itemTouchCallback = new ItemTouchHelper.Callback() {

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = 0, swipeFlags = 0;
            if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                //j禁止侧滑功能
                swipeFlags = 0;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder
                target) {
            if (source.getItemViewType() != target.getItemViewType()) {
                return false;
            } else {
                adapter.onItemDragMoving(source, target);

                hasMoved = true; //置为排序

                return true;//返回true表示执行拖动
            }

        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //此方法在当前业务环境下不用实现
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX,
                                float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                //滑动时改变Item的透明度
                final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            }
        }
    };

    @Override
    public void onItemEditCategory(EditText editText, Category category) {
        /*
            修改分类名后，键盘消失调用此方法.
            判断是否修改过分类名，没有修改则无需上传.
         */
        String newCategoryName = editText.getText().toString();
        presenter.editCustomCategory(category.getCategoryId(), newCategoryName);
    }
}
