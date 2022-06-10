package com.voidsow.community.backend.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//链式调用
//fluent API
public class Event {
    //通知的类型
    private String topic;
    //通知的发起者uid
    int sourceUid;
    int toUid;
    //通知的接收者uid
    Date time;
    //其余的自定义字段
    Map<String, Object> properties = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getSourceUid() {
        return sourceUid;
    }

    public Event setSourceUid(int sourceUid) {
        this.sourceUid = sourceUid;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public Event setTime(Date time) {
        this.time = time;
        return this;
    }

    public int getToUid() {
        return toUid;
    }

    public Event setToUid(int toUid) {
        this.toUid = toUid;
        return this;
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Event setProperties(Map<String, Object> properties) {
        this.properties = properties;
        return this;
    }

    public Event addProperty(String key, Object value) {
        properties.put(key, value);
        return this;
    }
}
