package com.culturebud.model;

import com.culturebud.bean.User;
import com.culturebud.contract.MeContract;
import com.culturebud.db.dao.UserDAO;

import java.sql.SQLException;
import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/10/25.
 */

public class MeModel extends MeContract.Model {
    private UserDAO userDAO;

    public void initDAO() throws SQLException {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
    }

    @Override
    public Observable<Boolean> saveUser(User user) {
        return Observable.create((subscriber -> {
            try {
                initDAO();
                boolean res = userDAO.addUser(user);
                subscriber.onNext(res);
                subscriber.onCompleted();
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        }));
    }

    @Override
    public Observable<User> loadLastUser() {
        return Observable.create(subscriber -> {
            try {
                initDAO();
                List<User> users = userDAO.findAll();
                if (users != null && users.size() > 0) {
                    subscriber.onNext(users.get(0));
                } else {
                    subscriber.onNext(null);
                }
                subscriber.onCompleted();
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }

}
