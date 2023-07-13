package com.base.service.base.system.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.base.core.framework.sql.service.impl.SqlBaseServiceImpl;
import com.base.core.framework.sql.utils.SqlWrapperBuilderV2Ext;
import com.base.core.head.constants.CodeResVal;
import com.base.core.head.enums.ValueTypeEnum;
import com.base.service.base.system.dao.ConfigureDao;
import com.base.service.base.system.entity.ConfigureDO;
import com.base.service.base.system.service.ConfigureService;
import com.gitee.magic.context.ConverterEditorUtils;
import com.gitee.magic.core.json.JsonArray;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.framework.base.utils.ParamValue;
import com.gitee.magic.framework.head.exception.BusinessException;
import com.gitee.magic.framework.head.vo.QueryVO;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.query.QueryWrapper;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.update.UpdateWrapper;

/**
 * @author start 
 */
@Service("configureService")
public class ConfigureServiceImpl extends SqlBaseServiceImpl<ConfigureDO, Long> implements ConfigureService {

	private ConfigureDao configureDao;

	public ConfigureServiceImpl(@Qualifier("configureDao") ConfigureDao configureDao) {
		super(configureDao);
		this.configureDao = configureDao;
	}

	@Override
	public <R> R initConfig(Class<R> prototype,Integer groupId) {
		List<ConfigureDO> configs = queryByGroupId(groupId);
		Map<String, Object> data = new HashMap<>(100);
		for (ConfigureDO config : configs) {
			if (config.getType() == ValueTypeEnum.JSONObject) {
				data.put(config.getCode(), new JsonObject(config.getValue()));
			} else if (config.getType() == ValueTypeEnum.JSONArray) {
				data.put(config.getCode(), new JsonArray(config.getValue()));
			} else {
				data.put(config.getCode(), config.getValue());
			}
		}
		return ConverterEditorUtils.restoreObject(prototype,new JsonObject(data));
	}

	@Override
	public Map<String,Map<String,Object>> initDataDictionary(Integer groupId) {
		List<ConfigureDO> configs=queryByGroupId(groupId);
		Map<String,Map<String,Object>> dataDictionary=new LinkedHashMap<>(100);
		for(ConfigureDO conf:configs) {
			if(conf.getType()==ValueTypeEnum.JSONObject) {
				JsonObject json=new JsonObject(conf.getValue());
				dataDictionary.put(conf.getCode(),json.toMap());
			}else if(conf.getType()==ValueTypeEnum.JSONArray) {
				JsonArray json=new JsonArray(conf.getValue());
				Map<String,Object> map=new LinkedHashMap<>(100);
				for(int i=0;i<json.length();i++) {
					String val=json.getString(i);
					map.put(String.valueOf(i), val);
				}
				dataDictionary.put(conf.getCode(),map);
			}else  if(conf.getType()==ValueTypeEnum.String) {
				Map<String,Object> map=new HashMap<>(1);
				map.put("0", conf.getValue());
				dataDictionary.put(conf.getCode(),map);
			}
		}
		return dataDictionary;
	}

	@Override
	public ConfigureDO loadByCode(String code) {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("code", code).eq("disable", String.valueOf(false));
		ConfigureDO config = get(configureDao.queryForList(wrapper));
		if(config==null) {
			throw new BusinessException(CodeResVal.CODE_10003,code);
		}
		return config;
	}

	@Override
	public List<ConfigureDO> queryByGroupId(Integer groupId) {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("groupId", groupId).eq("disable", String.valueOf(false));
		return configureDao.queryForList(wrapper);
	}

	@Override
	public Object getValue(String code) {
		ConfigureDO config=loadByCode(code);
		if(config.getType()==ValueTypeEnum.JSONObject) {
			return new JsonObject(config.getValue());
		} else if (config.getType() == ValueTypeEnum.JSONArray) {
			return new JsonArray(config.getValue());
		}else {
			return config.getValue();
		}
	}

	@Override
	public String getString(String code) {
		ConfigureDO config=loadByCode(code);
		return config.getValue();
	}

	@Override
	public <R>R getObject(String code,Class<R> prototype) {
		ConfigureDO config=loadByCode(code);
		if(config.getType()==ValueTypeEnum.String) {
			return ConverterEditorUtils.restoreObject(prototype, config.getValue());
		}else if(config.getType()==ValueTypeEnum.JSONObject) {
			return ConverterEditorUtils.restoreObject(prototype, new JsonObject(config.getValue()));
		}else {
			throw new BusinessException(CodeResVal.CODE_10003,code);
		}
	}

	@Override
	public <R>List<R> getArray(String code,Class<R> prototype) {
		ConfigureDO config=loadByCode(code);
		if(config.getType()!=ValueTypeEnum.JSONArray) {
			throw new BusinessException(CodeResVal.CODE_10003,code);
		}
		return ConverterEditorUtils.restoreList(prototype, new JsonArray(config.getValue()));
	}
	
	///////////////////////

	@Override
	public <E> int setObject(String code, E param) {
		return setValue(code,ConverterEditorUtils.converter(param,JsonObject.class));
	}

	@Override
	public <E> int setArray(String code, List<E> param) {
		return setValue(code,ConverterEditorUtils.converter(param));
	}

	@Override
	public int setValue(String code,Object value) {
		UpdateWrapper wrapper=new UpdateWrapper();
		if(value.getClass().equals(JsonObject.class)) {
			wrapper.eq("type", String.valueOf(ValueTypeEnum.JSONObject));
		}else if(value.getClass().equals(JsonArray.class)) {
			wrapper.eq("type", String.valueOf(ValueTypeEnum.JSONArray));
		}else {
			wrapper.eq("type", String.valueOf(ValueTypeEnum.String));
		}
		wrapper.eq("code", code).eq("disable", String.valueOf(false));
		wrapper.set("value",String.valueOf(value));
		return configureDao.executeUpdate(wrapper);
	}
	
	@Override
	public <R> QueryVO<R> pageWrapper(Class<R> rpro, Map<String, ParamValue> params, int pageIndex, int pageSize) {
		QueryWrapper wrapper=SqlWrapperBuilderV2Ext.buildQueryWrapper(configureDao.getEntityObject(),true,rpro,params);
		if(wrapper.isEmptyOfWhere()) {
			wrapper.eq("1","1");
		}
		wrapper.orderByDesc("sorted");
		wrapper.last("limit " + pageIndex+","+pageSize);
		return pageWrapper(rpro, wrapper);
	}

}
