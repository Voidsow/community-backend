package com.voidsow.community.backend.utils;

public class RedisKey {
    private static final String USER = "user:";
    public static final String FOLLOWER = ":follower";
    public static final String FOLLOWEE = ":followee";

    //uid的关注者
    public static String getKey(int uid, String type) {
        StringBuilder builder = new StringBuilder();
        builder.append(USER);
        builder.append(uid);
        builder.append(type);
        return builder.toString();
    }

    //uid关注的人
    public static String getFolloweeKey(int uid) {
        StringBuilder builder = new StringBuilder();
        builder.append(USER);
        builder.append(uid);
        builder.append(FOLLOWEE);
        return builder.toString();
    }

}
