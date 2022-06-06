package com.voidsow.community.backend.constant;

public final class Constant {
    //用户激活状态
    static public int UNACTIVATED = 0;
    static public int NORMAL = 1;

    //帖子类型
    static public int NORMAL_POST = 0;
    static public int STICKY_POST = 1;
    static public int STAR_POST = 1;

    //用户类型
    static public int COMMON = 0;

    //评论类型
    static public int POST_LEVEL_ONE = 0;
    static public int POST_LEVEL_TWO = 1;

    //消息状态
    static public int UNREAD = 0;
    static public int READ = 1;

    //点赞类型
    static public int POST = 0;
    static public int COMMENT = 1;

    //通信状态码
    static public int SUCCESS = 200;
    static public int INCORRECT = 600;
    static public int EXPIRED = 601;
    static public int INVALID = 602;
    static public int ILLEGAL = 603;
    static public int RESOURCE_NOT_FOUND = 404;
    static public int NOT_SUPPORT = 405;
}
