package com.zengcheng.sandhouse.web;

import com.zengcheng.sandhouse.common.entity.ResponseEntity;
import com.zengcheng.sandhouse.dto.AdminLoginDTO;
import com.zengcheng.sandhouse.service.AdminService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 管理员用户相关请求控制器
 * @author zengcheng
 * @date 2019/4/12
 */
@RequestMapping("/admin")
@RestController
public class AdminController {

    @Resource
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity loginAdmin(@RequestBody AdminLoginDTO adminLoginDTO){
        return adminService.adminLogin(adminLoginDTO);
    }

    @GetMapping("/logout")
    public ResponseEntity logoutAdmin(HttpServletRequest request){
        String logoutToken = request.getHeader("accessToken");
        return adminService.adminLogout(logoutToken);
    }

}
