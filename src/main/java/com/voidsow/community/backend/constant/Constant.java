package com.voidsow.community.backend.constant;

public final class Constant {
    //用户激活状态
    static public int UNACTIVATED = 0;
    static public int NORMAL = 1;

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
    static public int INCORRECT = 400;
    static public int EXPIRED = 400;
    static public int INVALID = 400;
    static public int ILLEGAL = 401;
}
