package com.base.core.context.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.base.core.head.annotations.EcSign;
import com.gitee.magic.context.ConverterEditorUtils;
import com.gitee.magic.core.converter.AbstractConverterEditor;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.utils.StringUtils;

/**
 * 签名
 * @author zywei
 *
 */
public class EcSignUtils {
	

	public static SortedMap<String, Object> getSignMap(Object instance) {
		return getSignMap(instance,0);
	}
	
	public static String getSignMessage(Object instance) {
		return getSignMessage(instance,0);
	}

	/**
	 * 获取实体对象的签名数据
	 * 	默认排序规则：按照key的字典顺序来排序（升序）。（注：这里的有序是遍历时值大小的有序，不是插入顺序的有序）（默认排序规则对应底层数据结构红黑树的中序遍历）
	 * @param instance
	 * @return
	 */
	public static SortedMap<String, Object> getSignMap(Object instance,int group) {
		SortedMap<String, Object> data = new TreeMap<>();
		Class<?> prototype = instance.getClass();
		do {
			Field[] fields = prototype.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(EcSign.class)) {
					EcSign ecSign=field.getAnnotation(EcSign.class);
					List<Integer> groups= Arrays.stream(ecSign.group()).boxed().collect(Collectors.toList());
					if(groups.contains(group)) {
						PropertyDescriptor pd = null;
						try {
							pd = new PropertyDescriptor(field.getName(), prototype);
						} catch (IntrospectionException e) {
							throw new ApplicationException(e);
						}
						try {
							Object value = pd.getReadMethod().invoke(instance);
							//当字段为null时不加入签名消息
							if(value!=null) {
								AbstractConverterEditor<?> convertObj=ConverterEditorUtils.getFieldConverterEditor(field, field.getType(), null);
								convertObj.setValue(value);
								Object targetValue=convertObj.getSource();
								data.put(field.getName(), targetValue);
							}
						} catch (IllegalAccessException e) {
							throw new ApplicationException(e);
						} catch (IllegalArgumentException e) {
							throw new ApplicationException(e);
						} catch (InvocationTargetException e) {
							throw new ApplicationException(e);
						}
					}
				}
			}
		} while (!(prototype = prototype.getSuperclass()).equals(Object.class));
		return data;
	}

	/**
	 * 生成签名字符串
	 * @param instance
	 * @return
	 */
	public static String getSignMessage(Object instance,int group) {
		SortedMap<String, Object> data=getSignMap(instance,group);
		List<String> items = new ArrayList<>();
		for (String key : data.keySet()) {
			items.add(key + ":" + data.get(key));
		}
		return StringUtils.listToString(items);
	}

}
