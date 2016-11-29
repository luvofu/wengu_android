package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.bean.CommentReply;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by XieWei on 2016/10/30.
 */

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.CommentReplyViewHolder> {
    private List<CommentReply> data;

    public CommentReplyAdapter() {
        data = new LinkedList<>();
    }

    public void addItems(List<CommentReply> items) {
        if (items == null || items.size() < 0) {
            return;
        }
        int position = data.size() - 1;
        data.addAll(items);
        notifyItemRangeChanged(position, items.size());
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public CommentReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView tv = new TextView(parent.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setPadding(10, 10, 10, 10);
        tv.setTextSize(12);
        return new CommentReplyViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(CommentReplyViewHolder holder, int position) {
        CommentReply item = data.get(position);
        int replyType = item.getReplyType();
        switch (replyType) {
            case 0:
                holder.setComment(item.getNickname(), item.getContent());
                break;
            case 1:
                holder.setReply(item.getNickname(), item.getReplyObjNickname(), item.getContent());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CommentReplyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public CommentReplyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }

        public void setComment(CharSequence nick, CharSequence content) {
            SpannableString ss = new SpannableString(nick + "：" + content);
            ss.setSpan(new ForegroundColorSpan(BaseApp.getInstance()
                            .getResources().getColor(R.color.front_hot_font)),
                    0, nick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(ss);
        }

        public void setReply(CharSequence nick, CharSequence replyNick, CharSequence replyContent) {
            String str = "回复";
            SpannableString ss = new SpannableString(nick + str + replyNick + "：" + replyContent);
            ss.setSpan(new ForegroundColorSpan(BaseApp.getInstance()
                            .getResources().getColor(R.color.front_hot_font)),
                    0, nick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            int start = (nick.length() + str.length());
            ss.setSpan(new ForegroundColorSpan(BaseApp.getInstance()
                            .getResources().getColor(R.color.front_hot_font)),
                    start, start + replyNick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(ss);
        }
    }
}
