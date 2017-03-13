package com.culturebud.ui.bhome;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;

/**
 * Created by XieWei on 2017/3/13.
 */

public class EditBookRatingActivity extends BaseActivity {
    private float rating;
    private String comment;
    private RatingBar rbRating;
    private TextView tvRating;
    private EditText etComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book_rating);
        showTitlebar();
        showBack();
        showOperas();
        setOperasText(R.string.confirm);
        rbRating = obtainViewById(R.id.rb_rating);
        tvRating = obtainViewById(R.id.tv_rating);
        etComment = obtainViewById(R.id.et_comment);

        LayerDrawable ld = (LayerDrawable) rbRating.getProgressDrawable();
        ld.getDrawable(0).setColorFilter(getResources().getColor(R.color.light_font_black), PorterDuff.Mode.SRC_ATOP);
        ld.getDrawable(1).setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        ld.getDrawable(2).setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);


        Intent intent = getIntent();
        String title = intent.getStringExtra("book_title");
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        rating = intent.getFloatExtra("rating", 0);
        comment = intent.getStringExtra("comment");
        if (rating > 0) {
            rbRating.setRating(rating / 2);
        }
        if (!TextUtils.isEmpty(comment)) {
            etComment.setText(comment);
            etComment.setSelection(comment.length());
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        float rating = rbRating.getRating();
        String comment = etComment.getText().toString();
        if (rating > 0 && rating != this.rating
                || !TextUtils.isEmpty(comment) && comment.equals(this.comment)) {
            Intent data = new Intent();
            data.putExtra("rating", rating);
            data.putExtra("comment", comment);
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
