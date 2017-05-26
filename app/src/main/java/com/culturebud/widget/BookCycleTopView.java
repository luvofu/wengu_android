package com.culturebud.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.User;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Dana on 17/5/26.
 */

public class BookCycleTopView extends ScaleRelativeLayout implements View.OnClickListener {
    private TextView nicknameTV, autographTV, myFollowedTV, followedTV;
    private SimpleDraweeView avatarView, backgroundImage;

    boolean editable = false;

    public void setUserInfo(User user) {
        avatarView.setImageURI(user.getAvatar());
        nicknameTV.setText(user.getNickname());
        autographTV.setText(TextUtils.isEmpty(user.getAutograph()) ? getContext().getString(R.string.autograph_hint) : user.getAutograph());

        backgroundImage.setImageURI(user.getBackground());

        String concernNumDescString = getContext().getString(R.string.concern_title) + String.valueOf(user.getConcernNum());
        myFollowedTV.setText(concernNumDescString);

        String fansNumDescString = getContext().getString(R.string.concerned_title) + String.valueOf(user.getFanNum());
        followedTV.setText(fansNumDescString);

        int resid = -1;
        if (user.getSex() == 0) {
            resid = R.mipmap.setting_me_male_icon;
        } else {
            resid = R.mipmap.setting_me_female_icon;
        }
        setSexIcon(resid);
    }

    public void setSexIcon(int resid) {
        Drawable sexicon = ContextCompat.getDrawable(getContext(), resid);
        if (sexicon != null) {
            sexicon.setBounds(0, 0, sexicon.getIntrinsicWidth(), sexicon.getIntrinsicHeight());
            nicknameTV.setCompoundDrawables(null, null, sexicon, null);
        }
    }

    public BookCycleTopView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public BookCycleTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = View.inflate(getContext(), R.layout.bookcycletopview, this);

        backgroundImage = (SimpleDraweeView) view.findViewById(R.id.background_image);
        autographTV = (TextView) view.findViewById(R.id.autograph);
        myFollowedTV = (TextView) view.findViewById(R.id.myfollow);
        followedTV = (TextView) view.findViewById(R.id.followed);
        nicknameTV = (TextView) view.findViewById(R.id.nick_name);
        avatarView = (SimpleDraweeView) view.findViewById(R.id.avatarview);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.BookCycleTopView);
        editable = typedArray.getBoolean(R.styleable.BookCycleTopView_editable, false);

        typedArray.recycle();

        setListeners();
    }

    private void setListeners() {
        if (editable) {
            myFollowedTV.setClickable(true);
            followedTV.setClickable(true);
            backgroundImage.setClickable(true);
            avatarView.setClickable(true);

            backgroundImage.setOnClickListener(this);
            avatarView.setOnClickListener(this);
            myFollowedTV.setOnClickListener(this);
            followedTV.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (topViewListeners == null) {
            return;
        }

        switch (v.getId()) {
            case R.id.background_image: {
                topViewListeners.onBackgroundClicked(backgroundImage);
                break;
            }
            case R.id.myfollow: {
                topViewListeners.onMyFollowedClicked();
                break;
            }
            case R.id.followed: {
                topViewListeners.onFollowedClicked();
                break;
            }
            case R.id.avatarview: {
                topViewListeners.onAvatarClicked();
                break;
            }
        }
    }

    private BookCycleTopViewListeners topViewListeners;

    public BookCycleTopViewListeners getTopViewListeners() {
        return topViewListeners;
    }

    public void setTopViewListeners(BookCycleTopViewListeners topViewListeners) {
        this.topViewListeners = topViewListeners;
    }

    public interface BookCycleTopViewListeners {
        //        点击背景
        void onBackgroundClicked(ImageView backgroundImageView);

        //        点击头像
        void onAvatarClicked();

        //        点击我关注的
        void onMyFollowedClicked();

        //        点击关注我的
        void onFollowedClicked();
    }
}
