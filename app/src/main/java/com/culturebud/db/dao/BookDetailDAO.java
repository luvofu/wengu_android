package com.culturebud.db.dao;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookDetail;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by XieWei on 2016/11/7.
 */

public class BookDetailDAO {
    private Dao<BookDetail, Long> dao;

    public BookDetailDAO() throws SQLException {
        dao = BaseApp.getInstance().getDataHelper().getDao(BookDetail.class);
    }

    public boolean save(BookDetail bookDetail) throws SQLException {
        Dao.CreateOrUpdateStatus status = dao.createOrUpdate(bookDetail);
        return status.isCreated() || status.isUpdated();
    }

    public BookDetail findByBookId(long bookId) throws SQLException {
        return dao.queryForId(bookId);
    }
}
