package com.culturebud.presenter;

import android.net.Uri;
import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.contract.NoteContract;
import com.culturebud.model.NoteModel;
import com.culturebud.util.ApiException;

import java.net.SocketTimeoutException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/23.
 */

public class NotePresenter extends NoteContract.Presenter {

    public NotePresenter() {
        setModel(new NoteModel());
    }

    @Override
    public void createNote(long notebookId, String content, String chapter, int page, String otherLocation, Uri imgUri) {
        if (!validateToken()) {
            return;
        }
        if (TextUtils.isEmpty(content)) {
            view.onErrorTip("客官，请写下笔记内容或者拍下图片");
            return;
        }
        view.showProDialog();
        model.createNote(BaseApp.getInstance().getUser().getToken(),
                notebookId, content, chapter, page, otherLocation, imgUri)
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
                        } else if (e instanceof SocketTimeoutException) {
                            view.onErrorTip("当前网络不给力，请稍后再试");
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onNoteOpera(aBoolean, NoteContract.View.OPERA_TYPE_CREATE);
                    }
                });
    }

    @Override
    public void deleteNote(long noteId) {
        if (!validateToken()) {
            return;
        }
        model.deleteNote(BaseApp.getInstance().getUser().getToken(), noteId)
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
                        view.onNoteOpera(aBoolean, NoteContract.View.OPERA_TYPE_DELETE);
                    }
                });
    }

    @Override
    public void editNote(long noteId, String content, String chapter, int pages, String otherLocation) {
        if (!validateToken()) {
            return;
        }
        model.editNote(BaseApp.getInstance().getUser().getToken(), noteId, content, chapter, pages, otherLocation)
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
                        view.onNoteOpera(aBoolean, NoteContract.View.OPERA_TYPE_EDIT);
                    }
                });
    }
}
