package com.zengcheng.sandhouse.common.enums;

/**
 * 响应码枚举类
 * @author zengcheng
 * @date 2019/4/12
 */
public enum ResCodeEnum {
    /**
     * example:R01("请求成功") -- code = 01,message = "请求成功"
     */
    R0("请求成功"),
    R01("内部处理异常"),
    R02("不支持的请求方式"),
    R03("会话超时"),
    R04("参数格式错误"),
    R05("您无权限访问此接口!"),
    //自定义错误
    R06("不存在该页面或无该接口!"),

    /**
     * 业务模块响应码
     */
    R1001("用户名或密码错误"),
    ;
    /**
     * 响应码
     */
    private String code;
    /**
     * 响应信息
     */
    private String message;

    ResCodeEnum(String message) {
        this.code = name().substring(1);
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
