package com.culturebud.contract;

import android.net.Uri;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/23.
 */

public interface NoteContract {
    abstract class Model extends BaseModel {
        /**
         * String token;令牌
         * long notebookId;笔记本id
         * String content;内容
         * String chapter;章节
         * int pages;页码
         * String otherLocation;其他位置信息
         * File imageFile;图片文件
         *
         * @return
         */
        public abstract Observable<Boolean> createNote(String token, long notebookId,
                                                       String content, String chapter,
                                                       int page, String otherLocation,
                                                       Uri imgUri);

        public abstract Observable<Boolean> deleteNote(String token, long noteId);

        public abstract Observable<Boolean> editNote(String token, long noteId, String content,
                                                     String chapter, int pages, String otherLocation);
    }

    interface View extends BaseView {
        public static final int OPERA_TYPE_CREATE = 0;
        public static final int OPERA_TYPE_DELETE = 1;
        public static final int OPERA_TYPE_EDIT = 2;
        void onNoteOpera(boolean res, int operaType);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void createNote(long notebookId, String content,
                                        String chapter, int page,
                                        String otherLocation, Uri imgUri);

        public abstract void deleteNote(long noteId);

        public abstract void editNote(long noteId, String content, String chapter, int pages, String otherLocation);

        public abstract void editCover(Uri uri, long bsId);
    }
}
