package com.chenyu.common.datascope.aspect;


import com.chenyu.common.core.utils.StringUtils;
import com.chenyu.common.datascope.annotation.DataScope;
import com.chenyu.common.security.utils.SecurityUtils;
import com.chenyu.common.web.domain.BaseEntity;
import com.chenyu.system.api.domain.SysRole;
import com.chenyu.system.api.domain.SysUser;
import com.chenyu.system.api.model.LoginUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 数据权限注解切面
 *
 * @author chen yu
 * @create 2021-11-15 20:22
 */
@Aspect
@Component
public class DataScopeAspect {


    /**
     * 全部数据权限
     *
     */
    public static final String DATA_SCOPE_ALL = "1";


    /**
     * 自定义数据权限
     *
     */

    public static final String DATA_SCOPE_CUSTOM = "2";


    /**
     * 部门数据权限
     *
     */
    public static final String DATA_SCOPE_DEPT = "3";


    /**
     * 部门及以下数据权限
     *
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";


    /**
     * 仅本人数据权限
     *
     */
    public static final String DATA_SCOPE_SELF = "5";


    /**
     * 数据权限过滤关键字
     *
     */
    public static final String DATA_SCOPE = "dataScope";

    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) {
        //拼接前进行清理 防止注入
        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(final  JoinPoint joinPoint, DataScope controllerDataScope){
        //获取当前用户
        LoginUser loginUser = SecurityUtils.getLoginUser();

        if(StringUtils.isNotNull(loginUser)){
            SysUser currentUser = loginUser.getSysUser();
            if(StringUtils.isNotNull(currentUser)&&!currentUser.isAdmin()){
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(), controllerDataScope.userAlias());
            }
        }
    }



    /**
     *  数据范围过滤逻辑
     *
     */
    public  static void  dataScopeFilter(JoinPoint joinPoint,SysUser currentUser,String deptAlias, String userAlias){
        StringBuilder sqlString = new StringBuilder();
        for (SysRole role : currentUser.getRoles()) {
            String dataScope = role.getDataScope();
            if(DATA_SCOPE_ALL.equals(dataScope)){
                //全部数据范围
                sqlString=new StringBuilder();
                break;
            }else if(DATA_SCOPE_CUSTOM.equals(dataScope)){
                //自定义数据范围
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ",
                        deptAlias,
                        role.getRoleId()
                ));
            }else if(DATA_SCOPE_DEPT.equals(dataScope)){
                //部门数据范围
                sqlString.append(StringUtils.format("OR {}.dept_id={}",currentUser.getDeptId()));

            }else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)){
                //部门及其以下数据权限
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                        deptAlias,
                        currentUser.getDeptId(),
                        currentUser.getDeptId()
                ));
            }else if (DATA_SCOPE_SELF.equals(dataScope)) {
                //本人数据权限
                if (StringUtils.isNotBlank(userAlias)) {
                    sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, currentUser.getUserId()));
                }
                else {
                    // 数据权限为仅本人且没有userAlias别名不查询任何数据
                    sqlString.append(" OR 1=0 ");
                }
            }

        }


        //把拼接好的sql 设置为属性内容
        if (StringUtils.isNotBlank(sqlString.toString())) {
            Object params = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) params;
                baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
            }
        }
    }

    /**
     * 先对属性进行清理
     *
     */
    private void clearDataScope(final JoinPoint joinPoint) {
        Object params = joinPoint.getArgs()[0];
        if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put(DATA_SCOPE, "");
        }
    }


}


