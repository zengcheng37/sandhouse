package com.zengcheng.sandhouse.dto;

/**
 * 管理员用户登录参数封装类
 * @author zengcheng
 * @date 2019/4/20
 */
public class AdminLoginDTO {

    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AdminLoginDTO{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
