package com.rs.doctor.common;

/**
 * Doctor系统接口结果常量枚举类
 * @author liegou
 */
public enum DoctorResultConstant {

   ;

    public int code;

    public String message;

    DoctorResultConstant(int code, String message) {
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
