package com.zengcheng.sandhouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zengcheng.sandhouse.common.entity.Admin;
import com.zengcheng.sandhouse.common.entity.ResponseEntity;
import com.zengcheng.sandhouse.common.entity.ResponseEntityFactory;
import com.zengcheng.sandhouse.common.enums.ResCodeEnum;
import com.zengcheng.sandhouse.common.mapper.AdminMapper;
import com.zengcheng.sandhouse.common.util.JwtTokenUtil;
import com.zengcheng.sandhouse.dto.AdminLoginDTO;
import com.zengcheng.sandhouse.service.AdminService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

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

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    @Qualifier("userDetailService")
    private UserDetailsService userDetailsService;

    @Override
    public ResponseEntity adminLogin(AdminLoginDTO adminLoginDTO) {
        //根据用户名和加密后的密码查询数据库
        QueryWrapper<Admin> adminEntityWrapper = new QueryWrapper<>();
        adminEntityWrapper.eq("name",adminLoginDTO.getUserName())
                .eq("use_state",0)
                .eq("delete_state",0);
        Admin admin = getOne(adminEntityWrapper);
        if(StringUtils.isEmpty(admin)
                || !passwordEncoder.matches(adminLoginDTO.getPassword(),admin.getPassword())){
            //用户名或密码错误
            return ResponseEntityFactory.error(ResCodeEnum.R1001);
        }
        //加载该管理员用户相关信息
        UserDetails adminDetails = userDetailsService.loadUserByUsername(adminLoginDTO.getUserName());
        //生成token并放入redis中
        String generatedToken = jwtTokenUtil.generateToken(admin.getName(),"0",adminDetails.getAuthorities());
        return ResponseEntityFactory.success(generatedToken);
    }

    @Override
    public ResponseEntity adminLogout(String logoutToken) {
        //删除Redis中的指定token,无论删除是否成功均响应登出成功
        jwtTokenUtil.deleteToken(logoutToken);
        return ResponseEntityFactory.success("登出成功!");
    }
}
