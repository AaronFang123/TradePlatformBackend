package com.xsjt.learn.neo4j.model;

/**
 * 定义 接口响应数据规范
 */

public class Result {

    //内部逻辑代码
    private int errorCode;
    private String msg;
    private Object data;

    public Result(int errorCode, String msg, Object data) {
        this.errorCode = errorCode;
        this.msg = msg;
        this.data = data;
    }

    public Result(int errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public Result() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + errorCode +
                ", message='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
