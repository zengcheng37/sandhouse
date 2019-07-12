package com.zengcheng.sandhouse.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zengcheng.sandhouse.common.entity.Permission;

import java.util.List;

/**
 * <p>
 * 权限表; InnoDB free: 10240 kB Mapper 接口
 * </p>
 *
 * @author zengcheng
 * @since 2019-04-16
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 通过roleId查询权限集合
     * @param roleId 要查询的角色id
     * @return
     */
    List<Permission> selectPermissionsByRoleId(Integer roleId);

}
