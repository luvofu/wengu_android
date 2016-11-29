package com.culturebud.db.dao;

import com.culturebud.BaseApp;
import com.culturebud.bean.Book;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by XieWei on 2016/10/27.
 */

public class BookDAO {

    private Dao<Book, Long> dao;

    public BookDAO() throws SQLException {
        dao = BaseApp.getInstance().getDataHelper().getDao(Book.class);
    }

    public boolean addBook(Book book) throws SQLException {
        Dao.CreateOrUpdateStatus status = dao.createOrUpdate(book);
        return status.isCreated() || status.isUpdated();
//        return dao.create(book) == 1;
    }

    public boolean delBook(Book book) throws SQLException {
        return dao.delete(book) == 1;
    }

    public boolean updateBook(Book book) throws SQLException {
        return dao.update(book) == 1;
    }

    public List<Book> findAll() throws SQLException {
        return dao.queryForAll();
    }
}
