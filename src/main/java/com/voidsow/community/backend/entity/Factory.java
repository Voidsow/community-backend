package com.voidsow.community.backend.entity;

import java.util.Date;

import static com.voidsow.community.backend.constant.Constant.NORMAL_POST;

public class Factory {
    public static Post newPost(int uid, String title, String content) {
        Date now = new Date();
        return new Post(null, uid, title, NORMAL_POST, NORMAL_POST, now, now, 0, null, content);
    }
}
