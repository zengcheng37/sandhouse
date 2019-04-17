package com.zengcheng.sandhouse.common.vo;

import com.zengcheng.sandhouse.common.entity.Permission;
import com.zengcheng.sandhouse.common.entity.Role;

import java.util.Objects;
import java.util.Set;

/**
 * 权限信息封装类
 * @author zengcheng
 * @date 2019/4/16
 */
public class RoleVO extends Role {
    /**
     * 角色拥有权限集合
     */
    private Set<Permission> permissions;

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleVO roleVO = (RoleVO) o;
        return Objects.equals(permissions, roleVO.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissions);
    }

    @Override
    public String toString() {
        return "RoleVO{" +
                "permissions=" + permissions +
                '}';
    }
}
