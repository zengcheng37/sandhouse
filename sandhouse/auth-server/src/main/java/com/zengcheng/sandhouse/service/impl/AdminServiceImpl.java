package com.zengcheng.sandhouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zengcheng.sandhouse.entity.Admin;
import com.zengcheng.sandhouse.mapper.AdminMapper;
import com.zengcheng.sandhouse.service.AdminService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员用户表; InnoDB free: 10240 kB 服务实现类
 * </p>
 *
 * @author zengcheng
 * @since 2019-04-16
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
