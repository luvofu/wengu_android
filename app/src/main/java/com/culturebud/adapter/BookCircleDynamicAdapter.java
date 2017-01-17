package com.culturebud.adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.culturebud.widget.RecyclerViewDivider;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2016/10/29.
 */

public class BookCircleDynamicAdapter extends RecyclerView.Adapter<BookCircleDynamicAdapter.DynamicViewHolder> {
    private List<BookCircleDynamic> data;
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd kk:mm", Locale.getDefault());

    public BookCircleDynamicAdapter() {
        data = new ArrayList<>();
    }

    public void clearData() {
        if (data.isEmpty()) {
            return;
        }
        data.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<BookCircleDynamic> items) {
        if (items == null || items.size() <= 0) {
            return;
        }
        int position = data.size() - 1;
        data.addAll(items);
        notifyItemRangeChanged(position, items.size());
    }

    public void onThumbUpResult(long dynamicId, boolean result) {
        int index = 0;
        for (BookCircleDynamic bcd : data) {
            if (bcd.getDynamicId() == dynamicId) {
                bcd.setGood(result);
                bcd.setGoodNum(result ? bcd.getGoodNum() + 1 : bcd.getGoodNum() - 1);
                notifyItemChanged(index);
                return;
            }
            index++;
        }
    }

    public void deleteItem(long dynamicId) {
        BookCircleDynamic item = null;
        int position = 0;
        for (BookCircleDynamic bcd : data) {
            if (bcd.getDynamicId() == dynamicId) {
                item = bcd;
                break;
            }
            position++;
        }
        if (item != null) {
            data.remove(item);
            notifyItemRemoved(position);
        }
    }

    public BookCircleDynamic getDynamicById(long dynamicId) {
        for (BookCircleDynamic bcd : data) {
            if (bcd.getDynamicId() == dynamicId) {
                return bcd;
            }
        }
        return null;
    }

    public int getItemIndex(BookCircleDynamic item) {
        return data.indexOf(item);
    }

    @Override
    public int getItemViewType(int position) {
        BookCircleDynamic bcd = data.get(position);
        int linkType = bcd.getLinkType();
        if (bcd.getLinkId() == -1) {
            linkType = 255;
        }
        int hasImg = 0;//0：没有图片， 1：有图片
        if (!TextUtils.isEmpty(bcd.getImage())) {
            hasImg = 1;
        }
        int hasComments = 0; //0：没有评论， 1：有评论
        List<DynamicReply> rs = bcd.getDynamicReplies();
        if (rs != null && rs.size() > 0) {
            hasComments = 1;
        }
        //低8位表示 linkType, 次低8位表示 hasImg, 次高8位表示 hasComments;
        int type = (linkType & 0xFF) | ((hasImg << 8) & 0xFF00) | ((hasComments << 16) & 0xFF0000);
//        Log.d("bCircle", "hasImg = " + hasImg);
//        Log.d("bCircle", "hasImgType = " + Integer.toBinaryString(((hasImg << 8) & 0xFF00)));
//        Log.d("bCircle", "viewType = " + Integer.toBinaryString(type));
        return type;
    }

