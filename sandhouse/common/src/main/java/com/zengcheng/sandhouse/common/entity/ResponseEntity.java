package com.zengcheng.sandhouse.common.entity;

/**
 * 统一响应返回信息封装类
 * @author zengcheng
 * @date 2019/4/12
 * @param <T>
 */
public class ResponseEntity<T> {
    /**
     * 响应码
     */
    private String resCode;
    /**
     * 响应消息
     */
    private String resMessage;
    /**
     * 响应体
     */
    private T resBody;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMessage() {
        return resMessage;
    }

    public void setResMessage(String resMessage) {
        this.resMessage = resMessage;
    }

    public T getResBody() {
        return resBody;
    }

    public void setResBody(T resBody) {
        this.resBody = resBody;
    }

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "resCode='" + resCode + '\'' +
                ", resMessage='" + resMessage + '\'' +
                ", resBody=" + resBody +
                '}';
    }
}
