package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.Note;
import com.culturebud.bean.NotebookDetail;
import com.culturebud.contract.NotebookDetailContract;
import com.culturebud.model.NotebookDetailModel;
import com.culturebud.util.ApiException;

import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/22.
 */

public class NotebookDetailPresenter extends NotebookDetailContract.Presenter {

    public NotebookDetailPresenter() {
        setModel(new NotebookDetailModel());
    }

    @Override
    public void notebookDetail(long noteBookId) {
        if (!validateToken()) {
            return;
        }

        view.showLoadingView();
        model.notebookDetail(BaseApp.getInstance().getUser().getToken(), noteBookId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hiddenNoDataView();

                        String errorMessage = ApiException.getErrorMessage(e);
                        view.showErrorView(errorMessage);
                    }

                    @Override
                    public void onNext(Map<String, Object> res) {
                        view.hiddenNoDataView();
                        if (res.containsKey("relationType")) {
                            view.onRelationType(Integer.valueOf(res.get("relationType").toString()));
                        }
                        if (res.containsKey("notebookDetail")) {
                            view.onNotebookDetail((NotebookDetail) res.get("notebookDetail"));
                        }
                    }
                });
    }

    @Override
    public void notesForNotebook(long notebookId, int page) {
        if (!validateToken()) {
            return;
        }

        view.showLoadingView(page != 0);
        model.notesForNotebook(BaseApp.getInstance().getUser().getToken(), notebookId, page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Note>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hiddenNoDataView();
                        e.printStackTrace();
                        String errorMessage = ApiException.getErrorMessage(e);
                        if (page == 0) {
                            view.showErrorView(errorMessage);
                        } else  {
                            view.onErrorTip(errorMessage);
                        }
                    }

                    @Override
                    public void onNext(List<Note> notes) {
                        view.hiddenNoDataView();

                        if (page == 0 && notes.isEmpty()) {
                            view.showNoDataView("还没有创建笔记");
                        }

                        view.onNotes(notes);
                    }
                });
    }

    @Override
    public void notebookPermissionEdit(long notebookId, int permission) {
        if (!validateToken()) {
            return;
        }
        model.notebookPermissionEdit(BaseApp.getInstance().getUser().getToken(), notebookId, permission)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            view.onNotebookPermissionEdit(aBoolean, notebookId, permission);
                        }
                    }
                });
    }

    @Override
    public void notebookEdit(long notebookId, String name) {
        if (!validateToken()) {
            return;
        }
        model.notebookEdit(BaseApp.getInstance().getUser().getToken(), notebookId, name)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onNotebookEdit(aBoolean, notebookId, name);
                    }
                });
    }

    @Override
    public void deleteNote(long noteId, int position) {
        if (!validateToken()) {
            return;
        }
        view.showProDialog();
        model.deleteNote(BaseApp.getInstance().getUser().getToken(), noteId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        view.hideProDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProDialog();
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onDeleteNote(aBoolean, noteId, position);
                    }
                });
    }
}
