package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.contract.BookSheetEditRecommendContract;
import com.culturebud.model.BookSheetEditRecommendModel;
import com.culturebud.util.ApiException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/1/20.
 */

public class BookSheetEditRecommendPresenter extends BookSheetEditRecommendContract.Presenter {

    public BookSheetEditRecommendPresenter() {
        setModel(new BookSheetEditRecommendModel());
    }

    @Override
    public void editRecommend(long sheetBookId, String recommend) {
        if (!validateToken()) {
            return;
        }
        view.showProDialog();
        model.editRecommend(BaseApp.getInstance().getUser().getToken(), sheetBookId, recommend)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        view.hideProDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProDialog();
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onEditRecommend(sheetBookId, aBoolean);
                    }
                });
    }
}
