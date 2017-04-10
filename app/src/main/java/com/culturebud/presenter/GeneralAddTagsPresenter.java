package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.HistoryTag;
import com.culturebud.contract.GeneralAddTagsContract;
import com.culturebud.model.GeneralAddTagsModel;
import com.culturebud.util.ApiException;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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

    @Override
    public void getHistoryTag(byte type) {
        if (!validateToken()) {
            return;
        }
        model.getLocalHistory(type, BaseApp.getInstance().getUser().getUserId())
                .subscribeOn(Schedulers.io())
                .map(historyTags -> {
                    List<String> tags = new ArrayList<>();
                    for (HistoryTag ht : historyTags) {
                        tags.add(ht.getTag());
                    }
                    return tags;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<String> historyTags) {
                        view.onHistoryTags(historyTags);
                    }
                });
    }

    @Override
    public void recordHistory(byte type, String tag) {
        if (!validateToken()) {
            return;
        }
        HistoryTag historyTag = new HistoryTag(type, tag, BaseApp.getInstance().getUser().getUserId());
        model.saveHistoryToLocal(historyTag).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        //TODO none
                    }
                });

    }
}
