package com.culturebud.ui.community;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.CommunityContract;
import com.culturebud.presenter.CommunityPresenter;

/**
 * Created by XieWei on 2016/10/31.
 */

public class CommunityActivity extends BaseActivity {
    private CommunityFragment cf = new CommunityFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fl_container, cf, "community");
        ft.hide(cf);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.show(cf);
        ft.commit();
    }
}
