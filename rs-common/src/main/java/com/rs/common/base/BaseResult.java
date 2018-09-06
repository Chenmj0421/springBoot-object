package com.rs.common.base;

import java.io.Serializable;

/**
 * 序列化返回结果类
 * @author liegou
 * @date  .
 */
public class BaseResult<T> implements Serializable {

    /** 接口调用成功，不需要返回对象*/
    public static <T> BaseResult<T> newSuccess(){
        BaseResult<T> result = new BaseResult<>();
        result.setCode(BaseResultConstant.SUCCESS.getCode());
        result.setMessage(BaseResultConstant.SUCCESS.getMessage());
        return result;
    }

    /** 接口调用成功，有返回对象 */
    public static <T> BaseResult<T> newSuccess(T object) {
        BaseResult<T> result = new BaseResult<>();
        result.setCode(BaseResultConstant.SUCCESS.getCode());
        result.setMessage(BaseResultConstant.SUCCESS.getMessage());
        result.setData(object);
        return result;
    }
    /** 接口调用失败，使用默认返回 */
    public static <T> BaseResult<T> newFailure() {
        BaseResult<T> result = new BaseResult<>();
        result.setCode(BaseResultConstant.FAILED.getCode());
        result.setMessage(BaseResultConstant.FAILED.getMessage());
        return result;
    }
    /** 接口调用失败，有错误码和描述，没有返回对象*/
    public static <T> BaseResult<T> newFailure(int code, String message){
        BaseResult<T> result = new BaseResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    /** 接口调用失败，有错误码和描述，有返回对象*/
    public static <T> BaseResult<T> newFailure(int code, String message, T data){
        BaseResult<T> result = new BaseResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /** 接口调用失败，返回异常信息*/
    public static <T> BaseResult<T> newException(Exception e){
        BaseResult<T> result = new BaseResult<>();
        result.setCode(BaseResultConstant.FAILED.getCode());
        result.setMessage(e.getMessage());
        return result;
    }
    /** 状态码：1成功，其他为失败*/
    private int code;
    /** 返回的对象*/
    private T data;
    /** 描述 */
    private String message;

    /** 判断返回结果是否成功*/
    public boolean success() {
        return code == BaseResultConstant.SUCCESS.getCode();
    }
    /** 判断返回结果是否有结果对象*/
    public boolean hasObject() {
        return data!=null;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public T getData() {
        return data;
    }
    public void setData(T object) {
        this.data = object;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Result");
        if(data!=null) {
            result.append("<" + data.getClass().getSimpleName() + ">");
        }
        result.append(": {code="+code);
        if(data!=null) {
            result.append(", data=" + data);
        }
        if(message!=null) {
            result.append(", message=" + message);
        }
        result.append(" }");
        return result.toString();
    }
}