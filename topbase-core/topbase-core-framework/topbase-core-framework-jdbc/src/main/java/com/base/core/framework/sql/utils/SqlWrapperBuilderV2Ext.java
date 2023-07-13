package com.base.core.framework.sql.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gitee.magic.core.annotations.SqlCondition.SqlConditionType;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.framework.base.utils.MapConvert;
import com.gitee.magic.framework.base.utils.ParamValue;
import com.gitee.magic.jdbc.persistence.EntityObject;
import com.gitee.magic.jdbc.persistence.EntityProperty;
import com.gitee.magic.jdbc.persistence.source.jdbc.mapper.SqlTemplate;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.SqlWrapperBuilder;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.query.QueryWrapper;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.update.DeleteWrapper;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.update.UpdateWrapper;

/**
 * @author start
 */
public class SqlWrapperBuilderV2Ext extends SqlWrapperBuilder {

	public static final String DELETEDFIELD = "deleted";

	public static <R> QueryWrapper buildQueryWrapper(EntityObject eObject, Boolean idhandle, Class<R> rpro,
			Map<String, ParamValue> params) {
		Map<String, String> pmapping = MapConvert.mapping(rpro);
		Map<String, String> emapping = new HashMap<>(100);
		for (EntityProperty member : eObject.getMembers()) {
			emapping.put(member.getFieldName(), member.getTableFieldName());
		}
		emapping.put("id", eObject.getEntityPropertyStubPrimaryKey().getTableFieldName());
		List<String> listSelect = new ArrayList<>();
		if (idhandle) {
			// 争对ID的特殊处理
			String idStr = "id";
			if (!pmapping.containsValue(idStr)) {
				String pid = eObject.getName() + "Id";
				if (pmapping.containsValue(pid) && !emapping.containsValue(pid)) {
					listSelect.add(eObject.getEntityPropertyStubPrimaryKey().getTableFieldName() + " as " + pid);
				}
			}
		}
		for (String e : pmapping.keySet()) {
			if (emapping.containsKey(e)) {
				listSelect.add(emapping.get(e) + " as " + pmapping.get(e));
			}
		}
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.select(listSelect.toArray(new String[listSelect.size()]));
		for (String p : params.keySet()) {
			if (emapping.containsValue(p)) {
				ParamValue paramValue = params.get(p);
				for(SqlConditionType condition:paramValue.getCondition()) {
					if (condition == SqlConditionType.IN) {
						wrapper.in(!StringUtils.isEmpty(paramValue.getValue()), p, paramValue.getValue());
					} else if (condition == SqlConditionType.NOT) {
						wrapper.notIn(!StringUtils.isEmpty(paramValue.getValue()), p, paramValue.getValue());
					} else if (condition == SqlConditionType.LIKE) {
						wrapper.like(!StringUtils.isEmpty(paramValue.getValue()), p, paramValue.getValue());
					} else if (condition == SqlConditionType.LIKE_LEFT) {
						wrapper.likeLeft(!StringUtils.isEmpty(paramValue.getValue()), p, paramValue.getValue());
					} else if (condition == SqlConditionType.LIKE_RIGHT) {
						wrapper.likeRight(!StringUtils.isEmpty(paramValue.getValue()), p, paramValue.getValue());
					} else if (condition == SqlConditionType.EQ) {
						wrapper.eq(!StringUtils.isEmpty(paramValue.getValue()), p, paramValue.getValue());
					} else if (condition == SqlConditionType.NE) {
						wrapper.ne(!StringUtils.isEmpty(paramValue.getValue()), p, paramValue.getValue());
					} else if (condition == SqlConditionType.GT) {
						wrapper.gt(!StringUtils.isEmpty(paramValue.getValue()), p, paramValue.getValue());
					} else if (condition == SqlConditionType.GE) {
						wrapper.ge(!StringUtils.isEmpty(paramValue.getValue()), p, paramValue.getValue());
					} else if (condition == SqlConditionType.LT) {
						wrapper.lt(!StringUtils.isEmpty(paramValue.getValue()), p, paramValue.getValue());
					} else if (condition == SqlConditionType.LE) {
						wrapper.le(!StringUtils.isEmpty(paramValue.getValue()), p, paramValue.getValue());
					} else if (condition == SqlConditionType.IS_NULL) {
						wrapper.isNull(p);
					} else if (condition == SqlConditionType.IS_NOT_NULL) {
						wrapper.isNotNull(p);
					} else if (condition == SqlConditionType.GROUP_BY) {
						wrapper.groupBy(p);
					} else if (condition == SqlConditionType.ASC) {
						wrapper.orderByAsc(p);
					} else if (condition == SqlConditionType.DESC) {
						wrapper.orderByDesc(p);
					}
				}
			}
		}
		return wrapper;
	}

