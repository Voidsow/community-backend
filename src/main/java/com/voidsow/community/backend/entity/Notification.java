package com.voidsow.community.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Notification {
    Integer id;
    int topic;
    int sourceUid;
    int toUid;
    Integer entityId;
    String props;
    int status;
    Date time;

    public Notification(int topic, int sourceUid, int toUid, int status, Date time) {
        this.topic = topic;
        this.sourceUid = sourceUid;
        this.toUid = toUid;
        this.status = status;
        this.time = time;
    }
}
