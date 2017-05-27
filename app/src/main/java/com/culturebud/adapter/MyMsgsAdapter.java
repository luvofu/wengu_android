package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.bean.UserMessage;
import com.culturebud.util.WidgetUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2016/11/16.
 */

public class MyMsgsAdapter extends RecyclerView.Adapter<MyMsgsAdapter.MyMsgsViewHolder> {
    private List<UserMessage> data = new ArrayList<>();

    public void addItems(List<UserMessage> items) {
        if (items != null && !items.isEmpty()) {
//            int position = data.size() - 1;
            data.addAll(items);
//            notifyItemRangeChanged(position, items.size());
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public MyMsgsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyMsgsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_msgs_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyMsgsViewHolder holder, int position) {
        UserMessage msg = data.get(position);
        holder.setUmItem(msg);
        holder.setFace(msg.getAvatar());
        holder.setNick(msg.getNickname());
        holder.setDesc(msg.getMessageType(), msg.getContent());
        holder.setTime(msg);
        holder.setLink(msg);
        holder.setOpera(msg);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void deleteItem(UserMessage userMessage) {
        if (userMessage == null) {
            return;
        }
        int idx = data.indexOf(userMessage);
        if (idx >= 0) {
            data.remove(userMessage);
            notifyItemRemoved(idx);
        }
    }

    class MyMsgsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private UserMessage umItem;
        private LinearLayout llItem;
        private SimpleDraweeView sdvFace;
        private TextView tvNick, tvDesc, tvTime;
        private SimpleDraweeView sdvLink;
        private TextView tvLink;
        private Button btnOpera;
        private Button btnDel;

        public MyMsgsViewHolder(View itemView) {
            super(itemView);
            sdvFace = (SimpleDraweeView) itemView.findViewById(R.id.sdv_face);
            tvNick = (TextView) itemView.findViewById(R.id.tv_nick);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_content);
            tvTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            sdvLink = (SimpleDraweeView) itemView.findViewById(R.id.sdv_link);
            tvLink = (TextView) itemView.findViewById(R.id.tv_link);
            btnOpera = (Button) itemView.findViewById(R.id.btn_opt);
            btnOpera.setOnClickListener(this);

            sdvFace.setOnClickListener(this);

            btnDel = WidgetUtil.obtainViewById(itemView, R.id.btn_delete);
            btnDel.setOnClickListener(this);
            llItem = WidgetUtil.obtainViewById(itemView, R.id.rl_item);
            llItem.setOnClickListener(this);
        }

        public void setUmItem(UserMessage umItem) {
            this.umItem = umItem;
        }

        public void setFace(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            sdvFace.setImageURI(url);
        }

        public void setNick(String nick) {
            if (TextUtils.isEmpty(nick)) {
                return;
            }
            tvNick.setText(nick);
        }

        public void setDesc(int msgType, String desc) {
            if (TextUtils.isEmpty(desc)) {
                return;
            }
            tvDesc.setText(desc);
        }

        public void setTime(UserMessage msg) {
            tvTime.setText(sdf.format(new Date(msg.getCreatedTime())));
        }

        public void setLink(UserMessage msg) {
            sdvLink.setVisibility(View.GONE);
            tvLink.setVisibility(View.GONE);
            if (msg.getMsgLinkImage() != null && !msg.getMsgLinkImage().isEmpty()) {
                sdvLink.setImageURI(msg.getMsgLinkImage());
                sdvLink.setVisibility(View.VISIBLE);
            } else if (msg.getMsgLinkContent() != null && !msg.getMsgLinkContent().isEmpty()) {
                tvLink.setText(msg.getMsgLinkContent());
                tvLink.setVisibility(View.VISIBLE);
            }
        }

        public void setOpera(UserMessage msg) {
            boolean hasOpt = false;
            switch (msg.getMessageType()) {
                case 0:
                    hasOpt = true;
                    break;
            }
            if (!hasOpt) {
                btnOpera.setVisibility(View.GONE);
            } else {
                switch (msg.getDealStatus()) {
                    case CommonConst.MessageDealStatus.STATUS_NOT_DEAL:
                        btnOpera.setText("同意");
                        btnOpera.setEnabled(true);
                        break;
                    case CommonConst.MessageDealStatus.STATUS_ACCEPT:
                        btnOpera.setText("已接受");
                        btnOpera.setEnabled(false);
                        break;
                    case CommonConst.MessageDealStatus.STATUS_REFUSE:
                        btnOpera.setText("已拒绝");
                        btnOpera.setEnabled(false);
                        break;
                }
            }
        }

        @Override
        public void onClick(View view) {
            if (view == btnOpera && onAgreeListener != null) {
                onAgreeListener.onAgree(view, umItem);
            }
            if (onItemClickListener != null) {
                if (view == llItem) {//jump to detail
                    onItemClickListener.onItemClick(data.indexOf(umItem), view, umItem, 2);
                } else if (view == btnDel) {//del user msg
                    onItemClickListener.onItemClick(data.indexOf(umItem), view, umItem, 1);
                } else if (view == sdvFace) {//jump to user home
                    onItemClickListener.onItemClick(data.indexOf(umItem), view, umItem, 0);
                }
            }
        }
    }

    private OnAgreeListener onAgreeListener;

    public OnAgreeListener getOnAgreeListener() {
        return onAgreeListener;
    }

    public void setOnAgreeListener(OnAgreeListener onAgreeListener) {
        this.onAgreeListener = onAgreeListener;
    }

    public interface OnAgreeListener {
        void onAgree(View v, UserMessage userMessage);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v, UserMessage userMessage, int operaType);
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm", Locale.getDefault());
}
