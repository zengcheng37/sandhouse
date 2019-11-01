package com.zengcheng.sandhouse.vo;

import com.zengcheng.sandhouse.entity.Permission;
import com.zengcheng.sandhouse.entity.Role;

import java.util.List;

/**
 * 权限信息封装类
 * @author zengcheng
 * @date 2019/4/16
 */
public class RoleVO extends Role {
    /**
     * 角色拥有权限集合
     */
    private List<Permission> permissions;

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "RoleVO{" +
                "permissions=" + permissions +
                '}';
    }
}
