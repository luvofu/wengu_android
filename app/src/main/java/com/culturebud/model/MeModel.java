package com.culturebud.model;

import android.util.Log;

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
        Log.d("LoginActivity", "userDao 1 = " + userDAO);
        if (userDAO == null) {
            Log.d("LoginActivity", "userDao 2 = " + userDAO);
            synchronized (MeModel.class) {
                Log.d("LoginActivity", "userDao 3 = " + userDAO);
                if (userDAO == null) {
                    Log.d("LoginActivity", "userDao 4 = " + userDAO);
                    userDAO = new UserDAO();
                }
            }
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
//                Log.d("LoginActivity", "userDao = " + userDAO);
                List<User> users = new UserDAO().findAll();
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
