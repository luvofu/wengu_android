package com.culturebud.db.dao;

import com.culturebud.BaseApp;
import com.culturebud.bean.SearchKeyword;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by XieWei on 2016/11/5.
 */

public class SearchKeywordDAO {
    private Dao<SearchKeyword, Integer> dao;

    public SearchKeywordDAO() throws SQLException {
        dao = BaseApp.getInstance().getDataHelper().getDao(SearchKeyword.class);
    }

    public SearchKeyword save(SearchKeyword keyword) throws SQLException {
        SearchKeyword sk;
        if ((sk = findByKyeword(keyword.getKeyword())) != null) {
                return sk;
        }
        return dao.createIfNotExists(keyword);
    }

    public SearchKeyword findByKyeword(String keyword) throws SQLException {
        List<SearchKeyword> sks = dao.queryForEq("search_keyword", keyword);
        if (sks != null && sks.size() > 0) {
            return sks.get(0);
        }
        return null;
    }

    public List<SearchKeyword> getAllByType(int type) throws SQLException {
        return dao.queryForEq("search_type", type);
    }

    public boolean deleteAll(List<SearchKeyword> keywords) throws SQLException {
        return dao.delete(keywords) > 0;
    }
}
