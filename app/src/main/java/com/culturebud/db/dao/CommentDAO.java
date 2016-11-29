package com.culturebud.db.dao;

import com.culturebud.BaseApp;
import com.culturebud.bean.Comment;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by XieWei on 2016/10/27.
 */

public class CommentDAO {
    private Dao<Comment, Long> dao;

    public CommentDAO() throws SQLException {
        dao = BaseApp.getInstance().getDataHelper().getDao(Comment.class);
    }

    public boolean addComment(Comment comment) throws SQLException {
        return dao.create(comment) == 1;
    }

    public boolean delComment(Comment comment) throws SQLException {
        return dao.delete(comment) == 1;
    }

    public boolean updateComment(Comment comment) throws SQLException {
        return dao.update(comment) == 1;
    }

    public List<Comment> findAll() throws SQLException {
        return dao.queryForAll();
    }

}
