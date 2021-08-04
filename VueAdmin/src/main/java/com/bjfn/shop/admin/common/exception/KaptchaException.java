package com.bjfn.shop.admin.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常类
 */

public class KaptchaException extends AuthenticationException {
    public KaptchaException(String msg) {
        super(msg);
    }
}
