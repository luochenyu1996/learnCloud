package com.chenyu.system.service.imple;

import com.chenyu.common.core.utils.StringUtils;
import com.chenyu.system.mapper.SysMenuMapper;
import com.chenyu.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜单业务层处理类
 *
 * @author chen yu
 * @create 2021-11-10 22:55
 */
@Service
public class ISysMenuServiceImpl implements ISysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;


    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> menuPerms = menuMapper.selectMenuPermsByUserId(userId);
        HashSet<String>  menuPermsSet = new HashSet<>();
        for(String  menuPerm:menuPerms ){
            if(StringUtils.isNotNull( menuPerm)){
                menuPermsSet.addAll(Arrays.asList(menuPerm.trim().split(",")));
            }
        }
        return menuPermsSet;
    }


}
