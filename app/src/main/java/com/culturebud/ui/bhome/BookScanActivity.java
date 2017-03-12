package com.culturebud.ui.bhome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.ScanBookAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Book;
import com.culturebud.contract.ScanBookContract;
import com.culturebud.presenter.ScanBookPresenter;
import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.Locale;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_ADD_BOOK_MANUAL;

/**
 * Created by XieWei on 2016/11/25.
 */

@PresenterInject(ScanBookPresenter.class)
public class BookScanActivity extends BaseActivity<ScanBookContract.Presenter> implements ScanBookContract.View {
    private CaptureFragment captureFragment;
    private RecyclerView rvScanResults;
    private TextView tvScanCount, tvConfirm;
    private ImageView ivCancel, ivLighting;
    private TextView tvSearch, tvManual;
    private LinearLayout llOpera, llConfirm;
    private ScanBookAdapter adapter;
    private boolean isOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_scan);
        presenter.setView(this);
        rvScanResults = obtainViewById(R.id.rv_scan_results);
        ivCancel = obtainViewById(R.id.iv_cancel);
        ivLighting = obtainViewById(R.id.iv_lighting);
        tvScanCount = obtainViewById(R.id.tv_scan_desc);
        tvConfirm = obtainViewById(R.id.tv_confirm);
        tvSearch = obtainViewById(R.id.tv_search);
        tvManual = obtainViewById(R.id.tv_manual);
        llOpera = obtainViewById(R.id.ll_opera);
        llConfirm = obtainViewById(R.id.ll_confirm);

        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.camera_scan);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, captureFragment)
                .commit();
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvScanResults.setLayoutManager(llm);
//        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);

        adapter = new ScanBookAdapter();
        rvScanResults.setAdapter(adapter);
        setListeners();
    }

    private void setListeners() {
        ivCancel.setOnClickListener(this);
        ivLighting.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        tvManual.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_cancel:
                finish();
                break;
            case R.id.iv_lighting:
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }
                break;
            case R.id.tv_search:
                break;
            case R.id.tv_manual: {
                Intent intent = new Intent(this, ManualAddBookActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_BOOK_MANUAL);
                break;
            }
            case R.id.tv_confirm: {
                Intent intent = new Intent(this, BookScanResultActivity.class);
                intent.putExtra("books", new Gson().toJson(adapter.getData()));
                startActivity(intent);
                break;
            }
        }
    }

    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            presenter.scanBook(result);
        }

        @Override
        public void onAnalyzeFailed() {
            onErrorTip("未识别");
            captureFragment.getHandler().sendEmptyMessageDelayed(R.id.restart_preview, 3000);
        }
    };


    @Override
    public void onScanBook(Book book) {
        if (book != null) {
            if (adapter.hasAdded(book)) {
                onErrorTip("已扫描");
            } else {
                adapter.addItem(book);
                tvScanCount.setText(String.format(Locale.getDefault(),
                        getString(R.string.scan_result_desc),
                        adapter.getItemCount(), adapter.getItemCount()));
                if (llConfirm.getVisibility() != View.VISIBLE) {
                    llConfirm.setVisibility(View.VISIBLE);
                    llOpera.setVisibility(View.GONE);
                }
            }
        }
        captureFragment.getHandler().sendEmptyMessageDelayed(R.id.restart_preview, 3000);
    }

    private Toast toast;

    @Override
    public void onNotExitsBook(String tip, String isbn) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, tip, Toast.LENGTH_SHORT);
        toast.show();
        captureFragment.getHandler().sendEmptyMessageDelayed(R.id.restart_preview, 3000);
    }

    @Override
    public void onScanFail() {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, "扫描失败", Toast.LENGTH_SHORT);
        toast.show();
        captureFragment.getHandler().sendEmptyMessageDelayed(R.id.restart_preview, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ADD_BOOK_MANUAL:
                if (resultCode == RESULT_OK) {
                    startActivity(new Intent(this, CollectedBooksActivity.class));
                    finish();
                }
                break;
        }
    }
}
