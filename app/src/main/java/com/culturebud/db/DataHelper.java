package com.culturebud.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.culturebud.BaseApp;
import com.culturebud.bean.Book;
import com.culturebud.bean.BookDetail;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.Comment;
import com.culturebud.bean.HistoryTag;
import com.culturebud.bean.SearchKeyword;
import com.culturebud.bean.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by XieWei on 2016/10/26.
 */

public class DataHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = "DataHelper";
    private static final String DB_NAME = "culturebud.db3";
    private static final int VERSION_CODE = 2;

    public DataHelper() {
        this(BaseApp.getInstance(), DB_NAME, null, VERSION_CODE);
    }

    public DataHelper(Context context, String databaseName, SQLiteDatabase
            .CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            createVersionOneTables(connectionSource);
            createVersionTwoTables(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(TAG, "onCreate() --> error");
        }
    }

    private void createVersionOneTables(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTable(connectionSource, User.class);
        TableUtils.createTable(connectionSource, Book.class);
        TableUtils.createTable(connectionSource, BookSheet.class);
        TableUtils.createTable(connectionSource, Comment.class);
        TableUtils.createTable(connectionSource, SearchKeyword.class);
        TableUtils.createTable(connectionSource, BookDetail.class);
    }

    private void createVersionTwoTables(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTable(connectionSource, HistoryTag.class);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {
            database.beginTransaction();
            switch (oldVersion) {
                case 0:
                    createVersionTwoTables(connectionSource);
            }
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }
}
