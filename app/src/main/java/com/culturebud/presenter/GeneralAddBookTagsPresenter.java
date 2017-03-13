package com.culturebud.presenter;

import com.culturebud.contract.GeneralAddBookTagsContract;
import com.culturebud.util.ApiException;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/3/13.
 */

public class GeneralAddBookTagsPresenter extends GeneralAddBookTagsContract.Presenter {

    public GeneralAddBookTagsPresenter() {
        setModel(new GeneralAddBookTagsContract.Model(){});
    }

    @Override
    public void getBookTags() {
        model.getBookTags()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof ApiException) {
                    view.onErrorTip(e.getMessage());
                }
            }

            @Override
            public void onNext(List<String> tags) {
                view.onTags(tags);
            }
        });
    }
}
