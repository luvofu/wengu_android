package com.culturebud.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RadioGroup;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.BaseFragment;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.MainContract;
import com.culturebud.presenter.MainPresenter;

/**
 * Created by XieWei on 2016/10/19.
 */

@PresenterInject(MainPresenter.class)
public class MainActivity extends BaseActivity<MainContract.Presenter> implements MainContract.View,
        RadioGroup.OnCheckedChangeListener {
    private RadioGroup rgTabs;
    private FragmentManager fragmentManager;
    private static final int[] PAGE_INDEX = {
            R.id.rb_front_page,
//            R.id.rb_community,
            R.id.rb_book_home,
//            R.id.rb_book_circle,
            R.id.rb_me
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        fragmentManager = getFragmentManager();
        rgTabs = obtainViewById(R.id.rg_tabs);
        rgTabs.setOnCheckedChangeListener(this);
        presenter.setView(this);
        presenter.initFragment();

        //调用自动登录API.
        presenter.autoLogin();
    }

    @Override
    public void onAttachPages(BaseFragment[] pages) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        for (BaseFragment page : pages) {
            ft.add(R.id.fl_container, page, "main");
        }
        ft.commitAllowingStateLoss();
        fragmentManager.beginTransaction()
                .hide(pages[0])
                .hide(pages[1])
                .hide(pages[2])
//                .hide(pages[3])
                .commitAllowingStateLoss();
        rgTabs.check(PAGE_INDEX[BaseApp.getInstance().getCurrTabIndex()]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (BaseApp.getInstance().getCurrTabIndex() == 1) {
            presenter.hidePop();
        }
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int index = 0;
        switch (checkedId) {
            case R.id.rb_front_page:
                index = 0;
                break;
//            case R.id.rb_community:
//                index = 1;
//                break;
            case R.id.rb_book_home:
//                index = 2;
                index = 1;
                break;
//            case R.id.rb_book_circle:
//                index = 3;
//                break;
            case R.id.rb_me:
//                index = 4;
//                index = 3;
                index = 2;
                break;
        }
        presenter.switchPage(index);
    }

    @Override
    public void onSwitchPage(BaseFragment[] pages, Class<? extends BaseFragment> pageClass) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        for (BaseFragment page : pages) {
            if (page.getClass() == pageClass) {
                ft.show(page);
            } else {
                ft.hide(page);
            }
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        rgTabs.check(PAGE_INDEX[BaseApp.getInstance().getCurrTabIndex()]);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //按物理返回键，不应该退出程序，应该切换到后台.
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


}
