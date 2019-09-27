package com.zengcheng.sandhouse.common.util;

import com.zengcheng.sandhouse.common.entity.Permission;
import com.zengcheng.sandhouse.common.mapper.AdminMapper;
import com.zengcheng.sandhouse.common.vo.AdminVO;
import com.zengcheng.sandhouse.common.vo.RoleVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * 管理员用户UserDetailService自实现类
 * @author zengcheng
 * @date 2019/4/16
 */
@Service(value = "userDetailService")
public class AdminUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private AdminMapper adminMapper;

    /**
     * 通过adminName来加载用户信息
     * @param adminName 要查询的管理员用户name
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String adminName) {
        AdminVO adminVO = adminMapper.selectAdminDetailByAdminName(adminName);
        if(adminVO == null){
            throw new UsernameNotFoundException(adminName);
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for(RoleVO roleVO: adminVO.getRoles()){
            //角色必须是ROLE_开头，可以在数据库中设置
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roleVO.getName());
            grantedAuthorities.add(grantedAuthority);
            //获取权限
            for(Permission permission : roleVO.getPermissions()){
                GrantedAuthority authority = new SimpleGrantedAuthority(permission.getUrl());
                grantedAuthorities.add(authority);
            }
        }
        return new User(adminVO.getName(),adminVO.getPassword(),
                // 可用性 :true:可用 false:不可用
                adminVO.getUseState() == 0,
                // 过期性 :true:没过期 false:过期
                true,
                // 有效性 :true:凭证有效 false:凭证无效
                true,
                // 锁定性 :true:未锁定 false:已锁定
                true,
                grantedAuthorities);
    }
}
