package com.zengcheng.sandhouse.vo;

import com.zengcheng.sandhouse.entity.Admin;

import java.util.List;

/**
 * 管理员用户信息
 * @author zengcheng
 * @date 2019/4/16
 */
public class AdminVO extends Admin {
    /**
     * 管理员用户拥有角色
     */
    private List<RoleVO> roles ;

    public List<RoleVO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleVO> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "AdminVO{" +
                "roles=" + roles +
                '}';
    }
}
