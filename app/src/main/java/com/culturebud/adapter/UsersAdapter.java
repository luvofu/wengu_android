package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2016/12/26.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    private List<User> data;

    public UsersAdapter() {
        data = new ArrayList<>();
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    public void addItems(List<User> users) {
        if (users != null && !users.isEmpty()) {
            int position = data.size() - 1;
            data.addAll(users);
            notifyItemRangeChanged(position, users.size());
        }
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UsersViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_friends_item, parent, false));
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {
        User item = data.get(position);
        holder.setFace(item.getAvatar());
        holder.setNick(item.getNickname());
        holder.userId = item.getUserId();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvFace;
        private TextView tvNick;
        private long userId;

        public UsersViewHolder(View itemView) {
            super(itemView);
            sdvFace = (SimpleDraweeView) itemView.findViewById(R.id.sdv_friend_face);
            tvNick = (TextView) itemView.findViewById(R.id.tv_friend_nick);
            itemView.setOnClickListener(this);
        }

        public void setFace(String url) {
            if (!TextUtils.isEmpty(url)) {
                sdvFace.setImageURI(url);
            }
        }

        public void setNick(String nick) {
            if (!TextUtils.isEmpty(nick)) {
                tvNick.setText(nick);
            }
        }

        @Override
        public void onClick(View v) {

        }
    }
}
