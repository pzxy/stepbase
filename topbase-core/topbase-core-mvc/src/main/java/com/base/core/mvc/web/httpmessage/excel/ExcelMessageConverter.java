package com.base.core.mvc.web.httpmessage.excel;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.base.core.head.annotations.ExportExcel;
import com.base.core.head.annotations.ExportExcel.DefaultConverter;
import com.base.core.mvc.web.httpmessage.BaseHttpMessageConverter;
import com.gitee.magic.context.ConverterEditorUtils;
import com.gitee.magic.core.annotations.Column;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.json.JsonArray;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.core.utils.ReflectUtils;
import com.gitee.magic.framework.head.utils.CodecUtils;
import com.gitee.magic.framework.head.vo.ListVO;
import com.gitee.magic.framework.head.vo.PageVO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Start
 */
public class ExcelMessageConverter extends BaseHttpMessageConverter<Object> {

    @Autowired
    private HttpServletResponse response;
	
    public ExcelMessageConverter() {
        super(new MediaType("application", "vnd.ms-excel", UTF8));
    }
    
    @Override
    protected boolean supports(Class<?> clazz) {
    	return List.class.isAssignableFrom(clazz)|| 
    			ListVO.class.isAssignableFrom(clazz)|| 
    			PageVO.class.isAssignableFrom(clazz);
    }
    
    @Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws HttpMessageNotReadableException {
		return null;
	}

    @Override
	protected void writeInternal(Object t, Type type, HttpOutputMessage outputMessage)
			throws HttpMessageNotWritableException {
    	if(t instanceof ListVO) {
    		if(type instanceof ParameterizedType) {
    			Type gType=((ParameterizedType) type).getActualTypeArguments()[0];
    			Class<?> prototype=((Class<?>)gType);
    			
    			Map<Integer,ExportExcelHolder> exportMap=new TreeMap<>();
    			Class<?> curPro=prototype;
    			while(!curPro.equals(Object.class)) {
    				Field[] fields=curPro.getDeclaredFields();
        			for(Field field:fields) {
        				if(field.isAnnotationPresent(ExportExcel.class)) {
        					ExportExcel ee=field.getAnnotation(ExportExcel.class);
        					String headName=field.getName();
        					if(field.isAnnotationPresent(Schema.class)) {
        						Schema amp=field.getAnnotation(Schema.class);
            					headName=amp.title();
            				}
        					int index=ee.index();
        					while(exportMap.containsKey(index)) {
        						index++;
        					}
        					String fieldName=field.getName();
        					if(field.isAnnotationPresent(Column.class)) {
        						Column column=field.getAnnotation(Column.class);
        						fieldName=column.value();
        					}
        					ExportExcelHolder holder=new ExportExcelHolder();
        					holder.setHeadName(headName);
        					holder.setFieldName(fieldName);
        					holder.setAnnCls(ee);
        					exportMap.put(index, holder);
        				}
        			}
    				curPro=curPro.getSuperclass();
    			}
    			Map<Integer,ExportExcelHolder> holderMap=new TreeMap<>();
    			int j=0;
    			for(ExportExcelHolder h:exportMap.values()) {
    				holderMap.put(j, h);
    				j++;
    			}
    			List<String> head=new ArrayList<>();
    			for(ExportExcelHolder h:holderMap.values()) {
    				head.add(h.getHeadName());
    			}
    			List<List<String>> datas=new ArrayList<>();
    			datas.add(head);
    			
    			JsonArray array=ConverterEditorUtils.converter(((ListVO<?>) t).getData());
    			for(int i=0;i<array.length();i++) {
    				JsonObject json=array.getJsonObject(i);
    				List<String> data=new ArrayList<>();
    				for(Integer index:holderMap.keySet()) {
    					ExportExcelHolder holder=holderMap.get(index);
    					if(json.has(holder.getFieldName())&&
    							!json.isNull(holder.getFieldName())) {
//    						DefaultConverter converter=null;
//    						try {
//								converter=(DefaultConverter) holder.getAnnCls().converter().newInstance();
//							} catch (InstantiationException e) {
//								throw new ApplicationException(e);
//							} catch (IllegalAccessException e) {
//								throw new ApplicationException(e);
//							}
    						DefaultConverter converter=(DefaultConverter)ReflectUtils.newInstance(holder.getAnnCls().converter());
    						
    						data.add(String.valueOf(converter.converter(json.get(holder.getFieldName()))));
    					}else {
    						data.add(null);
    					}
    				}
        			datas.add(data);
    			}
    			
        		String excelName=prototype.getSimpleName();
        		if(prototype.isAnnotationPresent(Schema.class)) {
        			Schema am=prototype.getAnnotation(Schema.class);
        			excelName=am.title();
        		}
        		
        		response.setHeader("Content-disposition", "attachment;filename=" + CodecUtils.encode(excelName+".xlsx", "UTF-8"));
                ExcelWriterBuilder excelWriterBuilder;
        		try {
        			excelWriterBuilder = EasyExcel.write(outputMessage.getBody());
        		} catch (IOException e) {
        			throw new ApplicationException(e);
        		}
        		ExcelWriter excelWriter=excelWriterBuilder.registerWriteHandler(new DefaultMergeStrategy(holderMap)).build();
        		WriteSheet writeSheet = EasyExcel.writerSheet().build();
        		writeSheet.setSheetName(excelName);
        		excelWriter.write(datas, writeSheet);
        		excelWriter.finish();
    		}
    	}
    }

}
