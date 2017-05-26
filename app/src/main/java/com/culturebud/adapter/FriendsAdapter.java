package com.culturebud.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.bean.Friend;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2016/12/5.
 */

public class FriendsAdapter extends RecyclerView.Adapter {
    private List<Friend> data = new ArrayList<>();
    private boolean hasOperas = false;

    public FriendsAdapter(boolean hasOperas) {
        this.hasOperas = hasOperas;
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
        }
    }

    public void addItems(List<Friend> friends) {
        if (friends != null && !friends.isEmpty()) {
            data.addAll(friends);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = new FriendViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FriendViewHolder fvHolder = (FriendViewHolder) holder;
        Friend friend = data.get(position);
        fvHolder.setFriend(friend);
        fvHolder.setFriendFace(friend.getAvatar());
        fvHolder.setNick(friend.getNickname());
        fvHolder.setTvInfo(friend.getFanNum() + "人关注");
        fvHolder.setTvOpeas();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Friend friend;
        private SimpleDraweeView sdvFriendFace;
        private LinearLayout llInfo;
        private TextView tvNick;
        private TextView tvInfo;
        private TextView tvOpt;

        public FriendViewHolder(View itemView) {
            super(itemView);
            sdvFriendFace = (SimpleDraweeView) itemView.findViewById(R.id.sdv_friend_face);
            llInfo = (LinearLayout) itemView.findViewById(R.id.ll_info);
            tvNick = (TextView) itemView.findViewById(R.id.tv_friend_nick);
            tvInfo = (TextView) itemView.findViewById(R.id.tv_friend_info);
            tvOpt = (TextView) itemView.findViewById(R.id.tv_opt);

            sdvFriendFace.setOnClickListener(this);
            llInfo.setOnClickListener(this);
            tvOpt.setOnClickListener(this);
        }

        public void setFriend(Friend friend) {
            this.friend = friend;
        }

        public void setFriendFace(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            sdvFriendFace.setImageURI(url);
        }

        public void setNick(String nick) {
            if (TextUtils.isEmpty(nick)) {
                return;
            }
            tvNick.setText(nick);
        }

        public void setTvInfo(String info) {
            if (TextUtils.isEmpty(info)) {
                return;
            }
            this.tvInfo.setText(info);
        }

        public void setTvOpeas() {
            if (hasOperas) {
                int resId = R.mipmap.ic_friend_concern;
                String text = CommonConst.getConcernTitle(friend.getConcernStatus());
                int color = BaseApp.getInstance().getResources().getColor(R.color.green);
                switch (friend.getConcernStatus()) {
                    case CommonConst.ConcernStatus.SINGLE_CONCERN_STATUS:
                        resId = R.mipmap.ic_friend_concerned;
                        color = BaseApp.getInstance().getResources().getColor(R.color.font_black_light);
                        break;
                    case CommonConst.ConcernStatus.EACH_CONCERN_STATUS:
                        resId = R.mipmap.ic_friend_eachconcerned;
                        color = BaseApp.getInstance().getResources().getColor(R.color.font_black_light);
                        break;
                }
                Drawable top = BaseApp.getInstance().getResources().getDrawable(resId);
                top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight());
                tvOpt.setCompoundDrawables(null, top, null, null);
                tvOpt.setText(text);
                tvOpt.setTextColor(color);
                tvOpt.setVisibility(View.VISIBLE);
            } else {
                tvOpt.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                switch (v.getId()) {
                    case R.id.sdv_friend_face:
                    case R.id.ll_info:
                        onItemClickListener.onItemClick(v, friend, 0);
                        break;
                    case R.id.tv_opt:
                        onItemClickListener.onItemClick(v, friend, 1);
                        break;
                }
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Friend friend, int opt);
    }
}
