package com.chenyu.common.core.utils.sql;

import com.chenyu.common.core.utils.StringUtils;
import com.chenyu.common.exception.UtilException;

/**
 * sql  操作工具类
 *
 * @author chen yu
 * @create 2021-11-10 20:48
 */
public class SqlUtil {

    //sql参数的类型
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * 检查字符，防止注入绕过
     *
     */
    public  static  String escapeOrderBySql(String value){
        if(StringUtils.isNotEmpty(value)&&!isValidOrderBySql(value)){
            throw  new UtilException("参数不符合规范，不能进查询");
        }
        return  value;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }

}
