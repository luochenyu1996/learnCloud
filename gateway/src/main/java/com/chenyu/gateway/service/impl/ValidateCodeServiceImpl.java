package com.chenyu.gateway.service.impl;

import com.chenyu.common.core.constant.Constants;
import com.chenyu.common.core.utils.IdUtils;
import com.chenyu.common.core.utils.StringUtils;
import com.chenyu.common.core.utils.sign.Base64;
import com.chenyu.common.exception.CaptchaException;
import com.chenyu.common.redis.service.RedisService;
import com.chenyu.common.web.domain.AjaxResult;
import com.chenyu.gateway.config.properties.CaptchaProperties;
import com.chenyu.gateway.service.ValidateCodeService;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;


import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码相关
 *
 * @author: chen yu
 * @create: 2021-10-20 20:51
 */
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private CaptchaProperties captchaProperties;

    @Autowired
    private RedisService redisService;


    /**
     * 生成验证码
     *
     */
    @Override
    public AjaxResult createCapcha() throws IOException, CaptchaException {
        AjaxResult ajaxResult = AjaxResult.success();
        Boolean captchaOnOff = captchaProperties.getEnabled();
        ajaxResult.put("captchaOnOff", captchaOnOff);
        if (!captchaOnOff) {  //验证码关闭直接返回
            return ajaxResult;
        }
        String uuid = IdUtils.simpleUUID();
        //在redis中的key
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null;
        String code = null;
        BufferedImage image = null;

        //获取配置文件中的验证码类型
        String captchaType = captchaProperties.getType();

        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            System.out.println("生成的验证码:" + capText);
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            //从字符串对象转换为图形对象
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            //生成字符类型的验证码
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        //对验证码进行缓存
        redisService.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }
        ajaxResult.put("uuid", uuid);
        ajaxResult.put("img", Base64.encode(os.toByteArray()));
        return ajaxResult;
    }

    /**
     * 校验验证码
     *
     */
    @Override
    public void checkCapcha(String code, String uuid) throws CaptchaException {
        if (StringUtils.isEmpty(code)){
            throw new CaptchaException("验证码不能为空");
        }

        if (StringUtils.isEmpty(uuid)) {
            throw new CaptchaException("验证码已失效");
        }

        //  取了就要删除
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisService.getCacheObject(verifyKey);
        redisService.deleteObject(verifyKey);

        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException("验证码错误");
        }


    }
}
