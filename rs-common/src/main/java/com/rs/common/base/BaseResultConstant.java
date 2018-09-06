package com.rs.common.base;

/**
 * 系统接口结果常量枚举类
 *
 * @author liegou
 */
public enum BaseResultConstant {

    FAILED(0, "failed"),
    SUCCESS(1, "success"),

    INVALID_LENGTH(10001, "长度不符合规定长度"),
    EMPTY_USERNAME(10101, "账号为空"),
    EMPTY_PASSWORD(10102, "密码为空"),
    INVALID_USERNAME(10103, "账号不存在"),
    INVALID_PASSWORD(10104, "密码错误"),
    INVALID_ACCOUNT(10105, "账号已锁定"),
    UNAUTHORIZED_EXCEPTION(10106, "无此操作权限"),
    INVALIDSESSION_EXCEPTION(10107, "会话已过期"),
    UNREGISTERSYSTEM_EXCEPTION(10108, "未注册的系统");

    public int code;

    public String message;

    BaseResultConstant(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
