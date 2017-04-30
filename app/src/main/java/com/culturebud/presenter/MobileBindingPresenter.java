package com.culturebud.presenter;

import com.culturebud.contract.MobileBindingContract;
import com.culturebud.model.MobileBindingModel;
import com.culturebud.util.ApiException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MobileBindingPresenter extends MobileBindingContract.Presenter {

    public MobileBindingPresenter() {
        setModel(new MobileBindingModel());
    }


    @Override
    public void getValidcode(String token,String mobile,int type) {
        model.getSucrityCode(token, mobile, type).subscribeOn(Schedulers.io())
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
                        view.onObtainedCode(aBoolean);
                    }
                });
    }

    @Override
    public void checkMobile(String token, String mobile, String validcode) {
        model.checkMobile(token, mobile, validcode).subscribeOn(Schedulers.io())
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
    public void changeMobile(String token, String mobile, String validcode) {
        model.changeMobile(token, mobile, validcode).subscribeOn(Schedulers.io())
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

}
