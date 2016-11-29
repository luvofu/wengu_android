package com.culturebud.db.dao;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookCommunity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by XieWei on 2016/11/6.
 */

public class BookCommunityDAO {
    private Dao<BookCommunity, Long> dao;

    public BookCommunityDAO() throws SQLException {
        dao = BaseApp.getInstance().getDataHelper().getDao(BookCommunity.class);
    }

    public void save(BookCommunity community) {

    }

}
