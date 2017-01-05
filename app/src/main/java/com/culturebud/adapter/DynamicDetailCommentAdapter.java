package com.culturebud.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.adapter.DynamicDetailCommentAdapter.DynamicDetailCommentViewHolder;
import com.culturebud.bean.DynamicReply;
import com.culturebud.util.WidgetUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2017/1/4.
 */

public class DynamicDetailCommentAdapter extends RecyclerView.Adapter<DynamicDetailCommentViewHolder> {
    private List<DynamicReply> data;

    public DynamicDetailCommentAdapter() {
        data = new ArrayList<>();
    }

    public void addItems(List<DynamicReply> items) {
        if (items != null && !items.isEmpty()) {
            int position = data.size() - 1;
            data.addAll(items);
            notifyItemRangeChanged(position, items.size());
        }
    }

    public void insertItem(DynamicReply dynamicReply) {
        if (dynamicReply == null) {
            return;
        }
        data.add(0, dynamicReply);
        notifyItemInserted(0);
    }

    @Override
    public DynamicDetailCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DynamicDetailCommentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dynamic_detail_comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(DynamicDetailCommentViewHolder holder, int position) {
        DynamicReply item = data.get(position);
        holder.setFace(item.getAvatar());
        holder.setNick(item.getNickname());
        holder.setReplyContent(item.getContent());
        holder.setCreatedTime(item.getCreatedTime());
        if (item.getReplies() != null && !item.getReplies().isEmpty()) {
            Log.d("xwlljj", "reply replies is not null, size is " + item.getReplies().size());
            holder.inflateRepliesView();
            holder.setReplies(item.getReplies());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DynamicDetailCommentViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView sdvFace;
        private TextView tvNick, tvCreatedTime, tvReplyContent;
        private TextView tvReply;
        private ViewStub vsReplies;
        private RecyclerView rvReplies;

        public DynamicDetailCommentViewHolder(View itemView) {
            super(itemView);
            sdvFace = WidgetUtil.obtainViewById(itemView, R.id.sdv_face);
            tvNick = WidgetUtil.obtainViewById(itemView, R.id.tv_nick);
            tvCreatedTime = WidgetUtil.obtainViewById(itemView, R.id.tv_created_time);
            tvReplyContent = WidgetUtil.obtainViewById(itemView, R.id.tv_reply_content);
            tvReply = WidgetUtil.obtainViewById(itemView, R.id.tv_reply);
            vsReplies = WidgetUtil.obtainViewById(itemView, R.id.vs_replies);
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

        public void setCreatedTime(long createdTime) {
            tvCreatedTime.setText(sdf.format(new Date(createdTime)));
        }

        public void setReplyContent(String content) {
            if (!TextUtils.isEmpty(content)) {
                tvReplyContent.setText(content);
            }
        }

        public void inflateRepliesView() {
            Log.d("xwlljj", "inflateRepliesView()");
            if (rvReplies == null) {
                Log.d("xwlljj", "inflateRepliesView() ---> first");
                vsReplies.setLayoutResource(R.layout.book_circle_item_comments);
                View view = vsReplies.inflate();
                rvReplies = WidgetUtil.obtainViewById(view, R.id.rv_comments);
                LinearLayoutManager llm = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
                rvReplies.setLayoutManager(llm);
                DynamicCommentAdapter adapter = new DynamicCommentAdapter();
//                adapter.setOnItemClickListener(this);
                rvReplies.setAdapter(adapter);
            }
        }

        public void setReplies(List<DynamicReply> replies) {
            Log.d("xwlljj", "setReplies() --> replies size is " + replies.size());
            ((DynamicCommentAdapter) rvReplies.getAdapter()).clearData();
            ((DynamicCommentAdapter) rvReplies.getAdapter()).addItems(replies);
        }
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm", Locale.getDefault());
}
