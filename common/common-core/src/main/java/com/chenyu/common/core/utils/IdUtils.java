package com.chenyu.common.core.utils;

import com.chenyu.common.core.text.UUID;

/**
 * ID生成器
 *
 * @author: chen yu
 * @create: 2021-10-20 21:04
 */
public class IdUtils {

    /**
     * 简化的UUID，去掉了横线
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString(true);
    }


    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUUID() {
        return UUID.fastUUID().toString();
    }

}
