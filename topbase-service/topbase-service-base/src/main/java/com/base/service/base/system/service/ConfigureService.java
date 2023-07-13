package com.base.service.base.system.service;

import java.util.List;
import java.util.Map;

import com.base.core.framework.sql.service.SqlBaseService;
import com.base.service.base.system.entity.ConfigureDO;

/**
 * @author start 
 */
public interface ConfigureService extends SqlBaseService<ConfigureDO, Long> {

    /**
     * 初始化配置，存缓存重新配置后需要重新调用
     * @param prototype
     * @param groupId
     * @param <R>
     * @return
     */
    <R> R initConfig(Class<R> prototype,Integer groupId);

    /**
     * 初始化调用,初始化或重置数据字典时调用
     * @param groupId
     * @return
     */
    Map<String,Map<String,Object>> initDataDictionary(Integer groupId);

    /**
     * 根据code获取
     * @param code
     * @return
     */
    ConfigureDO loadByCode(String code);

    /**
     * 根据GroupId获取配置列表
     * @param groupId
     * @return
     */
    List<ConfigureDO> queryByGroupId(Integer groupId);

    /**
     * 获取配置对象
     * @param code
     * @return
     */
    Object getValue(String code);

    /**
     * 获取字符配置项
     * @param code
     * @return
     */
    String getString(String code);

    /**
     * 获取配置对象
     * @param code
     * @param prototype
     * @param <R>
     * @return
     */
    <R>R getObject(String code,Class<R> prototype);

    /**
     * 获取配置数组
     * @param code
     * @param prototype
     * @param <R>
     * @return
     */
    <R>List<R> getArray(String code,Class<R> prototype);

    /**
     * 设置配置对象
     * @param code
     * @param param
     * @param <E>
     * @return
     */
    <E>int setObject(String code,E param);

    /**
     * 设置配置数组
     * @param code
     * @param param
     * @param <E>
     * @return
     */
    <E>int setArray(String code, List<E> param);

    /**
     * 设置配置值
     * @param code
     * @param value
     * @return
     */
    int setValue(String code,Object value);
    
}
