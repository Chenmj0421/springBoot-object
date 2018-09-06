package com.rs.visit.common;

/**
 * visit系统接口结果常量枚举类
 * @author liegou
 */
public enum VisitResultConstant {

   ;

    public int code;

    public String message;

    VisitResultConstant(int code, String message) {
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
