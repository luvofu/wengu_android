package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.ManualAddBookContract;
import com.culturebud.presenter.ManualAddBookPresenter;
import com.culturebud.widget.SettingItemView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by XieWei on 2016/12/9.
 */

@PresenterInject(ManualAddBookPresenter.class)
public class ManualAddBookActivity extends BaseActivity<ManualAddBookContract.Presenter>
        implements ManualAddBookContract.View, OptionsPickerView.OnOptionsSelectListener {
    private LinearLayout llParent;
    private LinearLayout llAddCover;
    private SimpleDraweeView sdvBookCover;

    private EditText etBookName, etNameOrign, etSubtitle;
    private EditText etIsbn;

    private ImageView ivAddAuthor, ivAddTranslator;
    private LinearLayout llAddAuthor, llAddTranslator;
    private List<EditText> etAuthors, etTranslators;
    private List<ImageView> ivSubAuthors, ivSubTranslators;

    private EditText etPrice, etPublisher, etPages;
    private SettingItemView sivPubDate, sivBinding;
    private EditText etContentSummary, etAuthorSummary;

    private BottomSheetDialog pubDateDlg;
    private OptionsPickerView<String> bindingDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_add_book);
        presenter.setView(this);
        showTitlebar();
        showBack();
        showOperas();
        setOperasText(R.string.commit_verify);

        etAuthors = new ArrayList<>();
        etTranslators = new ArrayList<>();
        ivSubAuthors = new ArrayList<>();
        ivSubTranslators = new ArrayList<>();

        llParent = obtainViewById(R.id.ll_parent);
        llAddCover = obtainViewById(R.id.ll_book_cover);
        sdvBookCover = obtainViewById(R.id.sdv_book_sheet_cover);
        etBookName = obtainViewById(R.id.et_book_name);
        etNameOrign = obtainViewById(R.id.et_name_orgin);
        etSubtitle = obtainViewById(R.id.et_subtitle);
        etIsbn = obtainViewById(R.id.et_isbn);
        llAddAuthor = obtainViewById(R.id.ll_add_author);
        llAddTranslator = obtainViewById(R.id.ll_add_translator);
        etAuthors.add(obtainViewById(R.id.et_author));
        etTranslators.add(obtainViewById(R.id.et_translator));
        ivAddAuthor = obtainViewById(R.id.iv_add_author);
        ivAddTranslator = obtainViewById(R.id.iv_add_translator);
        etPrice = obtainViewById(R.id.et_price);
        etPublisher = obtainViewById(R.id.et_publisher);
        etPages = obtainViewById(R.id.et_pages);
        sivPubDate = obtainViewById(R.id.siv_pub_date);
        sivBinding = obtainViewById(R.id.siv_binding);
        etContentSummary = obtainViewById(R.id.et_content_summary);
        etAuthorSummary = obtainViewById(R.id.et_author_summary);

        setListeners();
    }

    private void setListeners() {
        llAddCover.setOnClickListener(this);
        ivAddAuthor.setOnClickListener(this);
        ivAddTranslator.setOnClickListener(this);
        sivBinding.setOnClickListener(this);
        sivPubDate.setOnClickListener(this);
    }

    private ArrayList<String> items = new ArrayList<>();

    private void initBindingDlg(int binding) {
        if (bindingDlg == null) {
            bindingDlg = new OptionsPickerView<>(this);
            items.add("平装");
            items.add("精装");
            items.add("简装");
            items.add("其他");
            bindingDlg.setPicker(items);
            bindingDlg.setSelectOptions(binding);
            bindingDlg.setCyclic(false);
            bindingDlg.setOnoptionsSelectListener(this);
        } else {
            bindingDlg.setSelectOptions(binding);
        }
    }

    private void initPubDateDlg() {
        if (pubDateDlg == null) {
            pubDateDlg = new BottomSheetDialog(this);
            pubDateDlg.setContentView(R.layout.pub_date_popup);
            pubDateDlg.setCancelable(true);
            DatePicker dpPubDate = (DatePicker) pubDateDlg.getWindow().findViewById(R.id.dp_pub_date);
            Calendar calendar = Calendar.getInstance();
            dpPubDate.updateDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            pubDateDlg.getWindow().findViewById(R.id.tv_cancel)
                    .setOnClickListener(view -> {
                        if (pubDateDlg.isShowing()) {
                            pubDateDlg.dismiss();
                        }
                    });
            pubDateDlg.getWindow().findViewById(R.id.tv_confirm)
                    .setOnClickListener(view -> {
                        if (pubDateDlg.isShowing()) {
                            pubDateDlg.dismiss();
                        }
                        pubDate = dpPubDate.getYear() + "." + (dpPubDate.getMonth() + 1);
                        sivPubDate.setRightInfo(pubDate);
                    });
        }
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        List<String> authors = new ArrayList<>();
        for (EditText et : etAuthors) {
            if (!TextUtils.isEmpty(et.getText().toString())) {
                authors.add(et.getText().toString());
            }
        }

        List<String> translators = new ArrayList<>();
        for (EditText et : etTranslators) {
            if (!TextUtils.isEmpty(et.getText().toString())) {
                translators.add(et.getText().toString());
            }
        }
        presenter.submitBook(photoUri, etBookName.getText().toString(), etNameOrign.getText().toString(),
                etSubtitle.getText().toString(), etIsbn.getText().toString(), authors, translators,
                etPrice.getText().toString(), etPublisher.getText().toString(), sivPubDate.getInfo(),
                sivBinding.getInfo(), etPages.getText().toString(), etContentSummary.getText().toString(),
                etAuthorSummary.getText().toString());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_book_cover:
                showPhotoDialog();
                break;
            case R.id.iv_add_author: {
                View view = getLayoutInflater().inflate(R.layout.add_author, null);
                TextView tvAut = obtainViewById(view, R.id.tv_author);
                tvAut.setText("");
                etAuthors.add(obtainViewById(view, R.id.et_author));
                ImageView ivSub = obtainViewById(view, R.id.iv_add_author);
                ivSub.setImageResource(R.drawable.author_sub_selector);
                ivSub.setOnClickListener(sub -> {
                    llParent.removeView(view);
                });
                ivSubAuthors.add(ivSub);
                int index = llParent.indexOfChild(llAddAuthor);
                llParent.addView(view, index + 1);
                break;
            }
            case R.id.iv_add_translator: {
                View view = getLayoutInflater().inflate(R.layout.add_translator, null);
                TextView tvTan = obtainViewById(view, R.id.tv_translator);
                tvTan.setText("");
                etTranslators.add(obtainViewById(view, R.id.et_translator));
                ImageView ivSub = obtainViewById(view, R.id.iv_add_translator);
                ivSub.setImageResource(R.drawable.author_sub_selector);
                ivSub.setOnClickListener(sub -> {
                    llParent.removeView(view);
                });
                ivSubTranslators.add(ivSub);
                int index = llParent.indexOfChild(llAddTranslator);
                llParent.addView(view, index + 1);
                break;
            }
            case R.id.siv_binding: {
                initBindingDlg(0);
                if (!bindingDlg.isShowing()) {
                    bindingDlg.show();
                }
                break;
            }
            case R.id.siv_pub_date: {
                initPubDateDlg();
                if (!pubDateDlg.isShowing()) {
                    pubDateDlg.show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    sdvBookCover.setImageURI(photoUri);
                }
                break;
            case REQUEST_CODE_PHOTO_CROP:
                if (resultCode == RESULT_OK) {
                    sdvBookCover.setImageURI(photoUri);

                }
                break;
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onAddResult(boolean res) {
        if (res) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onOptionsSelect(int options1, int option2, int options3) {
        sivBinding.setRightInfo(items.get(options1));
    }

    private String pubDate = "";

}
