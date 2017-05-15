package com.culturebud;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Rect;
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


/**
 * Created by XieWei on 2016/10/20.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment {
    protected static final int REQUEST_LOGIN = 1001;
    private TextView tvTitle;
    private ViewStub vsContainer, vsTitleLeft, vsTitleRight;
    protected P presenter;

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
            int statusBarheight = BaseFragment.getStatusHeight(getActivity());
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

    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;

        if (activity == null) {
            return statusHeight;
        }

        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
}
