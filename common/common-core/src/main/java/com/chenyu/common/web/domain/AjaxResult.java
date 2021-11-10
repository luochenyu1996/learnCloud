package com.chenyu.common.web.domain;

import com.chenyu.common.core.constant.HttpStatus;
import com.chenyu.common.core.utils.StringUtils;

import java.util.HashMap;

/**
 * 统一响应结果
 */
public class AjaxResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    //状态码
    public static final String CODE_TAG = "code";

    //返回内容和
    public static final String MSG_TAG = "msg";

    //数据对象
    public static final String DATA_TAG = "data";

    public AjaxResult() {
    }


    /**
     * 构造1
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  返回内瓤
     */

    public AjaxResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }


    /**
     * 构造2
     *
     * @param code
     * @param code
     * @return
     */
    public AjaxResult(int code, String msg, Object date) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(date)) {
            super.put(DATA_TAG, date);
        }
    }

    //构造成功的消息

    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(HttpStatus.SUCCESS, msg, data);
    }


    public static AjaxResult success(String msg) {
        return AjaxResult.success(msg, null);
    }

    public static AjaxResult success(Object data) {
        return AjaxResult.success("操作成功", data);
    }


    public static AjaxResult success() {
        return AjaxResult.success("操作成功");
    }


    //构造错误消息

    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(HttpStatus.ERROR, msg, data);
    }


    public static AjaxResult error(String msg) {
        return AjaxResult.success(msg, null);
    }

    public static AjaxResult error(Object data) {
        return AjaxResult.success("操作失败", data);
    }


    public static AjaxResult error() {
        return AjaxResult.success("操作失败");
    }


   /**
    *  方便链式调用
    *
    */
    @Override
    public AjaxResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
