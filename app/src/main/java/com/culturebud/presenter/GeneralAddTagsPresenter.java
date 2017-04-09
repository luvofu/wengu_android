package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.contract.GeneralAddTagsContract;
import com.culturebud.model.GeneralAddTagsModel;
import com.culturebud.util.ApiException;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/3/13.
 */

public class GeneralAddTagsPresenter extends GeneralAddTagsContract.Presenter {

    public GeneralAddTagsPresenter() {
        setModel(new GeneralAddTagsModel());
    }

    @Override
    public void getBookTags() {
        model.getBookTags(BaseApp.getInstance().getUser().getToken())
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

    @Override
    public void getBookSheetTags() {
        model.getBookSheetTags(BaseApp.getInstance().getUser().getToken())
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
