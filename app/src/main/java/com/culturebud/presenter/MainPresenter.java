package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.BaseFragment;
import com.culturebud.contract.MainContract;
import com.culturebud.ui.bcircle.BookCircleFragment;
import com.culturebud.ui.bhome.BookHomeFragment;
import com.culturebud.ui.community.CommunityFragment;
import com.culturebud.ui.front.FrontPageFragment;
import com.culturebud.ui.me.MeFragment;

/**
 * Created by XieWei on 2016/10/21.
 */

public class MainPresenter extends MainContract.Presenter {
    private BaseFragment[] pages;

    public MainPresenter() {
        pages = new BaseFragment[3];
    }

    @Override
    public void initFragment() {
        pages[0] = new FrontPageFragment();
//        pages[1] = new CommunityFragment();
        pages[1] = new BookHomeFragment();
//        pages[3] = new BookCircleFragment();
        pages[2] = new MeFragment();
        attachFragment();
    }

    @Override
    public void attachFragment() {
        view.onAttachPages(pages);
    }

    @Override
    public void switchPage(int index) {
        view.onSwitchPage(pages, pages[index].getClass());
        BaseApp.getInstance().setCurrTabIndex(index);
    }

    @Override
    public void hidePop() {
        ((BookHomeFragment)pages[1]).hidePop();
    }
}
