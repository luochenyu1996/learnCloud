package com.chenyu.system.controller;

import com.chenyu.common.web.controller.BaseController;
import com.chenyu.system.service.ISysRoleService;
import com.chenyu.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  用户信息
 *
 * @author chen yu
 * @create 2021-10-29 16:53
 */
@RestController
@RequestMapping("/user")
public class SysUserController  extends BaseController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private ISysConfigService configService;






}
