package com.culturebud.ui.me;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.culturebud.BaseActivity;
import com.culturebud.BaseFragment;
import com.culturebud.R;
import com.culturebud.ui.bhome.BookSheetsFragment;
import com.culturebud.ui.bhome.BooksFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2016/11/17.
 */

public class MyFavoritesActivity extends BaseActivity {
    private ImageView ivBack;
    private TabLayout tlTabs;
    private ViewPager vpFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_favorites);
        ivBack = obtainViewById(R.id.iv_back);
        tlTabs = obtainViewById(R.id.tl_tabs);
        vpFavorites = obtainViewById(R.id.vp_favorites);
        vpFavorites.setAdapter(new PagesAdapter(getFragmentManager()));
        tlTabs.setupWithViewPager(vpFavorites, true);
        tlTabs.setTabMode(TabLayout.MODE_FIXED);
        ivBack.setOnClickListener(this);
        vpFavorites.setOnTouchListener((v, event) -> true);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class PagesAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> data;
        private List<String> titles;

        public PagesAdapter(FragmentManager fm) {
            super(fm);
            data = new ArrayList<>(2);
            titles = new ArrayList<>(2);
            data.add(new BooksFragment());
            data.add(new BookSheetsFragment());
            titles.add("书籍");
            titles.add("书单");
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
