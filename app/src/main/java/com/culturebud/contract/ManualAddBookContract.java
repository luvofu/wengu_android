package com.culturebud.contract;

import android.net.Uri;

import com.culturebud.bean.CheckedBook;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/12/10.
 */

public interface ManualAddBookContract {
    abstract class Model extends BaseModel {
        public abstract Observable<Boolean> addBook(String token, Uri imgUri, String bookName, String nameOrgin,
                                                    String subTitle, String isbn, String authors,
                                                    String translators, String price, String publisher,
                                                    String pubDate, String binding, String pages,
                                                    String summary, String authorInfo);

        public abstract Observable<Boolean> checkBook(String token, CheckedBook checkedBook, Uri imgUri);
    }

    interface View extends BaseView {
        void onAddResult(boolean res);

        void onCheckResult(boolean res);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void submitBook(Uri imgUri, String bookName, String nameOrgin,
                                        String subTitle, String isbn, List<String> authors,
                                        List<String> translators, String price, String publisher,
                                        String pubDate, String binding, String pages,
                                        String summary, String authorInfo);

        public abstract void checkBook(CheckedBook checkedBook, Uri imgUri);
    }
}
