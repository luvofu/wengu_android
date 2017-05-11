package com.culturebud.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.culturebud.BaseApp;
import com.culturebud.BaseFragment;
import com.culturebud.bean.User;
import com.culturebud.contract.MainContract;
import com.culturebud.model.LoginModel;
import com.culturebud.ui.bhome.BookHomeFragment;
import com.culturebud.ui.front.FrontPageFragment;
import com.culturebud.ui.me.MeFragment;
import com.culturebud.util.ApiException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.culturebud.ApiErrorCode.EXCEPT_ACC;
import static com.culturebud.ApiErrorCode.EXPIRETIME_TOKEN;

/**
 * Created by XieWei on 2016/10/21.
 */

public class MainPresenter extends MainContract.Presenter {
    private BaseFragment[] pages;

    public MainPresenter() {
        pages = new BaseFragment[3];

        setModel(new MainContract.Model(){});
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

    @Override
    public void autoLogin() {
        if (!validateToken(false)) {
            return;
        }
        model.autoLogin(BaseApp.getInstance().getUser().getToken())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   if (e instanceof ApiException) {
                                        //发生错误，本地登出.
                                       switch (((ApiException) e).getCode()) {
                                           case EXPIRETIME_TOKEN:
                                           case EXCEPT_ACC: {
                                               //账号异常或Token过期
                                               //第一步本地登出
                                               model.autoLogin(BaseApp.getInstance().getUser().getToken());

                                               //第二步，清除shereSDK授权.此处待实现.

                                               //第三步，弹出错误描述.
                                               try {
                                                   view.onErrorTip(e.getMessage());
                                               } catch (Exception ec) {
                                                   ec.printStackTrace();
                                               }
                                           }
                                       }
                                   }
                               }

                               @Override
                               public void onNext(User user) {
                                   if (user != null) {
                                       //成功获取到用户实体信息.
                                       //第一步保存内存中的实体信息.
                                       BaseApp.getInstance().setUser(user);

                                       //第二步更新数据库中的用户实体信息.
                                        updateLocalUser();
                                   }
                               }
                           }

                );
    }



    private void updateLocalUser() {
        model.updateLocalUser(BaseApp.getInstance().getUser()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                    }
                });
    }
}
