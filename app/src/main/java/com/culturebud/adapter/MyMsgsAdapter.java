package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.culturebud.CommonConst.MessageDealStatus;
import com.culturebud.CommonConst.UserMsgType;
import com.culturebud.R;
import com.culturebud.bean.UserMessage;
import com.culturebud.util.WidgetUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2016/11/16.
 */

public class MyMsgsAdapter extends RecyclerView.Adapter<MyMsgsAdapter.MyMsgsViewHolder> {
    private List<UserMessage> data;

    public MyMsgsAdapter() {
        data = new ArrayList<>();
    }

    public void addItems(List<UserMessage> items) {
        if (items != null && !items.isEmpty()) {
            int position = data.size() - 1;
            data.addAll(items);
            notifyItemRangeChanged(position, items.size());
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
        return new MyMsgsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_msgs_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyMsgsViewHolder holder, int position) {
        UserMessage item = data.get(position);
//        holder.position = position;
        holder.umItem = item;
        holder.setFace(item.getAvatar());
        holder.setNick(item.getNickname());
        holder.setDesc(item.getMessageType(), item.getContent());
        holder.setOpera(item.getDealStatus());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateItemStatus(long messageId, int statusAccept) {
        int idx = 0;
        for (UserMessage um : data) {
            if (um.getMessageId() == messageId) {
                um.setDealStatus(statusAccept);
                notifyItemChanged(idx);
                return;
            }
            idx++;
        }
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
        private SimpleDraweeView sdvFace;
        private TextView tvNick, tvDesc;
        private Button btnOpera;
        //        private int position;
        public UserMessage umItem;
        private Button btnDel;
        private RelativeLayout rlItem;


        public MyMsgsViewHolder(View itemView) {
            super(itemView);
            sdvFace = (SimpleDraweeView) itemView.findViewById(R.id.sdv_face);
            tvNick = (TextView) itemView.findViewById(R.id.tv_nick);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_content);
            btnOpera = (Button) itemView.findViewById(R.id.btn_agree);
            btnOpera.setOnClickListener(this);

            btnDel = WidgetUtil.obtainViewById(itemView, R.id.btn_delete);
            btnDel.setOnClickListener(this);
            rlItem = WidgetUtil.obtainViewById(itemView, R.id.rl_item);
            rlItem.setOnClickListener(this);
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
            switch (msgType) {
                case UserMsgType.TYPE_FRIEND_INVITE:
                    desc = "请求添加你为好友：" + desc;
                    break;
                case UserMsgType.TYPE_DESK_INVITE:
                    desc = "请求添加你到书桌：" + desc;
                    break;
                case UserMsgType.TYPE_COMMUNITY_REPLY:
                    break;
                case UserMsgType.TYPE_CIRCLE_REPLY:
                    break;
            }
            tvDesc.setText(desc);
        }

        public void setOpera(int operaStatus) {
            switch (operaStatus) {
                case MessageDealStatus.STATUS_NOT_DEAL:
                    btnOpera.setText("同意");
                    btnOpera.setEnabled(true);
                    break;
                case MessageDealStatus.STATUS_ACCEPT:
                    btnOpera.setText("已接受");
                    btnOpera.setEnabled(false);
                    break;
                case MessageDealStatus.STATUS_REFUSE:
                    btnOpera.setText("已拒绝");
                    btnOpera.setEnabled(false);
                    break;
            }
        }

        @Override
        public void onClick(View view) {
            if (view == btnOpera && onAgreeListener != null) {
                onAgreeListener.onAgree(view, umItem);
            }
            if (onItemClickListener != null) {
                if (view == rlItem) {//jump to user profile
                    onItemClickListener.onItemClick(data.indexOf(umItem), view, umItem, 0);
                } else if (view == btnDel) {//del user msg
                    onItemClickListener.onItemClick(data.indexOf(umItem), view, umItem, 1);
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
}
