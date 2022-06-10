package com.voidsow.community.backend.mapper;

import com.voidsow.community.backend.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface NotificationMapper {
    void insert(Notification notification);

    List<Notification> select(int topic, Integer sourceUid, int toUid, Integer entityId, String props);

    void updateTime(int topic, int sourceUid, int toUid, Date time);

    void updateTimeById(int id, Date time);
}
