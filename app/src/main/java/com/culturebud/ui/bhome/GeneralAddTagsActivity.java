package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.EditableTagsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.GeneralAddTagsContract;
import com.culturebud.presenter.GeneralAddTagsPresenter;
import com.culturebud.vo.Tag;
import com.culturebud.widget.FlowLayout;
import com.culturebud.widget.TagAdapter;
import com.culturebud.widget.TagFlowLayout;

import java.util.List;

/**
 * Created by XieWei on 2017/3/13.
 */

@PresenterInject(GeneralAddTagsPresenter.class)
public class GeneralAddTagsActivity extends BaseActivity<GeneralAddTagsContract.Presenter> implements
        GeneralAddTagsContract.View, EditableTagsAdapter.OnTagClickListener, TagFlowLayout.OnTagClickListener {
    private View contentRoot;
    private TagFlowLayout tflTags, tflRecommendTags, tflHistoryTags;
    private TextView tvClearHistory;

    private int type;//0 书籍标签， 1 书单标签
    private String[] tags;
    private EditableTagsAdapter etAdapter;

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

        contentRoot = obtainViewById(R.id.ll_root);
        tflTags = obtainViewById(R.id.tfl_tags);
        tflRecommendTags = obtainViewById(R.id.tfl_recommend_tags);
        tflHistoryTags = obtainViewById(R.id.tfl_history_tags);

        initData();
        contentRoot.setOnClickListener(this);
        tflRecommendTags.setOnTagClickListener(this);
        tflHistoryTags.setOnTagClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        String tmp = intent.getStringExtra("tag");
        if (type == 0) {
            presenter.getBookTags();
        } else {
            presenter.getBookSheetTags();
        }
        if (!TextUtils.isEmpty(tmp)) {
            tags = tmp.split("\\|");
        }

        etAdapter = new EditableTagsAdapter();
        etAdapter.setOnTagClickListener(this);
        tflTags.setAdapter(etAdapter);
        if (tags != null) {
            for (String s : tags) {
                Tag t = new Tag();
                t.setContent(s);
                etAdapter.addTag(t);
            }
        }
        if (etAdapter.getCount() < 3) {
            etAdapter.addTag(new Tag());
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Log.d("xiewei", "onClick()");
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        Intent data = new Intent();
        String tags = "";
        for (Tag t : etAdapter.getTags()) {
            if (TextUtils.isEmpty(t.getContent())) {
                continue;
            }
            tags = tags + t.getContent() + "|";
        }
        if (tags.endsWith("|")) {
            tags = tags.substring(0, tags.lastIndexOf("|"));
        }
        data.putExtra("tag", tags);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onTags(List<String> tags) {
        TagAdapter adapter = new TagAdapter<String>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, String tag) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.book_category_item, null);
                TextView tvTag = (TextView) view;
                tvTag.setText(tag);
                return view;
            }
        };
        tflRecommendTags.setAdapter(adapter);
    }

    @Override
    public void onHistoryTags(List<String> tags) {

    }

    @Override
    public void onTagClick(View v, Tag tag) {
        etAdapter.delTag(tag);
        if (etAdapter.getCount() < 3 && etAdapter.getLastTag() != null && !TextUtils.isEmpty(etAdapter.getLastTag()
                .getContent())) {
            etAdapter.addTag(new Tag());
        }
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        String r = ((TagFlowLayout) parent).getAdapter().getItem(position).toString();
        if (parent == tflRecommendTags) {
            if (etAdapter.delByTag(r)) {
                if (etAdapter.getLastTag().getContent() != null) {
                    etAdapter.addTag(new Tag());
                }
                return true;
            }
        } else if (parent == tflHistoryTags) {
            if (etAdapter.delByTag(r)) {
                if (etAdapter.getLastTag().getContent() != null) {
                    etAdapter.addTag(new Tag());
                }
                return true;
            }
        }
        if (etAdapter.getCount() >= 3) {
            Tag t = etAdapter.getLastTag();
            if (TextUtils.isEmpty(t.getContent())) {
                t.setContent(((TagFlowLayout) parent).getAdapter().getItem(position).toString());
                etAdapter.notifyDataChanged();
                if (parent == tflRecommendTags) {
                    View v = obtainViewById(view, R.id.tv_tag);
                } else if (parent == tflHistoryTags) {

                }
                return true;
            } else {
                return false;
            }
        } else {
            Tag t = new Tag();
            t.setContent(((TagFlowLayout) parent).getAdapter().getItem(position).toString());
            etAdapter.addTag(t, etAdapter.getCount() - 1);
            return true;
        }
    }
}
