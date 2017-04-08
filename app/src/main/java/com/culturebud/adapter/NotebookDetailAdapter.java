package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.CommonConst.ContentPermission;
import com.culturebud.R;
import com.culturebud.bean.Note;
import com.culturebud.bean.NotebookDetail;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2016/11/23.
 */

public class NotebookDetailAdapter extends Adapter<ViewHolder> {
    private static final int TYPE_NOTEBOOK_DETAIL = 0;
    private static final int TYPE_NOTE = 1;

    public static final int OPERA_TYPE_PERMISSION = 0;
    public static final int OPERA_TYPE_NOTEBOOK_NAME = 1;
    public static final int OPERA_TYPE_NOTE_EDIT_DELETE = 2;
    public static final int OPERA_TYPE_NOTE_PREVIEW_PIC = 3;

    private NotebookDetail nbDetail;
    private List<Note> data;

    public NotebookDetailAdapter() {
        data = new ArrayList<>();
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    public void addItems(List<Note> items) {
        if (items != null && !items.isEmpty()) {
            int position = data.size() - 1;
            data.addAll(items);
            notifyItemRangeChanged(position, items.size());
        }
    }

    public void deleteItem(long noteId, int position) {
        if (position > 0 && position < getItemCount()) {
            Note note = data.get(position - 1);
            if (note.getNoteId() == noteId) {
                data.remove(position - 1);
                notifyItemRemoved(position);
                return;
            }
        }
        for (int i = 0; i < data.size(); i++) {
            Note note = data.get(i);
            if (note.getNoteId() == noteId) {
                data.remove(note);
                notifyItemRemoved(i + 1);
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_NOTEBOOK_DETAIL;
        } else {
            return TYPE_NOTE;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NOTEBOOK_DETAIL) {
            return new NotebookDetailViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notebook_detail_item, parent, false));
        } else {
            return new NoteViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.note_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            NotebookDetailViewHolder nbdHolder = (NotebookDetailViewHolder) holder;
            nbdHolder.position = position;
            nbdHolder.setNotebookCover(nbDetail.getCover());
            nbdHolder.setNotebookName(nbDetail.getName(), nbDetail.getTitle());
            nbdHolder.setNoteNum(nbDetail.getNoteNum());
            nbdHolder.setNotePermission(nbDetail.getPermission());
            nbdHolder.setNotebookCreateTime(nbDetail.getCreatedTime());
        } else {
            NoteViewHolder noteHolder = (NoteViewHolder) holder;
            Note item = data.get(position - 1);
            noteHolder.note = item;
            noteHolder.setNoteContent(item.getContent());
            noteHolder.setImage(item.getImage());
            noteHolder.setNoteDesc(item.getChapter(), item.getPages(), item.getOtherLocation());
            noteHolder.setUpdateTime(item.getUpdatedTime());
        }
    }

    @Override
    public int getItemCount() {
        if (nbDetail == null) {
            return 0;
        }
        return data.size() + 1;
    }

    public void setNotebookDetail(NotebookDetail notebookDetail) {
        if (notebookDetail != null) {
            nbDetail = notebookDetail;
            notifyItemChanged(0);
        }
    }

    class NotebookDetailViewHolder extends ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvNotebookCover;
        private TextView tvNotebookName;
        private TextView tvPermission;
        private TextView tvNoteNum;
        private TextView tvCreateTime;
        private int position;

        public NotebookDetailViewHolder(View itemView) {
            super(itemView);
            sdvNotebookCover = (SimpleDraweeView) itemView.findViewById(R.id.sdv_notebook_cover);
            tvNotebookName = (TextView) itemView.findViewById(R.id.tv_notebook_name);
            tvPermission = (TextView) itemView.findViewById(R.id.tv_edit_permission);
            tvNoteNum = (TextView) itemView.findViewById(R.id.tv_note_num);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);

