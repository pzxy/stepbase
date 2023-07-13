package com.base.core.head.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author start 
 */
public class DataDictionaryUtils {

	
	private static Map<String, Map<String, Object>> DataDictionaryMap = new LinkedHashMap<>();
	
	/**
	 * 数据字典
	 * 请在程序启动或数据字典更新时同步设置
	 */
	public static void init(Map<String, Map<String, Object>> data) {
		DataDictionaryMap.clear();
		DataDictionaryMap.putAll(data);
	}
	
	public static Map<String, Map<String, Object>> getDataDictionaryMap() {
		return DataDictionaryMap;
	}
	
	/**
	 * 获取字典数据
	 * @param code
	 * @return
	 */
	public static Map<String,String> getDictoinary(String code){
		return getAllDataDictionary().get(code);
	}
	
	public static Map<String, Map<String, String>> getAllDataDictionary() {
		Map<String, Map<String, String>> allDict=new LinkedHashMap<>(100);
		for(String code:getDataDictionaryMap().keySet()) {
			Map<String,Object> mapDict=getDataDictionaryMap().get(code);
			Map<String, String> dict=new LinkedHashMap<>(100);
			for(String key:mapDict.keySet()) {
				dict.put(code+"_"+key, String.valueOf(mapDict.get(key)));
			}
			allDict.put(code, dict);
		}
		return allDict;
	}

}
