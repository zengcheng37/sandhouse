package com.zengcheng.sandhouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zengcheng.sandhouse.common.entity.Admin;
import com.zengcheng.sandhouse.common.entity.ResponseEntity;
import com.zengcheng.sandhouse.dto.AdminLoginDTO;

/**
 * <p>
 * 管理员用户表; InnoDB free: 10240 kB 服务类
 * </p>
 *
 * @author zengcheng
 * @since 2019-04-16
 */
public interface AdminService extends IService<Admin> {
    /**
     * 管理员用户登录方法
     * 1.校验用户名密码是否正确.
     * 2.生成一个token放入redis并返回给前端.
     * @param adminLoginDTO
     * @return
     */
    ResponseEntity adminLogin(AdminLoginDTO adminLoginDTO);

    /**
     * 管理员用户登出方法
     * 1.校验token是否存在且是否是管理员用户.
     * 2.删除该token.
     * @param logoutToken 登出用户token
     * @return
     */
    ResponseEntity adminLogout(String logoutToken);
}
