package com.base.core.mvc.bigdata;

import com.gitee.magic.core.json.JsonObject;
import lombok.Data;

/**
 *  @description : POST请求需要服务
 *  @author : Scott
 *  @date :2022/10/19
 */
@Data
public class PostToMsg{
    /**
     * ip地址
     */
    private String ip;
    /**
     * 上报来源
     */
    private String ostype = "Linux";
    /**
     * 上报时间
     */
    private Long actiontime;
    /**
     * 类别
     */
    private String category = "interface";
    /**
     * 系统名称
     */
    private String appname;
    /**
     * 事件
     */
    private String event;

    /**
     * 详细信息
     */
    private JsonObject properties;



}
