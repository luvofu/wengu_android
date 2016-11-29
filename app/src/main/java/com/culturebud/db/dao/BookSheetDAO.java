package com.culturebud.db.dao;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookSheet;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by XieWei on 2016/10/27.
 */

public class BookSheetDAO {
    private Dao<BookSheet, Long> dao;

    public BookSheetDAO() throws SQLException {
        dao = BaseApp.getInstance().getDataHelper().getDao(BookSheet.class);
    }

    public boolean addBookSheet(BookSheet bookSheet) throws SQLException {
        return dao.create(bookSheet) == 1;
    }

    public boolean delBookSheet(BookSheet bookSheet) throws SQLException {
        return dao.delete(bookSheet) == 1;
    }

    public boolean updateBookSheet(BookSheet bookSheet) throws SQLException {
        return dao.update(bookSheet) == 1;
    }

    public List<BookSheet> findAll() throws SQLException {
        return dao.queryForAll();
    }
}
