package com.culturebud.widget;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class TagAdapter<T> {
    private List<T> tags;
    private OnDataChangedListener mOnDataChangedListener;
    private HashSet<Integer> mCheckedPosList = new HashSet<>();

    public TagAdapter() {
        tags = new ArrayList<>();
    }

    public TagAdapter(List<T> tags) {
        this.tags = tags;
    }

    public TagAdapter(T... tags) {
        this.tags = new ArrayList<>(Arrays.asList(tags));
    }

    public void addTag(T t) {
        if (t != null) {
            tags.add(t);
            notifyDataChanged();
        }
    }

    public void addTag(T t, int position) {
        if (t != null) {
            tags.add(position, t);
            notifyDataChanged();
        }
    }

    public void addTags(List<T> ts) {
        if (ts != null && !ts.isEmpty()) {
            tags.addAll(ts);
            notifyDataChanged();
        }
    }

    public void clearData() {
        if (!tags.isEmpty()) {
            tags.clear();
            notifyDataChanged();
        }
    }

    public List<T> getTags() {
        List<T> ts = new ArrayList<>();
        ts.addAll(tags);
        return ts;
    }

    public void delTag(T t) {
        if (t != null) {
            if (tags.remove(t)) {
                notifyDataChanged();
            }
        }
    }

    public T getLastTag() {
        if (!tags.isEmpty()) {
            return tags.get(tags.size() - 1);
        }
        return null;
    }

    interface OnDataChangedListener {
        void onChanged();
    }

    void setOnDataChangedListener(OnDataChangedListener listener) {
        mOnDataChangedListener = listener;
    }

    public void setSelectedList(int... poses) {
        Set<Integer> set = new HashSet<>();
        for (int pos : poses) {
            set.add(pos);
        }
        setSelectedList(set);
    }

    public void setSelectedList(Set<Integer> set) {
        mCheckedPosList.clear();
        if (set != null)
            mCheckedPosList.addAll(set);
        notifyDataChanged();
    }

    HashSet<Integer> getPreCheckedList() {
        return mCheckedPosList;
    }


    public int getCount() {
        return tags == null ? 0 : tags.size();
    }

    public void notifyDataChanged() {
        mOnDataChangedListener.onChanged();
    }

    public T getItem(int position) {
        return tags.get(position);
    }

    public abstract View getView(FlowLayout parent, int position, T t);

    public boolean setSelected(int position, T t) {
        return false;
    }


}