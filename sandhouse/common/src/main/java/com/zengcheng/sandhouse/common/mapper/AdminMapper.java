package com.zengcheng.sandhouse.common.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zengcheng.sandhouse.common.entity.Admin;
import com.zengcheng.sandhouse.common.vo.AdminVO;

/**
 * <p>
 * 管理员用户表; InnoDB free: 10240 kB Mapper 接口
 * </p>
 *
 * @author zengcheng
 * @since 2019-04-16
 */
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 通过name查询管理员用户相关信息
     * @param adminName name
     * @return
     */
    AdminVO selectAdminDetailByAdminName(String adminName);

}
