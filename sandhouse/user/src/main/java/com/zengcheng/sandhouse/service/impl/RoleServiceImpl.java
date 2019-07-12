package com.zengcheng.sandhouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zengcheng.sandhouse.common.entity.Role;
import com.zengcheng.sandhouse.common.mapper.RoleMapper;
import com.zengcheng.sandhouse.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表; InnoDB free: 10240 kB 服务实现类
 * </p>
 *
 * @author zengcheng
 * @since 2019-04-16
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
