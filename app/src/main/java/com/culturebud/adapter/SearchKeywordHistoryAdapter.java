package com.culturebud.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.SearchKeyword;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by XieWei on 2016/11/6.
 */

public class SearchKeywordHistoryAdapter extends BaseAdapter {
    private List<SearchKeyword> data;

    public SearchKeywordHistoryAdapter() {
        data = new ArrayList<>();
    }

    public void clearData() {
        if (data.size() > 0) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    public void addItems(List<SearchKeyword> keywords) {
        if (keywords != null && keywords.size() > 0) {
            Collections.reverse(keywords);
            data.addAll(keywords);
            notifyDataSetChanged();
        }
    }

    public void addItem(SearchKeyword keyword) {
        if (!data.contains(keyword)) {
            data.add(0, keyword);
        }
    }

    public List<SearchKeyword> getData() {
        List<SearchKeyword> tmp = new ArrayList<>();
        tmp.addAll(data);
        return tmp;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public SearchKeyword getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SKHViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            holder = new SKHViewHolder();
            holder.tvName = (TextView) convertView.findViewById(android.R.id.text1);
            holder.tvName.setTextColor(parent.getResources().getColor(R.color.title_font_default));
            holder.tvName.setTextSize(16F);
            convertView.setTag(holder);
        } else {
            holder = (SKHViewHolder) convertView.getTag();
        }
        holder.setName(data.get(position).getKeyword());
        return convertView;
    }

    class SKHViewHolder {
        private TextView tvName;

        public void setName(String name) {
            tvName.setText(name);
        }
    }
}
