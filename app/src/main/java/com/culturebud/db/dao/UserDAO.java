package com.culturebud.db.dao;

import com.culturebud.BaseApp;
import com.culturebud.bean.User;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by XieWei on 2016/10/26.
 */

public class UserDAO {
    private Dao<User, Long> dao;

    public UserDAO() throws SQLException {
        dao = BaseApp.getInstance().getDataHelper().getDao(User.class);
    }

    private void reinitDao() throws SQLException {
        if (dao == null) {
            synchronized (UserDAO.class) {
                if (dao == null) {
                    dao = BaseApp.getInstance().getDataHelper().getDao(User.class);
                }
            }
        }
    }

    public boolean addUser(User user) throws SQLException {
        return dao.create(user) == 1;
    }

    public boolean delUser(User user) throws SQLException {
        return dao.delete(user) == 1;
    }

    public boolean updateUser(User user) throws SQLException {
        return dao.update(user) == 1;
    }

    public User findByUserName(String userName) throws SQLException {
        List<User> userList = dao.queryForEq("user_name", userName);
        if (userList != null && userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    public List<User> findAll() throws SQLException {
        reinitDao();
        return dao.queryForAll();
    }
}
