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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by XieWei on 2016/12/5.
 */

public class MyFriendsAdapter extends RecyclerView.Adapter {
    private static final int ITEM_TYPE_SEARCH = 0;
    private static final int ITEM_TYPE_ALPH = 1;
    private static final int ITEM_TYPE_FRIEND = 2;
    private List<Object> data;
    private List<String> indexs;

    public MyFriendsAdapter() {
        data = new ArrayList<>();
        indexs = new ArrayList<>();
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
        }
        if (!indexs.isEmpty()) {
            indexs.clear();
        }
    }

    public int indexAlph(String key) {
        return indexs.indexOf(key);
    }

    public List<String> getIndexs() {
        return indexs;
    }

    public void addItems(List<User> items) {
        if (items != null && !items.isEmpty()) {
            Map<String, List<User>> map = new HashMap<>();
            for (User user : items) {
                if (map.containsKey(user.getSpellFirst())) {
                    map.get(user.getSpellFirst()).add(user);
                } else {
                    List<User> tmp = new LinkedList<>();
                    tmp.add(user);
                    map.put(user.getSpellFirst(), tmp);
                    indexs.add(user.getSpellFirst());
                }
            }
            data.add(0);
            Collections.sort(indexs, (o1, o2) -> {
                char c1 = o1.charAt(0);
                char c2 = o2.charAt(0);
                if (c1 == c2) {
                    return 0;
                } else if (c1 < c2) {
                    return -1;
                } else {
                    return 1;
                }
            });
            for (String key : indexs) {
                data.add(key);
                data.addAll(map.get(key));
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object item = data.get(position);
        if (item instanceof Integer) {
            return ITEM_TYPE_SEARCH;
        } else if (item instanceof String) {
            return ITEM_TYPE_ALPH;
        } else {
            return ITEM_TYPE_FRIEND;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case ITEM_TYPE_SEARCH:
                holder = new SearchViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_friend_search_item, parent, false));
                break;
            case ITEM_TYPE_ALPH:
                TextView tv = new TextView(parent.getContext());
                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setTextColor(parent.getResources().getColor(R.color.title_font_default));
                tv.setBackgroundColor(parent.getResources().getColor(R.color.light_gray));
                int padding = parent.getResources().getDimensionPixelSize(R.dimen.my_friend_alph_item_padding);
                tv.setPadding(padding, padding, padding, padding);
                holder = new IndexViewHolder(tv);
                break;
            case ITEM_TYPE_FRIEND:
                holder = new FriendViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_friends_item, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object item = data.get(position);
        if (item instanceof String) {
            IndexViewHolder ivHolder = (IndexViewHolder) holder;
            ivHolder.position = position;
            ivHolder.setAlph(item.toString());
        } else if (item instanceof User) {
            FriendViewHolder fvHolder = (FriendViewHolder) holder;
            fvHolder.position = position;
            User friend = (User) item;
            fvHolder.setFriendFace(friend.getAvatar());
            fvHolder.setNick(friend.getNickname());
        } else if (item instanceof Integer) {
            SearchViewHolder svHolder = (SearchViewHolder) holder;
            svHolder.position = position;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position;

        public SearchViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, v, data.get(position));
                }
            }
        }
    }

    class IndexViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvAlph;
        private int position;

        public IndexViewHolder(View itemView) {
            super(itemView);
            tvAlph = (TextView) itemView;
            itemView.setOnClickListener(this);
        }

        public void setAlph(String spell) {
            if (TextUtils.isEmpty(spell)) {
                return;
            }
            tvAlph.setText(spell);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, v, data.get(position));
                }
            }
        }
    }

    class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvFriendFace;
        private TextView tvNick;
        private int position;

        public FriendViewHolder(View itemView) {
            super(itemView);
            sdvFriendFace = (SimpleDraweeView) itemView.findViewById(R.id.sdv_friend_face);
            tvNick = (TextView) itemView.findViewById(R.id.tv_friend_nick);
            itemView.setOnClickListener(this);
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

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, v, data.get(position));
                }
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
        void onItemClick(int position, View v, Object item);
    }
}
