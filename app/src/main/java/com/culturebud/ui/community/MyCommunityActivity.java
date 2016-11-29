package com.culturebud.ui.community;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.culturebud.BaseActivity;
import com.culturebud.BaseFragment;
import com.culturebud.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2016/11/19.
 */

public class MyCommunityActivity extends BaseActivity {
    private ViewPager vpPages;
    private TabLayout tlTab;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_community);
        ivBack = obtainViewById(R.id.iv_back);
        tlTab = obtainViewById(R.id.tl_tabs);
        vpPages = obtainViewById(R.id.vp_favorites);
        vpPages.setAdapter(new PagesAdapter(getFragmentManager()));
        tlTab.setupWithViewPager(vpPages, true);
        tlTab.setTabMode(TabLayout.MODE_FIXED);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    class PagesAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> data;
        private List<String> titles;

        public PagesAdapter(FragmentManager fm) {
            super(fm);
            data = new ArrayList<>(2);
            titles = new ArrayList<>(2);
            data.add(new MyPublishedFragment());
            data.add(new MyRelatedFragment());
            titles.add("我发布的");
            titles.add("与我相关");
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }

}
