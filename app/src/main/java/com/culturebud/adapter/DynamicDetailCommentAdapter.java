package com.culturebud.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.bean.BookCircleDynamic;
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

public class DynamicDetailCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private BookCircleDynamic dynamic;
    private List<DynamicReply> data;

    public DynamicDetailCommentAdapter() {
        data = new ArrayList<>();
    }

    public BookCircleDynamic getDynamic() {
        return dynamic;
    }

    public void setDynamic(BookCircleDynamic dynamic) {
        this.dynamic = dynamic;
        notifyItemChanged(0);
    }

    public void addItemItem(long replyId, DynamicReply item) {
        if (replyId > 0 && item != null) {
            int index = 0;
            for (DynamicReply dr : data) {
                if (dr.getReplyId() == replyId) {
                    dr.getReplies().add(item);
                    notifyItemChanged(index + 1);
                }
                index++;
            }
        }
    }

    public void addItem(DynamicReply item) {
        if (item != null) {
            data.add(0, item);
            notifyItemChanged(1);
        }
    }

    public void addItems(List<DynamicReply> items) {
        if (items != null && !items.isEmpty()) {
            int position = data.size();
            data.addAll(items);
            notifyItemRangeChanged(position, items.size());
        }
    }

    public void deleteItem(long replyId) {
        DynamicReply tmp = null;
        int index = 0;
        for (DynamicReply dr : data) {
            if (dr.getReplyId() == replyId) {
                tmp = dr;
                break;
            }
            index++;
        }
        if (tmp != null) {
            data.remove(tmp);
            notifyItemRemoved(index + 1);// +1是因为第0个位置是动态，而评论是从1开始的
        }
    }

    public void deleteItemItem(long parentId, long replyId) {
        if (parentId > 0 && replyId > 0) {
            int index = 0;
            DynamicReply tmp = null;
            for (DynamicReply dr : data) {
                if (dr.getReplyId() == parentId) {
                    tmp = dr;
                    break;
                }
                index++;
            }
            if (tmp != null && tmp.getReplies() != null) {
                List<DynamicReply> replies = tmp.getReplies();
                tmp = null;
                for (DynamicReply dr : replies) {
                    if (dr.getReplyId() == replyId) {
                        tmp = dr;
                        break;
                    }
                }
                if (tmp != null) {
                    replies.remove(tmp);
                    notifyItemChanged(index + 1);// +1是因为第0个位置是动态，而评论是从1开始的
                }
            }
        }
    }

    public void onThumbUp(long dynamicId, boolean result) {
        if (dynamicId == dynamic.getDynamicId()) {
            dynamic.setGood(result);
            dynamic.setGoodNum(result ? dynamic.getGoodNum() + 1 : dynamic.getGoodNum() - 1);
            notifyItemChanged(0);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new DynamicDetailViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dynamic_detail_item, parent, false));
        } else {
            return new DynamicDetailCommentViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dynamic_detail_comment_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder pholder, int position) {
        if (position == 0) {
            DynamicDetailViewHolder holder = (DynamicDetailViewHolder) pholder;
            holder.showDynamic();
        } else {
            DynamicReply item = data.get(position - 1);
            DynamicDetailCommentViewHolder holder = (DynamicDetailCommentViewHolder) pholder;
            holder.dynamicReply = item;
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
    }

    @Override
    public int getItemCount() {
        if (dynamic == null) {
            return 0;
        }
        return data.size() + 1;
    }

    class DynamicDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvFace;
        private TextView tvNick, tvContent;
        private ViewStub vsImg, vsLinkedType;
        private SimpleDraweeView sdvImg;
        private TextView tvCreateTime, tvThumbNum, tvReplyNum;

        private SimpleDraweeView sdvBookCover;
        private TextView tvBookTitle;

        private SimpleDraweeView sdvSheetCover;
        private TextView tvSheet;

        private TextView tvCommentContent, tvCommunityTitle;

        public DynamicDetailViewHolder(View itemView) {
            super(itemView);
            sdvFace = WidgetUtil.obtainViewById(itemView, R.id.sdv_face);
            tvNick = WidgetUtil.obtainViewById(itemView, R.id.tv_nick_name);
            tvContent = WidgetUtil.obtainViewById(itemView, R.id.tv_content);
            tvCreateTime = WidgetUtil.obtainViewById(itemView, R.id.tv_create_time);
            tvThumbNum = WidgetUtil.obtainViewById(itemView, R.id.tv_good_num);
            tvReplyNum = WidgetUtil.obtainViewById(itemView, R.id.tv_reply_num);

            vsImg = WidgetUtil.obtainViewById(itemView, R.id.vs_image);
            vsLinkedType = WidgetUtil.obtainViewById(itemView, R.id.vs_type_holder);
            setListeners();
        }

        private void setListeners() {
            tvThumbNum.setOnClickListener(this);
            tvReplyNum.setOnClickListener(this);
        }

        private void showDynamic() {
            if (dynamic != null) {
                sdvFace.setImageURI(dynamic.getAvatar());
                tvNick.setText(dynamic.getNickname());
                if (!TextUtils.isEmpty(dynamic.getContent())) {
                    tvContent.setText(dynamic.getContent());
                }
                tvCreateTime.setText(sdf.format(new Date(dynamic.getCreatedTime())));
                tvThumbNum.setText("" + dynamic.getGoodNum());
                tvReplyNum.setText("" + dynamic.getReplyNum());
                Drawable drawable = null;
                if (dynamic.isGood()) {
                    drawable = tvThumbNum.getResources().getDrawable(R.mipmap.good_true);
                } else {
                    drawable = tvThumbNum.getResources().getDrawable(R.mipmap.good_false);
                }
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                tvThumbNum.setCompoundDrawables(drawable, null, null, null);

                if (!TextUtils.isEmpty(dynamic.getImage())) {
                    if (sdvImg == null) {
                        vsImg.setLayoutResource(R.layout.book_circle_item_img);
                        View view = vsImg.inflate();
                        sdvImg = (SimpleDraweeView) view.findViewById(R.id.sdv_img);
                    }
                    sdvImg.setImageURI(dynamic.getImage());
                }
                switch (dynamic.getLinkType()) {
                    case CommonConst.LinkType.TYPE_COMMON:
                        break;
                    case CommonConst.LinkType.TYPE_BOOK:
                        infalteBookView();
                        if (!TextUtils.isEmpty(dynamic.getBookCover())) {
                            sdvBookCover.setImageURI(dynamic.getBookCover());
                        }
                        if (!TextUtils.isEmpty(dynamic.getTitle())) {
                            tvBookTitle.setText(dynamic.getTitle());
                        }
                        break;
                    case CommonConst.LinkType.TYPE_BOOK_SHEET:
                        infalteBookSheetView();
                        if (!TextUtils.isEmpty(dynamic.getBookSheetCover())) {
                            sdvSheetCover.setImageURI(dynamic.getBookSheetCover());
                        }
                        if (!TextUtils.isEmpty(dynamic.getName())) {
                            tvSheet.setText(dynamic.getName());
                        }
                        break;
                    case CommonConst.LinkType.TYPE_COMMENT:
                        infalteCommentView();
                        String cnick = dynamic.getCommentNickname();
                        if (TextUtils.isEmpty(cnick)) {
                            cnick = "";
                        }
                        String cconten = dynamic.getCommentContent();
                        if (TextUtils.isEmpty(cconten)) {
                            cconten = "";
                        }
                        setCommentNickAndContent(cnick, cconten);
                        if (!TextUtils.isEmpty(dynamic.getCommunityTitle())) {
                            tvCommunityTitle.setText(dynamic.getCommunityTitle());
                        }
                        break;
                    case CommonConst.LinkType.TYPE_DELETED:
                        inflateDeletedView();
                        break;
                }
            }
        }

        public void setCommentNickAndContent(CharSequence nick, CharSequence content) {
            SpannableString ss = new SpannableString(nick + "：" + content);
            ss.setSpan(new ForegroundColorSpan(BaseApp.getInstance()
                            .getResources().getColor(R.color.front_hot_font)),
                    0, nick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvCommentContent.setText(ss);
        }

        public void inflateDeletedView() {
            vsLinkedType.setLayoutResource(R.layout.book_circle_item_deleted);
            View view = vsLinkedType.inflate();
        }

        public void infalteCommentView() {
            if (tvCommentContent == null) {
                vsLinkedType.setLayoutResource(R.layout.book_circle_item_comment);
                View view = vsLinkedType.inflate();
                tvCommentContent = (TextView) view.findViewById(R.id.tv_comment);
                tvCommunityTitle = (TextView) view.findViewById(R.id.tv_community_title);
            }
        }

        public void infalteBookView() {
            if (tvBookTitle == null) {
                vsLinkedType.setLayoutResource(R.layout.book_circle_item_book);
                View view = vsLinkedType.inflate();
                sdvBookCover = (SimpleDraweeView) view.findViewById(R.id.sdv_book_cover);
                tvBookTitle = (TextView) view.findViewById(R.id.tv_type_book);
            }
        }

        public void infalteBookSheetView() {
            if (tvSheet == null) {
                vsLinkedType.setLayoutResource(R.layout.book_circle_item_sheet);
                View view = vsLinkedType.inflate();
                sdvSheetCover = (SimpleDraweeView) view.findViewById(R.id.sdv_book_sheet_cover);
                tvSheet = (TextView) view.findViewById(R.id.tv_type_sheet);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_good_num:
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, ITEM_CLICK_TYPE_THUMBUP, dynamic, null, null);
                    }
                    break;
                case R.id.tv_reply_num:
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, ITEM_CLICK_TYPE_REPLY_DYNAMIC, dynamic, null, null);
                    }
                    break;
            }
        }
    }

    class DynamicDetailCommentViewHolder extends RecyclerView.ViewHolder implements DynamicCommentAdapter.OnItemClickListener, View.OnClickListener {
        private SimpleDraweeView sdvFace;
        private TextView tvNick, tvCreatedTime, tvReplyContent;
        private TextView tvReply;
        private ViewStub vsReplies;
        private RecyclerView rvReplies;
        private DynamicReply dynamicReply;

        public DynamicDetailCommentViewHolder(View itemView) {
            super(itemView);
            sdvFace = WidgetUtil.obtainViewById(itemView, R.id.sdv_face);
            tvNick = WidgetUtil.obtainViewById(itemView, R.id.tv_nick);
            tvCreatedTime = WidgetUtil.obtainViewById(itemView, R.id.tv_created_time);
            tvReplyContent = WidgetUtil.obtainViewById(itemView, R.id.tv_reply_content);
            tvReply = WidgetUtil.obtainViewById(itemView, R.id.tv_reply);
            vsReplies = WidgetUtil.obtainViewById(itemView, R.id.vs_replies);
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
                adapter.setOnItemClickListener(this);
                rvReplies.setAdapter(adapter);
            }
        }

        public void setReplies(List<DynamicReply> replies) {
            Log.d("xwlljj", "setReplies() --> replies size is " + replies.size());
            ((DynamicCommentAdapter) rvReplies.getAdapter()).clearData();
            ((DynamicCommentAdapter) rvReplies.getAdapter()).addItems(replies);
            ((DynamicCommentAdapter) rvReplies.getAdapter()).setBcd(dynamic);
            ((DynamicCommentAdapter) rvReplies.getAdapter()).setRootDyanmicReply(dynamicReply);
        }

        @Override
        public void onItemClick(View v, BookCircleDynamic bcd, DynamicReply dr) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, ITEM_CLICK_TYPE_REPLY_REPLY_REPLY, bcd, dr, dynamicReply);
            }
        }

        @Override
        public void onClick(View v) {
            if (v == itemView && onItemClickListener != null) {
                onItemClickListener.onItemClick(v, ITEM_CLICK_TYPE_REPLY_REPLY, dynamic, dynamicReply, null);
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int type, BookCircleDynamic bcd, DynamicReply dynamicReply, DynamicReply root);
    }

    public static final int ITEM_CLICK_TYPE_THUMBUP = 0;
    public static final int ITEM_CLICK_TYPE_REPLY_DYNAMIC = 1;
    public static final int ITEM_CLICK_TYPE_REPLY_REPLY = 2;
    public static final int ITEM_CLICK_TYPE_REPLY_REPLY_REPLY = 3;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm", Locale.getDefault());
}
