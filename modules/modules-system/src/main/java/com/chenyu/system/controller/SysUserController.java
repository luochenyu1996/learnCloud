package com.chenyu.system.controller;

import com.chenyu.common.core.constant.UserConstants;
import com.chenyu.common.core.domian.R;
import com.chenyu.common.core.utils.SpringUtils;
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
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
     * done
     *
     */
    @RequiresPermissions(value = "system:user:list")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        //使用PageHelper进行分页，这里对原始插件进行了封装
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }


    /**
     *
     *  根据token 获取当前用户信息
     *  done
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
     * done
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
        //岗位信息
        ajaxResult.put("posts", postService.selectPostAll());
        //当前用户的岗位和角色信息
        if (StringUtils.isNotNull(userId)) {
            ajaxResult.put(AjaxResult.DATA_TAG, userService.selectUserById(userId));
            ajaxResult.put("postIds", postService.selectPostListByUserId(userId));
            ajaxResult.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        return ajaxResult;
    }


    /**
     * 新增用户
     * done
     */
    @RequiresPermissions("system:user:add")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        // 问题处理逻辑
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }

        //当前新增用户的信息
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));

        return toAjax(userService.insertUser(user));
    }

    /**
     *  修改用户
     *
     */
    @RequiresPermissions("system:user:edit")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        //检查要修改的用户是否是管理员  如果是则不允许修改
        userService.checkUserAllowed(user);
        //如果手机号或者邮箱已经在数据库中存在一样的  则不允修改
        if(StringUtils.isNotEmpty(user.getPhonenumber())&&UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))){
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }else if (StringUtils.isNotEmpty(user.getEmail()) && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }

        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     * 批量删除
     */
    @RequiresPermissions(value = "system:user:remove")
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, SecurityUtils.getUserId())) {
            //判断要删除的用户中是否有当前用户，如果有则返回删除失败，当前用户不能被删除
            return AjaxResult.error("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置用户密码
     *
     */
    @RequiresPermissions("system:user:edit")
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 修改用户状态
     */
    @RequiresPermissions("system:user:edit")
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUserStatus(user));
    }


    /**
     * 获取用户信息
     *
     * 系统内部接口
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
     *
     * 系统内部接口
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




    //todo  使用excel 导入相应的数据

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







}
