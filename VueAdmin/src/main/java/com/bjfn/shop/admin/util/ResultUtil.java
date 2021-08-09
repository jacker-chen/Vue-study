package com.bjfn.shop.admin.util;

import com.bjfn.shop.admin.common.lang.Result;
import com.bjfn.shop.admin.common.lang.ResultEnum;

/**
 * 统一返回工具类
 */

public class ResultUtil {

    public static <T> Result<T>  defineSuccess(Integer code, T data) {
        Result result = new Result<>();
        return result.setCode(code).setData(data);
    }

    public static <T> Result<T> success() {
        Result result = new Result();
        result.setMsg("操作成功");
        result.setCode(ResultEnum.SUCCESS).setData("");
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result result = new Result();
        result.setMsg("操作成功");
        result.setCode(ResultEnum.SUCCESS).setData(data);
        return result;
    }

    public static <T> Result<T> fail(String msg) {
        Result result = new Result();
        result.setCode(ResultEnum.FAIL).setMsg(msg);
        return result;
    }

    public static <T> Result<T> defineFail(int code, String msg){
        Result result = new Result();
        result.setCode(code).setMsg(msg);
        return result;
    }

    public static <T> Result<T> define(int code, String msg, T data){
        Result result = new Result();
        result.setCode(code).setMsg(msg).setData(data);
        return result;
    }
}
