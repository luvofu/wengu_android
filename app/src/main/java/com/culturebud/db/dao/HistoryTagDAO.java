package com.culturebud.db.dao;

import com.culturebud.BaseApp;
import com.culturebud.bean.HistoryTag;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XieWei on 2017/4/10.
 */

public class HistoryTagDAO {
    private Dao<HistoryTag, Long> dao;

    public HistoryTagDAO() throws SQLException {
        dao = BaseApp.getInstance().getDataHelper().getDao(HistoryTag.class);
    }

    public boolean addTag(HistoryTag tag) throws SQLException {
        Dao.CreateOrUpdateStatus status = dao.createOrUpdate(tag);
        return status.isCreated() || status.isUpdated();
    }

    public List<HistoryTag> findByTypeAndUserId(byte type, long userId) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        map.put("t_type", type);
        map.put("user_id", userId);
        return dao.queryForFieldValues(map);
    }
}
