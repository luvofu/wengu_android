package com.culturebud.contract;

/**
 * Created by XieWei on 2016/10/21.
 */

public interface BaseView {

    void showProDialog();

    void hideProDialog();

    void onToLogin();

    void onErrorTip(String tip);
}
