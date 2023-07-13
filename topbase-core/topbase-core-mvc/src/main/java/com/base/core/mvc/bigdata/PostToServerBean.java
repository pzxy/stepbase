package com.base.core.mvc.bigdata;

import lombok.Data;

/**
 *  @description : POST请求需要服务
 *  @author : Scott
 *  @date :2022/10/19
 */
@Data
public class PostToServerBean{

    private String msg;

    private String sign;

    private String topic = "tz_block";
}
