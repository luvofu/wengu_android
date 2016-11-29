package com.culturebud.contract;

import com.culturebud.bean.Note;
import com.culturebud.bean.NotebookDetail;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/22.
 */

public interface NotebookDetailContract {
    abstract class Model extends BaseModel {
        public abstract Observable<Map<String, Object>> notebookDetail(String token, long notebookId);

        public abstract Observable<List<Note>> notesForNotebook(String token, long notebookId, int page);

        public abstract Observable<Boolean> notebookPermissionEdit(String token, long notebookId, int permission);

        public abstract Observable<Boolean> notebookEdit(String token, long notebookId, String name);

        public abstract Observable<Boolean> deleteNote(String token, long noteId);
    }

    interface View extends BaseView {

        void onNotebookDetail(NotebookDetail notebookDetail);

        void onRelationType(int relationType);

        void onNotes(List<Note> notes);

        void onNotebookPermissionEdit(boolean res, long notebookId, int permission);

        void onNotebookEdit(boolean res, long notebookId, String name);

        void onDeleteNote(boolean res, long noteId, int position);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void notebookDetail(long notebookId);

        public abstract void notesForNotebook(long notebookId, int page);

        public abstract void notebookPermissionEdit(long notebookId, int permission);

        public abstract void notebookEdit(long notebookId, String name);

        public abstract void deleteNote(long noteId, int position);
    }
}
