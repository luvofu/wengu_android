package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.contract.MobileBindingContract;
import com.culturebud.model.MobileBindingModel;
import com.culturebud.util.ApiException;
import com.culturebud.util.TxtUtil;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MobileBindingPresenter extends MobileBindingContract.Presenter {

    public MobileBindingPresenter() {
        setModel(new MobileBindingModel());
    }


    @Override
    public void getValidCode(String mobile, int type) {
        if (!TxtUtil.isChinaPhoneLegal(mobile)) {
            view.onErrorTip(BaseApp.getInstance().getString(R.string.incorrect_phone_num));
            return;
        }
        if (!validateToken()) {
            return;
        }
        view.showProDialog();
        model.getSucrityCode(BaseApp.getInstance().getUser().getToken(), mobile, type, CommonConst.ThirdType.TYPE_NONE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                            view.onInvalidate(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onObtainedCode(aBoolean);
                    }
                });
    }

    @Override
    public void checkMobile(String mobile, String validcode) {
        if (!validateToken()) {
            return;
        }
        model.checkMobile(BaseApp.getInstance().getUser().getToken(), mobile, validcode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onInvalidate(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onCheckMobile(aBoolean);
                    }
                });
    }

    @Override
    public void changeMobile(String mobile, String validCode) {
        if (!TxtUtil.isChinaPhoneLegal(mobile)) {
            view.onErrorTip(BaseApp.getInstance().getResources().getString(R.string.incorrect_phone_num));
            return;
        }
        if (!validateToken()) {
            return;
        }
        model.changeMobile(BaseApp.getInstance().getUser().getToken(), mobile, validCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onInvalidate(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onBindingMobile(aBoolean);
                    }
                });
    }

    @Override
    public void countDown() {
        model.countDown(60).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        view.onCountDown(-1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        view.onCountDown(integer);
                    }
                });
    }
}
