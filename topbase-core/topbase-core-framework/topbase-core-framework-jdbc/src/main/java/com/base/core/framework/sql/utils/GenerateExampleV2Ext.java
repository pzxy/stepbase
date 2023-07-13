package com.base.core.framework.sql.utils;

import java.util.ArrayList;
import java.util.List;

import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.AbstractGeneratedCriteria;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.BaseExample;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.Criterion;

/**
 * @author start
 */
public class GenerateExampleV2Ext {
	
	public static <E extends AbstractGeneratedCriteria>String builderSelectSql(List<String> selects,String tableName,BaseExample<E> example,List<Object> params) {
		StringBuilder sql=new StringBuilder();
		sql.append("Select");
		if(example.isDistinct()) {
			sql.append(" distinct");
		}
		sql.append(" ");
		sql.append(StringUtils.listToString(selects));
		sql.append(" From "+tableName);
		sql.append(GenerateExampleV2Ext.generateWhereClause(example,params));
		if(example.getOrderByClause()!=null) {
			sql.append(" Order by "+example.getOrderByClause());
		}
		if(example.getLimitIndex()!=null&&
				example.getLimitSize()!=null) {
			sql.append(" Limit ?,?");
			params.add(example.getLimitIndex());
			params.add(example.getLimitSize());
		}
		return sql.toString();
	}
	
	public static <E extends AbstractGeneratedCriteria>String generateWhereClause(BaseExample<E> example,List<Object> params) {
		return generateWhereClause(example, params, true);
	}
	
	@SuppressWarnings("unchecked")
	public static <E extends AbstractGeneratedCriteria>String generateWhereClause(BaseExample<E> example,List<Object> params,Boolean deletedFlag) {
		List<String> oredCriteriaSql=new ArrayList<>();
		for(E oredCriteria:example.getOredCriteria()) {
			if(oredCriteria.isValid()) {
				List<String> criteriaSql=new ArrayList<>();
				for(Criterion criteria:oredCriteria.getCriteria()) {
					if(criteria.isNoValue()) {
						criteriaSql.add(criteria.getCondition());
					}else if(criteria.isSingleValue()) {
						criteriaSql.add(criteria.getCondition()+" ?");
						params.add(criteria.getValue());
					}else if(criteria.isBetweenValue()) {
						criteriaSql.add(criteria.getCondition()+" ? and ?");
						params.add(criteria.getValue());
						params.add(criteria.getSecondValue());
					}else if(criteria.isListValue()) {
						List<String> listItemVal=new ArrayList<>();
						for(Object item:(List<Object>)criteria.getValue()) {
							listItemVal.add("?");
							params.add(item);
						}
						criteriaSql.add(criteria.getCondition()+"("+StringUtils.listToString(listItemVal)+")");
					}
				}
				oredCriteriaSql.add(StringUtils.listToString(criteriaSql," and "));
			}
		}
		if(oredCriteriaSql.size()>0) {
			String whereSql=StringUtils.listToString(oredCriteriaSql,"or");
			if(deletedFlag) {
				params.add(0);
				return " Where ("+whereSql+") and deleted = ?";
			}else {
				return " Where "+whereSql;
			}
		}
		if(deletedFlag) {
			params.add(0);
			return " Where deleted = ?";
		}else {
			return "";
		}
	}
	
}
