package com.chenyu.system.service.imple;

import com.chenyu.common.core.constant.UserConstants;
import com.chenyu.common.core.utils.SpringUtils;
import com.chenyu.common.core.utils.StringUtils;
import com.chenyu.common.exception.ServiceException;
import com.chenyu.common.security.utils.SecurityUtils;
import com.chenyu.system.api.domain.SysUser;
import com.chenyu.system.domain.SysUserPost;
import com.chenyu.system.domain.SysUserRole;
import com.chenyu.system.mapper.SysUserMapper;
import com.chenyu.system.mapper.SysUserPostMapper;
import com.chenyu.system.mapper.SysUserRoleMapper;
import com.chenyu.system.service.ISysConfigService;
import com.chenyu.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户  业务处理层
 *
 * @author chen yu
 * @create 2021-11-10 18:17
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;


    @Autowired
    private SysUserRoleMapper userRoleMapper;


    @Autowired
    private ISysConfigService configService;


    @Override
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);

    }

    @Override
    public List<SysUser> selectAllocatedList(SysUser user) {
        return null;
    }

    @Override
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return null;
    }

    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);

    }

    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    @Override
    public String selectUserRoleGroup(String userName) {
        return null;
    }

    @Override
    public String selectUserPostGroup(String userName) {
        return null;
    }


    @Override
    public String checkUserNameUnique(String userName) {
        int count = userMapper.checkUserNameUnique(userName);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }

    }

    @Override
    public void checkUserDataScope(Long userId) {
        //先判断是否是管理员  是的话直接通过
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysUser sysUser = new SysUser();
            sysUser.setUserId(userId);

            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(sysUser);
            if (StringUtils.isEmpty(users)) {
                // 如果查不出数据 则表示没有权限 抛出异常
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }


    /**
     * 导入单个用户信息
     *
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联  新增表 user-post中的信息
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }


    @Override
    public boolean registerUser(SysUser user) {
        return userMapper.insertUser(user) > 0;
    }


    @Override
    public int updateUser(SysUser user) {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    @Override
    public void insertUserAuth(Long userId, Long[] roleIds) {

    }

    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUser(user);
    }

    @Override
    public int updateUserProfile(SysUser user) {
        return 0;
    }

    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return false;
    }

    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    @Override
    public int resetUserPwd(String userName, String password) {
        return 0;
    }

    @Override
    public int deleteUserById(Long userId) {
        return 0;
    }

    @Override
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
        }
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userIds);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPost(userIds);
        return userMapper.deleteUserByIds(userIds);
    }


    /**
     * 批量导入用户数据
     *
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 新增用户岗位信息
     */
    public void insertUserPost(SysUser user) {
        Long[] postIds = user.getPostIds();

        if (StringUtils.isNotNull(postIds)) {

            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();

            for (Long postId : postIds) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }

            if (list.size() > 0) {
                //一次性插入
                userPostMapper.batchUserPost(list);
            }
        }

    }

    /**
     * 新增用户角色信息 对应 user-role表
     */
    public void insertUserRole(SysUser user) {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }
}
