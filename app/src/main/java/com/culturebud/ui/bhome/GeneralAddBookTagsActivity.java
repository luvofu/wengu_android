package com.culturebud.ui.bhome;

import android.os.Bundle;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.GeneralAddBookTagsContract;
import com.culturebud.presenter.GeneralAddBookTagsPresenter;
import com.culturebud.widget.TagFlowLayout;

import java.util.List;

/**
 * Created by XieWei on 2017/3/13.
 */

@PresenterInject(GeneralAddBookTagsPresenter.class)
public class GeneralAddBookTagsActivity extends BaseActivity<GeneralAddBookTagsContract.Presenter> implements
        GeneralAddBookTagsContract.View {
    private TagFlowLayout tflTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_add_book_tags);
        showTitlebar();
        showBack();
        showOperas();
        setTitle(R.string.set_tags);
        setOperasText(R.string.confirm);
        presenter.setView(this);

        tflTags = obtainViewById(R.id.tfl_tags);

    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);

    }

    @Override
    public void onTags(List<String> tags) {

    }
}