	public static String getSql(String tableName, QueryWrapper wrapper) {
		String columns = wrapper.getSqlSelect();
		if (StringUtils.isEmpty(columns)) {
			columns = "*";
		}
		StringBuilder sbSql = new StringBuilder(String.format(SELECT, columns, tableName));
		StringBuilder whereSql = new StringBuilder();
		if (wrapper.nonEmptyOfWhere()) {
			String sql = wrapper.getSqlNormal();
			whereSql.append("(" + SqlTemplate.getExecuteSql(sql) + ") and ");
		}
		whereSql.append(DELETEDFIELD + " = ?");
		sbSql.append(String.format(WHERE, whereSql.toString()));
		sbSql.append(wrapper.getSqlNormalAfter());
		return sbSql.toString();
	}

	public static Object[] getParams(QueryWrapper wrapper) {
		Map<String, Object> params = wrapper.getParamNameValuePairs();
		Object[] args = SqlTemplate.getExecuteParameter(wrapper.getSqlSegment(), params);
		Object[] p = new Object[args.length + 1];
		System.arraycopy(args, 0, p, 0, args.length);
		p[args.length] = 0;
		return p;
	}

	public static String getSql(String tableName, UpdateWrapper wrapper) {
		String sqlSet = wrapper.getSqlSet();
		StringBuilder sbSql = new StringBuilder(String.format(UPDATE, tableName, SqlTemplate.getExecuteSql(sqlSet)));
		StringBuilder whereSql = new StringBuilder();
		if (wrapper.nonEmptyOfWhere()) {
			String sql = wrapper.getSqlNormal();
			whereSql.append("(" + SqlTemplate.getExecuteSql(sql) + ") and ");
		}
		whereSql.append(DELETEDFIELD + " = ?");
		sbSql.append(String.format(WHERE, whereSql.toString()));
		sbSql.append(wrapper.getSqlNormalAfter());
		return sbSql.toString();
	}

	public static Object[] getParams(UpdateWrapper wrapper) {
		Map<String, Object> params = wrapper.getParamNameValuePairs();
		StringBuilder sbSql = new StringBuilder(wrapper.getSqlSet());
		if (!StringUtils.isEmpty(wrapper.getSqlSegment())) {
			sbSql.append(wrapper.getSqlSegment());
		}
		Object[] args = SqlTemplate.getExecuteParameter(sbSql.toString(), params);
		Object[] p = new Object[args.length + 1];
		System.arraycopy(args, 0, p, 0, args.length);
		p[args.length] = 0;
		return p;
	}

	public static String getSql(String tableName, DeleteWrapper wrapper) {
		StringBuilder sbSql = new StringBuilder(String.format(UPDATE, tableName, DELETEDFIELD + " = ?"));
		StringBuilder whereSql = new StringBuilder();
		if (wrapper.nonEmptyOfWhere()) {
			String sql = wrapper.getSqlNormal();
			whereSql.append("(" + SqlTemplate.getExecuteSql(sql) + ") and ");
		}
		whereSql.append(DELETEDFIELD + " = ?");
		sbSql.append(String.format(WHERE, whereSql.toString()));
		sbSql.append(wrapper.getSqlNormalAfter());
		return sbSql.toString();
	}

	public static Object[] getParams(Long deleted, DeleteWrapper wrapper) {
		Map<String, Object> params = wrapper.getParamNameValuePairs();
		Object[] args = SqlTemplate.getExecuteParameter(wrapper.getSqlSegment(), params);
		Object[] p = new Object[args.length + 2];
		p[0] = deleted;
		System.arraycopy(args, 0, p, 1, args.length);
		p[args.length + 1] = 0;
		return p;
	}

	public static void main(String[] args) {
		DeleteWrapper wrapper = new DeleteWrapper();
		wrapper.eq("taskId", 123).eq("age", 13);

		System.out.println(getSql("sys_user", wrapper));
		Long deletedN = 1234L;
		for (Object o : getParams(deletedN, wrapper)) {
			System.out.println(o.toString());
		}
	}

}
