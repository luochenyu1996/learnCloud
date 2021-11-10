package com.chenyu.common.web.controller;


import com.chenyu.common.core.utils.DateUtils;
import com.chenyu.common.web.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * @author chen yu
 * @create 2021-10-29 16:58
 */
public class BaseController {
    protected final Logger logger= LoggerFactory.getLogger(this.getClass());


    /**
     * @InitBinder   用于在@Controller中标注于方法，表示为当前控制器注册一个属性编辑器或者其他，只对当前的Controller有效。
     * WebDataBinder是用来绑定请求参数到指定的属性编辑器.
     */
    //将前台传递过来的日期格式字符串，抓换为 Date:类型
    @InitBinder
    public void  iniBinder(WebDataBinder binder){
        binder.registerCustomEditor(Date.class,new PropertyEditorSupport(){
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result) {
        return result ? success() : error();
    }

    /**
     * 返回成功
     */
    public AjaxResult success() {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error() {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message) {
        return AjaxResult.success(message);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message) {
        return AjaxResult.error(message);
    }





}