    @Override
    public DynamicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DynamicViewHolder holder = new DynamicViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_circle_item, parent, false));
        int linkType = viewType & 0xFF;
        int hasImg = (viewType & 0xFF00) >> 8;
        int hasComments = (viewType & 0xFF0000) >> 16;
        Log.e("xwlljj", "linkType is " + linkType);
        switch (linkType) {
            case CommonConst.LinkType.TYPE_COMMON://普通
                break;
            case CommonConst.LinkType.TYPE_BOOK://书
                holder.infalteBookView();
                break;
            case CommonConst.LinkType.TYPE_BOOK_SHEET://书单
                holder.infalteBookSheetView();
                break;
            case CommonConst.LinkType.TYPE_COMMENT://评论
                holder.infalteCommentView();
                break;
            case CommonConst.LinkType.TYPE_DELETED:
                holder.inflateDeletedView();
                break;
        }

        switch (hasImg) {
            case 0://没图片
                break;
            case 1://有图片
                holder.inflateImageView();
                break;
        }

        switch (hasComments) {
            case 0://没评论
                break;
            case 1://有评论
                holder.inflateCommentsView();
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(DynamicViewHolder holder, int position) {
        BookCircleDynamic bcd = data.get(position);
        holder.bcd = bcd;
        holder.setFace(bcd.getAvatar());
        holder.setNick(bcd.getNickname());
        holder.setContent(bcd.getContent());

        holder.setCreateTime(bcd.getCreatedTime());
        holder.setGoodNum(bcd.getGoodNum());
        holder.setGoodOfMe(bcd.isGood());
        holder.setReplyNum(bcd.getReplyNum());

        if (bcd.getReplyNum() == 18)
        Log.d("xwlljj", "dynamic id is " + bcd.getDynamicId());

        if (!TextUtils.isEmpty(bcd.getImage())) {
            holder.setImage(bcd.getImage());
        }

        int linkType = bcd.getLinkType();
        if (bcd.getLinkId() == -1) {
            linkType = 255;
        }
        switch (linkType) {
            case CommonConst.LinkType.TYPE_COMMON:
                break;
            case CommonConst.LinkType.TYPE_BOOK:
                holder.setBookCover(bcd.getBookCover());
                holder.setBookTitle(bcd.getTitle());
                break;
            case CommonConst.LinkType.TYPE_BOOK_SHEET:
                holder.setSheetCover(bcd.getBookSheetCover());
                holder.setSheetName(bcd.getName());
                break;
            case CommonConst.LinkType.TYPE_COMMENT:
                String cnick = bcd.getCommentNickname();
                if (TextUtils.isEmpty(cnick)) {
                    cnick = "";
                }
                String cconten = bcd.getCommentContent();
                if (TextUtils.isEmpty(cconten)) {
                    cconten = "";
                }
                holder.setCommentNickAndContent(cnick, cconten);
                holder.setCommunityTitle(bcd.getCommunityTitle());
                break;
        }

        if (bcd.getDynamicReplies() != null
                && bcd.getDynamicReplies().size() > 0) {
            holder.setCommentReplies(bcd.getDynamicReplies());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DynamicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            DynamicCommentAdapter.OnItemClickListener {
        private ViewStub vsImage, vsLinkTypeItem, vsComments;

        private SimpleDraweeView sdvFace;
        private TextView tvNick, tvContent;

        private TextView tvCreateTime, tvGoodNum, tvReplyNum;

        //linkType = 3
        private SimpleDraweeView sdvSheetCover;
        private TextView tvSheet;

        //linkType = 2
        private TextView tvCommentContent, tvCommunityTitle;

        //linkType = 1
        private TextView tvBookTitle;
        private SimpleDraweeView sdvBookCover;

        //linkType = 0
        private SimpleDraweeView sdvImg;

        //hasComments;
        private RecyclerView rvComments;

        private BookCircleDynamic bcd;

        public DynamicViewHolder(View itemView) {
            super(itemView);
            vsImage = (ViewStub) itemView.findViewById(R.id.vs_image);
            vsLinkTypeItem = (ViewStub) itemView.findViewById(R.id.vs_type_holder);
            vsComments = (ViewStub) itemView.findViewById(R.id.vs_comments);

            sdvFace = (SimpleDraweeView) itemView.findViewById(R.id.sdv_face);
            tvNick = (TextView) itemView.findViewById(R.id.tv_nick_name);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);

            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvGoodNum = (TextView) itemView.findViewById(R.id.tv_good_num);
            tvReplyNum = (TextView) itemView.findViewById(R.id.tv_reply_num);
            itemView.setOnClickListener(this);
            tvGoodNum.setOnClickListener(this);
            tvReplyNum.setOnClickListener(this);
        }

        public void setFace(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            Uri uri = Uri.parse(url);
            sdvFace.setImageURI(uri);
        }

        public void setNick(CharSequence nick) {
            tvNick.setText(nick);
        }

        public void setContent(CharSequence content) {
            tvContent.setText(content);
        }

        public void setCreateTime(long createTime) {
            Date date = new Date(createTime);
            String str = sdf.format(date);
            if (TextUtils.isEmpty(str)) {
                return;
            }
            tvCreateTime.setText(str);
        }

        public void setGoodNum(long goodNum) {
            tvGoodNum.setText(String.valueOf(goodNum));
        }

        public void setGoodOfMe(boolean isMe) {
            Drawable drawable = null;
            if (isMe) {
                drawable = BaseApp.getInstance().getResources().getDrawable(R.mipmap.good_true);
            } else {
                drawable = BaseApp.getInstance().getResources().getDrawable(R.mipmap.good_false);
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tvGoodNum.setCompoundDrawables(drawable, null, null, null);
        }

        public void setReplyNum(long replyNum) {
            tvReplyNum.setText(String.valueOf(replyNum));
        }

        public void inflateDeletedView() {
            vsLinkTypeItem.setLayoutResource(R.layout.book_circle_item_deleted);
            View view = vsLinkTypeItem.inflate();
        }

        public void infalteCommentView() {
            vsLinkTypeItem.setLayoutResource(R.layout.book_circle_item_comment);
            View view = vsLinkTypeItem.inflate();
            tvCommentContent = (TextView) view.findViewById(R.id.tv_comment);
            tvCommunityTitle = (TextView) view.findViewById(R.id.tv_community_title);
            view.setOnClickListener(this);
        }

        public void setCommentNickAndContent(CharSequence nick, CharSequence content) {
            SpannableString ss = new SpannableString(nick + "：" + content);
            ss.setSpan(new ForegroundColorSpan(BaseApp.getInstance()
                            .getResources().getColor(R.color.front_hot_font)),
                    0, nick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvCommentContent.setText(ss);
        }

        public void setCommunityTitle(CharSequence title) {
            tvCommunityTitle.setText(title);
        }

        public void infalteBookView() {
            vsLinkTypeItem.setLayoutResource(R.layout.book_circle_item_book);
            View view = vsLinkTypeItem.inflate();
            sdvBookCover = (SimpleDraweeView) view.findViewById(R.id.sdv_book_cover);
            tvBookTitle = (TextView) view.findViewById(R.id.tv_type_book);
            view.setOnClickListener(this);
        }

        public void setBookCover(String url) {
            if (!TextUtils.isEmpty(url)) {
                sdvBookCover.setImageURI(url);
            }
        }

        public void setBookTitle(String title) {
            if (!TextUtils.isEmpty(title)) {
                tvBookTitle.setText(title);
            }
        }

        public void infalteBookSheetView() {
            vsLinkTypeItem.setLayoutResource(R.layout.book_circle_item_sheet);
            View view = vsLinkTypeItem.inflate();
            sdvSheetCover = (SimpleDraweeView) view.findViewById(R.id.sdv_book_sheet_cover);
            tvSheet = (TextView) view.findViewById(R.id.tv_type_sheet);
            view.setOnClickListener(this);
        }

        public void setSheetCover(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            Uri uri = Uri.parse(url);
            sdvSheetCover.setImageURI(uri);
        }

        public void setSheetName(CharSequence name) {
            tvSheet.setText(name);
        }

        public void inflateCommentsView() {
            vsComments.setLayoutResource(R.layout.book_circle_item_comments);
            View view = vsComments.inflate();
            rvComments = (RecyclerView) view.findViewById(R.id.rv_comments);
            LinearLayoutManager llm = new LinearLayoutManager(rvComments.getContext(), LinearLayoutManager.VERTICAL, false);
            rvComments.setLayoutManager(llm);
            RecyclerViewDivider divider = new RecyclerViewDivider(rvComments.getContext(), LinearLayoutManager.HORIZONTAL);
            divider.setDividerColor(rvComments.getResources().getColor(android.R.color.transparent));
            rvComments.addItemDecoration(divider);
            DynamicCommentAdapter adapter = new DynamicCommentAdapter();
            adapter.setOnItemClickListener(this);
            rvComments.setAdapter(adapter);
        }

        public void setCommentReplies(List<DynamicReply> replies) {
            ((DynamicCommentAdapter) rvComments.getAdapter()).clearData();
            ((DynamicCommentAdapter) rvComments.getAdapter()).addItems(replies);
            ((DynamicCommentAdapter) rvComments.getAdapter()).setBcd(bcd);
        }


        public void inflateImageView() {
            vsImage.setLayoutResource(R.layout.book_circle_item_img);
            View view = vsImage.inflate();
            sdvImg = (SimpleDraweeView) view.findViewById(R.id.sdv_img);
            sdvImg.setOnClickListener(this);
        }

        public void setImage(String url) {
            ViewGroup.LayoutParams params = sdvImg.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            sdvImg.setLayoutParams(params);
            Uri uri = Uri.parse(url);
            sdvImg.setImageURI(uri);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, ONCLICK_TYPE_DYNAMIC, bcd, null);
                }
                return;
            }
            switch (v.getId()) {
                case R.id.ll_book:
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, ONCLICK_TYPE_BOOK, bcd, null);
                    }
                    break;
                case R.id.ll_book_sheet:
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, ONCLICK_TYPE_BOOK_SHEET, bcd, null);
                    }
                    break;
                case R.id.ll_type_comment:
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, ONCLICK_TYPE_SHORT_COMMENT, bcd, null);
                    }
                    break;
                case R.id.sdv_img:
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, ONCLICK_TYPE_IMG, bcd, null);
                    }
                    break;
                case R.id.tv_good_num:
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, ONCLICK_TYPE_THUMB, bcd, null);
                    }
                    break;
                case R.id.tv_reply_num:
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, ONCLICK_TYPE_REPLY, bcd, null);
                    }
                    break;
            }
        }

        @Override
        public void onItemClick(View v, BookCircleDynamic bcd, DynamicReply dr) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, ONCLICK_TYPE_REPLY_REPLY, bcd, dr);
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
        void onItemClick(View v, int type, BookCircleDynamic bcd, DynamicReply dy);
    }

    public static final int ONCLICK_TYPE_DYNAMIC = 0;
    public static final int ONCLICK_TYPE_SHORT_COMMENT = 1;
    public static final int ONCLICK_TYPE_BOOK = 2;
    public static final int ONCLICK_TYPE_BOOK_SHEET = 3;
    public static final int ONCLICK_TYPE_IMG = 4;
    public static final int ONCLICK_TYPE_REPLY = 5;
    public static final int ONCLICK_TYPE_THUMB = 6;
    public static final int ONCLICK_TYPE_REPLY_REPLY = 7;
}
