package com.culturebud;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.culturebud.contract.BasePresenter;
import com.culturebud.ui.MainActivity;
import com.culturebud.ui.me.LoginActivity;
import com.culturebud.util.ClassUtil;
import com.culturebud.util.SystemParameterUtil;
import com.culturebud.widget.NoDataView;


/**
 * Created by XieWei on 2016/10/20.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment {
    protected static final int REQUEST_LOGIN = 1001;
    private TextView tvTitle;
    private ViewStub vsContainer, vsTitleLeft, vsTitleRight;
    protected P presenter;

    private NoDataView noDataView;

    public NoDataView getNoDataView() {
        return noDataView;
    }

    public void setNoDataView(NoDataView noDataView) {
        this.noDataView = noDataView;
    }

    public void setNoDataView(int resID, View view) {
        this.noDataView = (NoDataView) view.findViewById(resID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.abs_title_page, container, false);
        vsContainer = (ViewStub) view.findViewById(R.id.vs_container);
        tvTitle = (TextView) view.findViewById(R.id.et_title);
        vsTitleLeft = (ViewStub) view.findViewById(R.id.vs_title_left);
        vsTitleRight = (ViewStub) view.findViewById(R.id.vs_title_right);

        View statusBarView = view.findViewById(R.id.customStatusbar);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            int statusBarheight = SystemParameterUtil.getStatusHeight(getActivity());
            Log.d("statusbar height:", String.valueOf(statusBarheight));
            if (statusBarheight > 0) {
                ViewGroup.LayoutParams layoutParams = statusBarView.getLayoutParams();
                layoutParams.height = statusBarheight;
                statusBarView.setLayoutParams(layoutParams);
            }
        } else {
            statusBarView.setVisibility(View.GONE);
        }

        return view;
    }

    protected void inflateView(@LayoutRes int layout) {
        vsContainer.setLayoutResource(layout);
        vsContainer.inflate();
    }

    protected void initTitleLeft(@LayoutRes int layout) {
        vsTitleLeft.setLayoutResource(layout);
        vsTitleLeft.inflate();
    }

    protected void initTitleRight(@LayoutRes int layout) {
        vsTitleRight.setLayoutResource(layout);
        vsTitleRight.inflate();
    }

    protected void showTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    public TextView getTileView() {
        return tvTitle;
    }

    private void initPresenter() {
//        presenter = ClassUtil.getClassInstance(this, 0);
        presenter = ClassUtil.getPresenter(this);
    }

    public void onToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    public void onErrorTip(String tip) {
        if (!TextUtils.isEmpty(tip)) {
            Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOGIN:
                if (resultCode == BaseActivity.RESULT_CANCELED_CUSTOM
                        && !(getActivity() instanceof MainActivity)) {
                    getActivity().finish();
                }
                break;
        }
    }

    public void showProDialog() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showProDialog();
        }
    }

    public void hideProDialog() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideProDialog();
        }
    }

    protected boolean onPopBack() {
        return false;
    }

    public void showLoadingView() {
        getNoDataView().showLoading();
    }

    public void showLoadingView(boolean showContentView) {
        getNoDataView().showLoading(showContentView);
    }

    public void showNoDataView(String nodataDesc) {
        getNoDataView().showNoDataView(nodataDesc);
    }

    public void hiddenNoDataView() {
        getNoDataView().hiddenNoDataView();
    }

    public void showErrorView(String errorDesc) {
        getNoDataView().setOnRetryClickListener(view -> {
            onRetryData();
        });

        getNoDataView().showErrorView(errorDesc);
    }

    public void showNoNetView() {
        getNoDataView().setOnRetryClickListener(view -> {
            onRetryData();
        });

        getNoDataView().showNoNetwork();
    }

    public void onRetryData() {
        //子类需要刷新的都需要重载.
    }

}
