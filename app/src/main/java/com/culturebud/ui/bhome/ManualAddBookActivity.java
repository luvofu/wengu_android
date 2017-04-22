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
import com.culturebud.bean.CheckedBook;
import com.culturebud.contract.ManualAddBookContract;
import com.culturebud.presenter.ManualAddBookPresenter;
import com.culturebud.widget.SettingItemView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_PHOTO_CROP;

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
    private int type;
    private CheckedBook checkedBook;

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
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        if (type == 1) {
            String json = intent.getStringExtra("book");
            if (TextUtils.isEmpty(json)) {
                finish();
            }
            checkedBook = new Gson().fromJson(json, CheckedBook.class);
            if (checkedBook != null) {
                if (!TextUtils.isEmpty(checkedBook.getCover())) {
                    sdvBookCover.setImageURI(checkedBook.getCover());
                }
                if (!TextUtils.isEmpty(checkedBook.getTitle())) {
                    etBookName.setText(checkedBook.getTitle());
                    etBookName.setSelection(etBookName.getText().length());
                }
                if (!TextUtils.isEmpty(checkedBook.getOriginTitle())) {
                    etNameOrign.setText(checkedBook.getOriginTitle());
                }
                if (!TextUtils.isEmpty(checkedBook.getSubTitle())) {
                    etSubtitle.setText(checkedBook.getSubTitle());
                }
                if (!TextUtils.isEmpty(checkedBook.getIsbn13())) {
                    etIsbn.setText(checkedBook.getIsbn13());
                }
                String authors = checkedBook.getAuthor();
                if (!TextUtils.isEmpty(authors)) {
                    String[] aths = authors.split("\\|");
                    if (aths != null) {
                        for (int i = 0; i < aths.length; i++) {
                            if (i == 0) {
                                etAuthors.get(i).setText(aths[i]);
                            } else {
                                addAuthorView(aths[i]);
                            }
                        }
                    }
                }
                String tras = checkedBook.getTranslator();
                if (!TextUtils.isEmpty(tras)) {
                    String[] trsArr = tras.split("\\|");
                    if (trsArr != null) {
                        for (int i = 0; i < trsArr.length; i++) {
                            if (i == 0) {
                                etTranslators.get(i).setText(trsArr[i]);
                            } else {
                                addTranslatorView(trsArr[i]);
                            }
                        }
                    }
                }
                if (!TextUtils.isEmpty(checkedBook.getPrice())) {
                    etPrice.setText(checkedBook.getPrice());
                }
                if (!TextUtils.isEmpty(checkedBook.getPublisher())) {
                    etPublisher.setText(checkedBook.getPublisher());
                }
                if (!TextUtils.isEmpty(checkedBook.getPubDate())) {
                    sivPubDate.setRightInfo(checkedBook.getPubDate());
                }
                if (!TextUtils.isEmpty(checkedBook.getBinding())) {
                    sivBinding.setRightInfo(checkedBook.getBinding());
                }
                if (!TextUtils.isEmpty(checkedBook.getPages())) {
                    etPages.setText(checkedBook.getPages());
                    etPages.setSelection(etPages.getText().length());
                }
                if (!TextUtils.isEmpty(checkedBook.getSummary())) {
                    etContentSummary.setText(checkedBook.getSummary());
                    etContentSummary.setSelection(etContentSummary.getText().length());
                }
                if (!TextUtils.isEmpty(checkedBook.getAuthorInfo())) {
                    etAuthorSummary.setText(checkedBook.getAuthorInfo());
                    etAuthorSummary.setText(etAuthorSummary.getText().length());
                }
            }
        }
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
        if (type == 0) {
            presenter.submitBook(photoUri, etBookName.getText().toString(), etNameOrign.getText().toString(),
                    etSubtitle.getText().toString(), etIsbn.getText().toString(), authors, translators,
                    etPrice.getText().toString(), etPublisher.getText().toString(), sivPubDate.getInfo(),
                    sivBinding.getInfo(), etPages.getText().toString(), etContentSummary.getText().toString(),
                    etAuthorSummary.getText().toString());
        } else {
            checkedBook.setTitle(etBookName.getText().toString());
            checkedBook.setOriginTitle(etNameOrign.getText().toString());
            checkedBook.setSubTitle(etSubtitle.getText().toString());
            checkedBook.setIsbn13(etIsbn.getText().toString());
            String author = "";
            for (String s : authors) {
                author = author + s + "|";
            }
            if (author.endsWith("|")) {
                author = author.substring(0, author.lastIndexOf("|"));
            }
            checkedBook.setAuthor(author);
            String translator = "";
            for (String s : translators) {
                translator = translator + s + "|";
            }
            if (translator.endsWith("|")) {
                translator = translator.substring(0, translator.lastIndexOf("|"));
            }
            checkedBook.setTranslator(translator);
            checkedBook.setPrice(etPrice.getText().toString());
            checkedBook.setPublisher(etPublisher.getText().toString());
            checkedBook.setPubDate(sivPubDate.getInfo());
            checkedBook.setBinding(sivBinding.getInfo());
            checkedBook.setPages(etPages.getText().toString());
            checkedBook.setSummary(etContentSummary.getText().toString());
            checkedBook.setAuthorInfo(etAuthorSummary.getText().toString());
            presenter.checkBook(checkedBook, photoUri);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_book_cover:
                aspectY = 8;
                aspectX = 5;
                outY = 720;
                outX = 480;
                showPhotoDialog();
                break;
            case R.id.iv_add_author: {
                addAuthorView(null);
                break;
            }
            case R.id.iv_add_translator: {
                addTranslatorView(null);
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

    private void addTranslatorView(String tra) {
        View view = getLayoutInflater().inflate(R.layout.add_translator, null);
        TextView tvTan = obtainViewById(view, R.id.tv_translator);
        tvTan.setText(tra == null ? "" : tra);
        etTranslators.add(obtainViewById(view, R.id.et_translator));
        ImageView ivSub = obtainViewById(view, R.id.iv_add_translator);
        ivSub.setImageResource(R.drawable.author_sub_selector);
        ivSub.setOnClickListener(sub -> llParent.removeView(view));
        ivSubTranslators.add(ivSub);
        int index = llParent.indexOfChild(llAddTranslator);
        llParent.addView(view, index + 1);
    }

    private void addAuthorView(String author) {
        View view = getLayoutInflater().inflate(R.layout.add_author, null);
        TextView tvAut = obtainViewById(view, R.id.tv_author);
        tvAut.setText(author == null ? "" : author);
        etAuthors.add(obtainViewById(view, R.id.et_author));
        ImageView ivSub = obtainViewById(view, R.id.iv_add_author);
        ivSub.setImageResource(R.drawable.author_sub_selector);
        ivSub.setOnClickListener(sub -> llParent.removeView(view));
        ivSubAuthors.add(ivSub);
        int index = llParent.indexOfChild(llAddAuthor);
        llParent.addView(view, index + 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
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
    public void onCheckResult(boolean res) {
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
