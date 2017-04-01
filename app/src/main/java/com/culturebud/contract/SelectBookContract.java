package com.culturebud.contract;

import com.culturebud.bean.CollectedBook;
import com.culturebud.model.CollectedBooksModel;

import java.util.List;

/**
 * Created by XieWei on 2017/4/1.
 */

public interface SelectBookContract {
    abstract class Model extends CollectedBooksModel {

    }

    interface View extends BaseView {
        void onBooks(List<CollectedBook> books);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getMyBooks(int page);

        public abstract void getMyBooks(int page, int categoryType, String category);
    }
}
