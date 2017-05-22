package com.culturebud.contract;

/**
 * Created by XieWei on 2016/10/21.
 */

public interface BaseView {

    void showProDialog();

    void hideProDialog();

    void onToLogin();

    void onErrorTip(String tip);

    //显示加载视图
    void  showLoadingView();

    //显示无数据视图
    void showNoDataView(String nodataDesc);

    //显示错误视图.
    void  showErrorView(String errorDesc);

    //显示无网络视图.
    void showNoNetView();

    //隐藏视图.
    void hiddenNoDataView();

    //无网络或错误视图点击之后回调.
    void onRetryData();
}
