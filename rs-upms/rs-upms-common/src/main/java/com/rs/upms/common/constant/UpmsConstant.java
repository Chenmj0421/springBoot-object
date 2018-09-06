package com.rs.upms.common.constant;

import com.rs.common.base.BaseConstants;

/**
 * upms系统通用常量类类
 * @author liegou
 */
public class UpmsConstant extends BaseConstants {

    public static final String UPMS_TYPE = "rs.upms.type";
    /** 访问的程序为服务端key*/
    public static final String UPMS_TYPE_SERVER = "server";
    /** 访问的程序为客户端*/
    public static final String UPMS_TYPE_CLIENT = "client";

    /** 会话key*/
    public static final String RS_UPMS_SHIRO_SESSION_ID = "rs-upms-shiro-session-id";
    /** 全局会话key*/
    public static final String RS_UPMS_SERVER_SESSION_ID = "rs-upms-server-session-id";
    /** 全局会话列表key*/
    public static final String RS_UPMS_SERVER_SESSION_IDS = "rs-upms-server-session-ids";
    /** code key*/
    public static final String RS_UPMS_SERVER_CODE = "rs-upms-server-code";
    /** 局部会话key*/
    public static final String RS_UPMS_CLIENT_SESSION_ID = "rs-upms-client-session-id";
    /** 单点同一个code所有局部会话key*/
    public static final String RS_UPMS_CLIENT_SESSION_IDS = "rs-upms-client-session-ids";
}
