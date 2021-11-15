package com.chenyu.system.controller;

import com.chenyu.common.core.constant.UserConstants;
import com.chenyu.common.core.domian.R;
import com.chenyu.common.core.utils.StringUtils;
import com.chenyu.common.security.annotation.InnerAuth;
import com.chenyu.common.security.annotation.RequiresPermissions;
import com.chenyu.common.security.utils.SecurityUtils;
import com.chenyu.common.web.controller.BaseController;
import com.chenyu.common.web.domain.AjaxResult;
import com.chenyu.common.web.page.TableDataInfo;
import com.chenyu.system.api.domain.SysRole;
import com.chenyu.system.api.domain.SysUser;
import com.chenyu.system.api.model.LoginUser;
import com.chenyu.system.service.*;
import com.chenyu.system.service.imple.SysPermissionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author chen yu
 * @create 2021-10-29 16:53
 */
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {

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

    /**
     * 获取用户列表
     *
     * @RequiresPermissions 自定义注解+切面
     */
    @RequiresPermissions("system:user:list")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

   //todo
    @RequiresPermissions("system:user:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) throws IOException {

    }

    //todo
    @RequiresPermissions("system:user:import")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        return  null;
    }

    //todo
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {

    }

    /**
     * 获取用户信息
     *
     */
    @InnerAuth
    @GetMapping("/info/{username}")
    public R<LoginUser> info(@PathVariable("username") String username){
        SysUser sysUser = userService.selectUserByUserName(username);
        if(StringUtils.isNull(sysUser)){
            return R.fail("用户名或密码错误");
        }
        //获取该用户的角色名称集合
        Set<String> roles= permissionService.getRolePermission(sysUser.getUserId());
        //获取该用户的权限集合
        Set<String> menuPerms = permissionService.getMenuPermission(sysUser.getUserId());
        LoginUser loginUser = new LoginUser();
        loginUser.setSysUser(sysUser);
        loginUser.setRoles(roles);
        loginUser.setPermissions(menuPerms);
        return  R.ok(loginUser);
    }

    /**
     * 注册用户
     * 服务内部调用接口？
     */

    @InnerAuth
    @PostMapping("/register")
    public R<Boolean> register(@RequestBody SysUser sysUser) {
        String userName = sysUser.getUserName();
        if(!"true".equals(configService.selectConfigByKey("sys.account.registerUser")) ){
            return R.fail("当前系统未开启注册功能");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(userName))) {
            return R.fail("保存用户'" + userName + "'失败，注册账号已存在");
        }
        return R.ok(userService.registerUser(sysUser));
    }

    /**
     *  获取当前用户信息
     *
     *
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        Long userId = SecurityUtils.getUserId();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(userId);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(userId);
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put("user", userService.selectUserById(userId));
        ajaxResult.put("roles", roles);
        ajaxResult.put("permissions", permissions);
        return ajaxResult;
    }

    /**
     * 根据用户ID获取详细信息
     *
     */
    @RequiresPermissions("system:user:query")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        //检查数据范围权限  如果没有权限则会抛出异常
        userService.checkUserDataScope(userId);
        AjaxResult ajaxResult = AjaxResult.success();
        //获取全部的角色
        List<SysRole> roles = roleService.selectRoleAll();

        //如果是管理员则 返回所有的角色，如果不是管理员则返回部分角色  todo 为什么这里把除管理员外的角色都进行返回了
        ajaxResult.put("roles",
                SysUser.isAdmin(userId)?roles:roles.stream().filter(r->!r.isAdmin()).collect(Collectors.toList())
        );



    }














}
