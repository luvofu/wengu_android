package com.culturebud.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.classic.common.MultipleStatusView;

/**
 * Created by Dana on 17/5/21.
 */

public class NoDataView extends MultipleStatusView {

    private TextView nodataTextView;
    private TextView errorTextView;

    public void setNodataTitle(String nodataTitle) {
        nodataTextView.setText(nodataTitle);
    }

    public void  setErrorTitle(String errorTitle) {
        errorTextView.setText(errorTitle);
    }

    public NoDataView(Context context) {
        this(context, null);
    }

    public NoDataView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showNoDataView(String nodataTitle) {
        showEmpty();

        nodataTextView = (TextView) findViewById(com.classic.common.R.id.empty_view_tv);

        if (nodataTextView != null) {
            setNodataTitle(nodataTitle);
        }
    }

    public void  showErrorView(String errorDesc) {
        showError();

        errorTextView = (TextView)findViewById(com.classic.common.R.id.error_view_tv);

        if (errorTextView != null) {
            setErrorTitle(errorDesc);
        }
    }

    public void hiddenNoDataView() {
        showContent();

        setOnRetryClickListener(null);
    }
}
