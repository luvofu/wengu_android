package com.culturebud.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.Book;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.Comment;
import com.culturebud.ui.community.BookCommunityActivity;
import com.culturebud.ui.community.CommentDetailActivity;
import com.culturebud.ui.front.BookDetailActivity;
import com.culturebud.ui.front.BookSheetDetailActivity;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by XieWei on 2016/10/31.
 */

public class FrontPageAdapter extends RecyclerView.Adapter<FrontPageAdapter.FrontPageViewHolder> implements
        BooksAdapter.OnItemClickListener, CommentAdapter.OnItemClickListener, BookSheetAdapter.OnItemClickListener,
        CommentAdapter.OnBookCommunityClickListener, CommentAdapter.OnShareListener {
    private List<Map<String, Object>> data;
    private Map<String, Object> hotBook;
    private Map<String, Object> hotSheet;
    private Map<String, Object> hotComment;

    public FrontPageAdapter() {
        data = new ArrayList();
        hotBook = new HashMap<>();
        hotSheet = new HashMap<>();
        hotComment = new HashMap<>();
    }

    public void addHotBooks(List<Book> books) {
        if (books == null || books.size() <= 0) {
            return;
        }
        if (!data.contains(hotBook)) {
            hotBook.put("type", 0);
            hotBook.put("label", "热书");
            hotBook.put("data", books);
            HotBookAdapter adapter = new HotBookAdapter();
            adapter.setOnItemClickListener(this);
            adapter.clearData();
            adapter.addItems(books);
            hotBook.put("adapter", adapter);
            data.add(0, hotBook);
            notifyItemInserted(0);
        } else {
            HotBookAdapter adapter = (HotBookAdapter) hotBook.get("adapter");
            adapter.clearData();
            adapter.addItems(books);
        }
    }

    public void addHotSheets(List<BookSheet> sheets) {
        if (sheets == null || sheets.size() <= 0) {
            return;
        }
        if (!data.contains(hotSheet)) {
            hotSheet.put("type", 1);
            hotSheet.put("label", "热单");
            hotSheet.put("data", sheets);
            BookSheetAdapter adapter = new BookSheetAdapter();
            adapter.setHorizontal(true);
            adapter.setOnItemClickListener(this);
            adapter.clearData();
            adapter.addItems(sheets);
            hotSheet.put("adapter", adapter);
            data.remove(hotSheet);
            if (data.size() < 1) {
                data.add(hotSheet);
                notifyDataSetChanged();
            } else {
                data.add(1, hotSheet);
                notifyItemInserted(1);
            }
        } else {
            BookSheetAdapter adapter = (BookSheetAdapter) hotSheet.get("adapter");
            adapter.clearData();
            adapter.addItems(sheets);
        }
    }

    public void addComments(List<Comment> comments) {
        if (comments == null || comments.size() <= 0) {
            return;
        }
        if (!data.contains(hotComment)) {
            hotComment.put("type", 2);
            hotComment.put("label", "热评");
            hotComment.put("data", comments);
            CommentAdapter adapter = new CommentAdapter(false);
            adapter.setOnItemClickListener(this);
            adapter.setOnBookCommunityClickListener(this);
            adapter.setOnShareListener(this);
            adapter.clearData();
            adapter.addItems(comments);
            hotComment.put("adapter", adapter);
            data.remove(hotComment);
            if (data.size() < 2) {
                data.add(hotComment);
                notifyItemInserted(data.size() - 1);
            } else {
                data.add(2, hotComment);
                notifyItemChanged(2);
            }
        } else {
            CommentAdapter adapter = (CommentAdapter) hotComment.get("adapter");
            adapter.clearData();
            adapter.addItems(comments);
        }
    }

    public void setThumbUpListener(CommentAdapter.OnThumbUpClickListener listener) {
        CommentAdapter adapter = (CommentAdapter) hotComment.get("adapter");
        if (adapter != null) {
            adapter.setOnThumbUpClickListener(listener);
        }
    }

    public void onThumbUp(long commentId, boolean isGood) {
        CommentAdapter adapter = (CommentAdapter) hotComment.get("adapter");
        if (adapter != null) {
            adapter.onThumbUp(commentId, isGood);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Map<String, Object> item = data.get(position);
        int type = Integer.valueOf(item.get("type").toString());
//        if (type == 0 || type == 1) {
        if (type == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public FrontPageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrontPageViewHolder holder = new FrontPageViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.front_page_item, parent, false));
        switch (viewType) {
            case 0:
                holder.inflateHlistView();
                break;
            case 1:
                holder.inflateVlistView();
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(FrontPageViewHolder holder, int position) {
        Map<String, Object> item = data.get(position);
        holder.setLabel(item.get("label").toString());
        holder.setAdapter((RecyclerView.Adapter) item.get("adapter"));
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnMoreListener(OnMoreListener listener) {
        onMoreListener = listener;
    }

    private OnMoreListener onMoreListener;

    @Override
    public void onItemClick(View v, Book book, int operaType) {
        if (operaType == 0) {
            Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
            intent.putExtra("bookId", book.getBookId());
            v.getContext().startActivity(intent);
        }
    }

    @Override
    public void onItemClick(View v, int position, Comment comment) {
        Intent intent = new Intent(v.getContext(), CommentDetailActivity.class);
        intent.putExtra("comment", new Gson().toJson(comment));
        v.getContext().startActivity(intent);
    }

    @Override
    public void onItemClick(int position, View v, BookSheet sheet) {
        Intent intent = new Intent(v.getContext(), BookSheetDetailActivity.class);
        intent.putExtra("sheetId", sheet.getSheetId());
        v.getContext().startActivity(intent);
    }

    @Override
    public void onBookCommunityClick(View v, int position, Comment comment) {
        Intent intent = new Intent(v.getContext(), BookCommunityActivity.class);
        intent.putExtra("communityId", comment.getCommunityId());
        v.getContext().startActivity(intent);
    }

    @Override
    public void onShare(View v, int position, Comment comment) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(comment.getTitle());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(comment.getTitle());
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(v.getContext());
    }

    public interface OnMoreListener {
        void onMore(int type, RecyclerView.Adapter adapter);
    }

    class FrontPageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvLable, tvMore;
        private RecyclerView rvItems;
        private ViewStub vsData;
        private int position;

        public FrontPageViewHolder(View itemView) {
            super(itemView);
            tvLable = (TextView) itemView.findViewById(R.id.tv_label);
            tvMore = (TextView) itemView.findViewById(R.id.tv_more);
            vsData = (ViewStub) itemView.findViewById(R.id.vs_data);
            tvMore.setOnClickListener(this);
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void inflateVlistView() {
            vsData.setLayoutResource(R.layout.vertical_list);
            View view = vsData.inflate();
            rvItems = (RecyclerView) view.findViewById(R.id.rv_items);
            LinearLayoutManager llm = new LinearLayoutManager(rvItems.getContext(), LinearLayoutManager.VERTICAL,
                    false);
            rvItems.setLayoutManager(llm);
            RecyclerViewDivider divider = new RecyclerViewDivider(rvItems.getContext(), LinearLayoutManager.HORIZONTAL);
            rvItems.addItemDecoration(divider);
        }

        public void inflateHlistView() {
            vsData.setLayoutResource(R.layout.horizontal_list);
            View view = vsData.inflate();
            rvItems = (RecyclerView) view.findViewById(R.id.rv_items);
            LinearLayoutManager llm = new LinearLayoutManager(rvItems.getContext(), LinearLayoutManager.HORIZONTAL,
                    false);
            rvItems.setLayoutManager(llm);
            RecyclerViewDivider divider = new RecyclerViewDivider(rvItems.getContext(), LinearLayoutManager.VERTICAL);
            divider.setDividerHeight(20);
            divider.setDividerColor(Color.WHITE);
            rvItems.addItemDecoration(divider);
        }

        public void setLabel(String label) {
            tvLable.setText(label);
        }

        public void setAdapter(RecyclerView.Adapter adapter) {
            rvItems.setAdapter(adapter);
        }

        @Override
        public void onClick(View v) {
            if (onMoreListener != null) {
                Map<String, Object> item = data.get(position);
                int type = Integer.valueOf(item.get("type").toString());
                RecyclerView.Adapter adapter = (RecyclerView.Adapter) item.get("adapter");
                onMoreListener.onMore(type, adapter);
            }
        }
    }
}
