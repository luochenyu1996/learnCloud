package com.chenyu.system.service.imple;

import com.chenyu.commom.datascope.annotation.DataScope;
import com.chenyu.common.core.constant.UserConstants;
import com.chenyu.common.core.utils.SpringUtils;
import com.chenyu.common.core.utils.StringUtils;
import com.chenyu.common.exception.ServiceException;
import com.chenyu.common.security.utils.SecurityUtils;
import com.chenyu.system.api.domain.SysUser;
import com.chenyu.system.mapper.SysUserMapper;
import com.chenyu.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
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
        return null;
    }

    @Override
    public SysUser selectUserById(Long userId) {
        return  userMapper.selectUserById(userId);
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
        return null;
    }

    @Override
    public String checkEmailUnique(SysUser user) {
        return null;
    }

    @Override
    public void checkUserAllowed(SysUser user) {

    }

    @Override
    public void checkUserDataScope(Long userId) {
        //先判断是否是管理员  是的话直接通过
        if(!SysUser.isAdmin(SecurityUtils.getUserId())){
            SysUser sysUser = new SysUser();
            sysUser.setUserId(userId);

            //todo  这里通过代理是什么意思？
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(sysUser);
            if (StringUtils.isEmpty(users)) {
                // 如果查不出数据 则表示没有权限 抛出异常
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }



    @Override
    public int insertUser(SysUser user) {
        return 0;
    }


    @Override
    public boolean registerUser(SysUser user) {
        return userMapper.insertUser(user) > 0;
    }

    @Override
    public int updateUser(SysUser user) {
        return 0;
    }

    @Override
    public void insertUserAuth(Long userId, Long[] roleIds) {

    }

    @Override
    public int updateUserStatus(SysUser user) {
        return 0;
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
        return 0;
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
        return 0;
    }

    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        return null;
    }
}
