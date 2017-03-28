package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.util.WidgetUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2017/3/18.
 */

public class MoreOperaItemsAdapter extends RecyclerView.Adapter<MoreOperaItemsAdapter.MoreOperaItemViewHolder> {
    private List<MoreOperaItemBean> data;

    public MoreOperaItemsAdapter() {
        data = new ArrayList<>();
    }

    public void setItems(List<MoreOperaItemBean> items) {
        if (items != null) {
            data.clear();
            notifyDataSetChanged();
            data.addAll(items);
            notifyDataSetChanged();
        }
    }

    @Override
    public MoreOperaItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = new TextView(parent.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        view.setGravity(Gravity.CENTER);
        view.setMinHeight(parent.getResources().getDimensionPixelSize(R.dimen.setting_item_middle_height));
        WidgetUtil.setRawTextSize(view, parent.getResources().getDimensionPixelSize(R.dimen.dialog_opera_font_size));
        view.setTextColor(parent.getResources().getColor(R.color.tabar_font_checked));
        return new MoreOperaItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoreOperaItemViewHolder holder, int position) {
        holder.setItemInfo(data.get(position).getItemInfo());
        holder.position = position;
        holder.item = data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MoreOperaItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvItem;
        private int position;
        private MoreOperaItemBean item;

        public MoreOperaItemViewHolder(View itemView) {
            super(itemView);
            tvItem = (TextView) itemView;
            tvItem.setOnClickListener(this);
        }

        public void setItemInfo(String itemInfo) {
            if (!TextUtils.isEmpty(itemInfo)) {
                tvItem.setText(itemInfo);
            } else {
                tvItem.setText("");
            }
        }

        @Override
        public void onClick(View v) {
            if (v == tvItem) {
                if (onMoreOperaItemClickListener != null) {
                    onMoreOperaItemClickListener.onMoreOperaItemClick(v, item, position);
                }
            }
        }
    }

    private OnMoreOperaItemClickListener onMoreOperaItemClickListener;

    public OnMoreOperaItemClickListener getOnMoreOperaItemClickListener() {
        return onMoreOperaItemClickListener;
    }

    public void setOnMoreOperaItemClickListener(OnMoreOperaItemClickListener onMoreOperaItemClickListener) {
        this.onMoreOperaItemClickListener = onMoreOperaItemClickListener;
    }

    public interface OnMoreOperaItemClickListener {
        void onMoreOperaItemClick(View v, MoreOperaItemBean item, int position);
    }

    public static abstract class MoreOperaItemBean {
        private int type;
        private String itemInfo;
        private int readStatus;

        public MoreOperaItemBean() {
        }

        public MoreOperaItemBean(int type, String itemInfo) {
            this.type = type;
            this.itemInfo = itemInfo;
        }

        public MoreOperaItemBean(int type, String itemInfo, int readStatus) {
            this.type = type;
            this.itemInfo = itemInfo;
            this.readStatus = readStatus;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getItemInfo() {
            return itemInfo;
        }

        public void setItemInfo(String itemInfo) {
            this.itemInfo = itemInfo;
        }

        public int getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(int readStatus) {
            this.readStatus = readStatus;
        }

        @Override
        public String toString() {
            return "MoreOperaItemBean{" +
                    "type=" + type +
                    ", itemInfo='" + itemInfo + '\'' +
                    ", readStatus=" + readStatus +
                    '}';
        }
    }
}
