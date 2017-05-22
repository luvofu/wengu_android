package com.culturebud.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.culturebud.BaseApp;
import com.culturebud.bean.User;
import com.culturebud.contract.MyFriendsContract;
import com.culturebud.model.MyFriendsModel;
import com.culturebud.util.ApiException;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/12/5.
 */

public class MyFriendsPresenter extends MyFriendsContract.Presenter {
    private static final String TAG = MyFriendsPresenter.class.getSimpleName();

    public MyFriendsPresenter() {
        setModel(new MyFriendsModel());
    }

    @Override
    public void myFriends() {
        if (!validateToken()) {
            return;
        }
        view.showLoadingView();
        model.myFriends(BaseApp.getInstance().getUser().getToken())
                .subscribeOn(Schedulers.io())
                .filter(users -> {
                    for (User user : users) {
                        Log.d(TAG, user.getNickname().charAt(0) + "");
                        char nick = user.getNickname().charAt(0);
                        String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(nick);
                        Log.d(TAG, pinyin + "");
                        if (pinyin != null && pinyin.length > 0) {
                            user.setSpellFirst(pinyin[0].substring(0, 1).toUpperCase());
                        } else if ((nick >= 65 && nick <= 90)
                                || (nick >= 97 && nick <= 122)) {
                            user.setSpellFirst((nick + "").toUpperCase());
                        } else {
                            user.setSpellFirst("#");
                        }
                    }
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.hiddenNoDataView();

                        String errorMessage = ApiException.getErrorMessage(e);
                        view.showErrorView(errorMessage);
                    }

                    @Override
                    public void onNext(List<User> users) {
                        view.hiddenNoDataView();

                        if (users.isEmpty()) {
                            view.showNoDataView("还没有好友，赶快添加吧");
                        }

                        view.onFriends(users);
                    }
                });
    }
}
