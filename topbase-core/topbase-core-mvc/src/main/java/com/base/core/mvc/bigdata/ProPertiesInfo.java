package com.base.core.mvc.bigdata;

import com.gitee.magic.core.json.JsonObject;
import lombok.Data;

/**
 *  @description : 固定信息
 *  @author : Scott
 *  @date :2022/10/19
 */
@Data
public class ProPertiesInfo{

    /**
     *  分配后端是4
     */
    private String source = "4";
    /**
     * dn：本地 pn：生产
     */
    private String evn;

    /**
     * 请求数据
     */
    private JsonObject data;

    /**
     * 设备地址
     */
    private String account;
    /**
     * 设备唯一id
     */
    private String visitor_id;

    /**
     * url
     */
    private String href;



}