            tvPermission.setOnClickListener(this);
            tvNotebookName.setOnClickListener(this);
        }

        public void setNotebookCover(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            sdvNotebookCover.setImageURI(url);
        }

        public void setNotebookName(String notebookName, String bookTitle) {
            if (TextUtils.isEmpty(notebookName) || TextUtils.isEmpty(bookTitle)) {
                return;
            }
            tvNotebookName.setText(bookTitle + " . " + notebookName);
        }

        public void setNoteNum(long noteNum) {
            tvNoteNum.setText(String.valueOf(noteNum));
        }

        public void setNotebookCreateTime(long createTime) {
            tvCreateTime.setText(sdf.format(new Date(createTime)));
        }

        public void setNotePermission(int permission) {
            String formatStr = BaseApp.getInstance().getString(R.string.note_permission);
            String pstr = ContentPermission.PER_DES_PUBLIC;
            switch (permission) {
                case ContentPermission.PERMISSION_PUBLIC:
                    pstr = ContentPermission.PER_DES_PUBLIC;
                    break;
                case ContentPermission.PERMISSION_FRIEND:
                    pstr = ContentPermission.PER_DES_FRIEND;
                    break;
                case ContentPermission.PERMISSION_PERSONAL:
                    pstr = ContentPermission.PER_DES_PERSONAL;
                    break;
            }
            tvPermission.setText(String.format(Locale.getDefault(), formatStr, pstr));
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_notebook_name:
                    if (onNotebookOperaListener != null) {
                        onNotebookOperaListener.onNotebookOpera(view, nbDetail, OPERA_TYPE_NOTEBOOK_NAME, position);
                    }
                    break;
                case R.id.tv_edit_permission:
                    if (onNotebookOperaListener != null) {
                        onNotebookOperaListener.onNotebookOpera(view, nbDetail, OPERA_TYPE_PERMISSION, position);
                    }
                    break;
            }
        }
    }

    class NoteViewHolder extends ViewHolder implements View.OnClickListener {
        private ImageView ivOperas;
        private SimpleDraweeView sdvImg;
        private TextView tvNoteContent, tvNoteDesc, tvUpdateTime;
        private Note note;

        public NoteViewHolder(View itemView) {
            super(itemView);
            ivOperas = (ImageView) itemView.findViewById(R.id.iv_operas);
            sdvImg = (SimpleDraweeView) itemView.findViewById(R.id.sdv_img);
            tvNoteContent = (TextView) itemView.findViewById(R.id.tv_note_content);
            tvNoteDesc = (TextView) itemView.findViewById(R.id.tv_note_desc);
            tvUpdateTime = (TextView) itemView.findViewById(R.id.tv_update_time);
            ivOperas.setOnClickListener(this);
            sdvImg.setOnClickListener(this);
        }

        public void setImage(String url) {
            if (TextUtils.isEmpty(url)) {
                sdvImg.setVisibility(View.GONE);
                return;
            }
            sdvImg.setVisibility(View.VISIBLE);
            sdvImg.setImageURI(url);
        }

        public void setNoteContent(String noteContent) {
            if (TextUtils.isEmpty(noteContent)) {
                return;
            }
            tvNoteContent.setText(noteContent);
        }

        public void setNoteDesc(String chapter, int pages, String other) {
            if (TextUtils.isEmpty(chapter) && TextUtils.isEmpty(other) && pages < 0) {
                return;
            }
            String tmp = TextUtils.isEmpty(chapter) ? "" : chapter + " | ";
            tmp = tmp + (pages < 0 ? "" : pages + " | ");
            tmp = tmp + (TextUtils.isEmpty(other) ? "" : other);
            if (tmp.trim().endsWith("|")) {
                tmp = tmp.substring(0, tmp.length() - 2);
            }
            tvNoteDesc.setText(tmp);
        }

        public void setUpdateTime(long updateTime) {
            tvUpdateTime.setText(String.format(Locale.getDefault(),
                    BaseApp.getInstance().getString(R.string.note_update_time),
                    sdf.format(new Date(updateTime))));
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_operas:
                    if (onNotebookOperaListener != null) {
                        onNotebookOperaListener.onNoteOpera(view, note, OPERA_TYPE_NOTE_EDIT_DELETE, data.indexOf
                                (note) + 1);
                    }
                    break;
                case R.id.sdv_img:
                    if (onNotebookOperaListener != null) {
                        onNotebookOperaListener.onNoteOpera(view, note, OPERA_TYPE_NOTE_PREVIEW_PIC, data.indexOf
                                (note) + 1);
                    }
                    break;
            }
        }
    }

    private OnNotebookOperaListener onNotebookOperaListener;

    public void setOnNotebookOperaListener(OnNotebookOperaListener onNotebookOperaListener) {
        this.onNotebookOperaListener = onNotebookOperaListener;
    }

    public interface OnNotebookOperaListener {
        void onNotebookOpera(View v, NotebookDetail notebookDetail, int operaType, int position);

        void onNoteOpera(View v, Note note, int operaType, int position);
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm", Locale.getDefault());
}
