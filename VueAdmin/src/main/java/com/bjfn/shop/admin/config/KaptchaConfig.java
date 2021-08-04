package com.bjfn.shop.admin.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


/**
 * 验证码配置类
 */

@Configuration
public class KaptchaConfig {

    @Bean
    DefaultKaptcha producer(){
        Properties properties = new Properties();
        properties.put("kaptcha.border","no");//是否有边框
        properties.put("kaptcha.textproducer.font.color","black");//文本颜色
        properties.put("kaptcha.textproducer.char.space","4");//字符间空格
        properties.put("kaptcha.image.width","160");
        properties.put("kaptcha.image.height","60");
        properties.put("kaptch.textproducer.font.size","30");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
