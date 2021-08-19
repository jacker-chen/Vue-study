package com.bjfn.shop.admin.controller;


import cn.hutool.core.lang.UUID;
import com.bjfn.shop.admin.common.lang.Result;
import com.bjfn.shop.admin.common.util.RedisUtil;
import com.bjfn.shop.admin.common.util.ResultUtil;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

@RestController
public class AuthController {

    @Autowired
    private Producer producer;

    @Autowired
   private RedisUtil redisUtil;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg",byteArrayOutputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:/image:jpeg;base64,";
        String base64Img = encoder.encode(byteArrayOutputStream.toByteArray());


        redisUtil.set(key,code,120);
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("key", key);objectObjectHashMap.put("base64Img", str+base64Img);
        //MapBuilder<Object, Object> put = MapUtil.builder().put("key", key).put("base64Img", base64Img);

        return ResultUtil.success(objectObjectHashMap);
    }
}
