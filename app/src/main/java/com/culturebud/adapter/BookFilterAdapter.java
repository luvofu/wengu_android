package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郝泽亮 on 2017/3/27.
 */

public class BookFilterAdapter extends RecyclerView.Adapter<BookFilterAdapter.BookFilterHolder> {
    private List<String> data;

    public BookFilterAdapter() {
        super();
        data = new ArrayList<>();
    }

    public void addItems(List<String> filters) {
        if (filters == null || filters.size() <= 0) {
            return;
        }
        data.clear();
        int position = data.size() - 1;
        data.addAll(filters);
        notifyItemRangeChanged(position, filters.size());
    }
    //private
    @Override
    public BookFilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         return new BookFilterAdapter.BookFilterHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_item, null, false));
    }

    @Override
    public void onBindViewHolder(BookFilterHolder holder, int position) {
        String filter = data.get(position);
        holder.setFilter(filter);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    class BookFilterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView filterView;
        String filter;
        public BookFilterHolder(View itemView) {
            super(itemView);
            filterView = (TextView)itemView.findViewById(R.id.filter_name);
            filterView.setOnClickListener(this);
        }

        public void setFilter(String filter) {
            this.filter = filter;
            filterView.setText(filter);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view,filter);
        }
    }
    private BookFilterAdapter.OnItemClickListener itemClickListener;

    public void setOnItemClickListener(BookFilterAdapter.OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, String filter);
    }
}
