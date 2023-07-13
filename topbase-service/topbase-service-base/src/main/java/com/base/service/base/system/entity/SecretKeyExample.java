package com.base.service.base.system.entity;

import java.util.List;

import com.base.service.base.system.entity.SecretKeyExample.Criteria;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.AbstractGeneratedCriteria;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.BaseExample;

/**
 * 
 * 警告:以下为自动生成的模版文件请误进行修改
 * @author start
 */
public class SecretKeyExample extends BaseExample<Criteria> {

	@Override
	protected Criteria createCriteriaInternal() {
		return new Criteria();
	}

	public static class Criteria extends AbstractGeneratedCriteria {

		public Criteria andAccessIdIsNull() {
			addCriterion("accessId is null");
			return (Criteria) this;
		}
		
		public Criteria andAccessIdIsNotNull() {
			addCriterion("accessId is not null");
			return (Criteria) this;
		}
		
		public Criteria andAccessIdEqualTo(String value) {
			addCriterion("accessId =", value, "accessId");
			return (Criteria) this;
		}
		
		public Criteria andAccessIdNotEqualTo(String value) {
			addCriterion("accessId <>", value, "accessId");
			return (Criteria) this;
		}
		
		public Criteria andAccessIdGreaterThan(String value) {
			addCriterion("accessId >", value, "accessId");
			return (Criteria) this;
		}
		
		public Criteria andAccessIdGreaterThanOrEqualTo(String value) {
			addCriterion("accessId >=", value, "accessId");
			return (Criteria) this;
		}
		
		public Criteria andAccessIdLessThan(String value) {
			addCriterion("accessId <", value, "accessId");
			return (Criteria) this;
		}
		
		public Criteria andAccessIdLessThanOrEqualTo(String value) {
			addCriterion("accessId <=", value, "accessId");
			return (Criteria) this;
		}
		
		public Criteria andAccessIdLike(String value) {
			addCriterion("accessId like", value, "accessId");
			return (Criteria) this;
		}
		
		public Criteria andAccessIdNotLike(String value) {
			addCriterion("accessId not like", value, "accessId");
			return (Criteria) this;
		}
		
		public Criteria andAccessIdIn(List<String> values) {
			addCriterion("accessId in", values, "accessId");
			return (Criteria) this;
		}
		
		public Criteria andAccessIdNotIn(List<String> values) {
			addCriterion("accessId not in", values, "accessId");
			return (Criteria) this;
		}
		
		public Criteria andAccessIdBetween(String value1, String value2) {
			addCriterion("accessId between", value1, value2, "accessId");
			return (Criteria) this;
		}
		
		public Criteria andAccessIdNotBetween(String value1, String value2) {
			addCriterion("accessId not between", value1, value2, "accessId");
			return (Criteria) this;
		}
		public Criteria andAccessKeyIsNull() {
			addCriterion("accessKey is null");
			return (Criteria) this;
		}
		
		public Criteria andAccessKeyIsNotNull() {
			addCriterion("accessKey is not null");
			return (Criteria) this;
		}
		
		public Criteria andAccessKeyEqualTo(String value) {
			addCriterion("accessKey =", value, "accessKey");
			return (Criteria) this;
		}
		
		public Criteria andAccessKeyNotEqualTo(String value) {
			addCriterion("accessKey <>", value, "accessKey");
			return (Criteria) this;
		}
		
		public Criteria andAccessKeyGreaterThan(String value) {
			addCriterion("accessKey >", value, "accessKey");
			return (Criteria) this;
		}
		
		public Criteria andAccessKeyGreaterThanOrEqualTo(String value) {
			addCriterion("accessKey >=", value, "accessKey");
			return (Criteria) this;
		}
		
		public Criteria andAccessKeyLessThan(String value) {
			addCriterion("accessKey <", value, "accessKey");
			return (Criteria) this;
		}
		
		public Criteria andAccessKeyLessThanOrEqualTo(String value) {
			addCriterion("accessKey <=", value, "accessKey");
			return (Criteria) this;
		}
		
		public Criteria andAccessKeyLike(String value) {
			addCriterion("accessKey like", value, "accessKey");
			return (Criteria) this;
		}
		
		public Criteria andAccessKeyNotLike(String value) {
			addCriterion("accessKey not like", value, "accessKey");
			return (Criteria) this;
		}
		
		public Criteria andAccessKeyIn(List<String> values) {
			addCriterion("accessKey in", values, "accessKey");
			return (Criteria) this;
		}
		
		public Criteria andAccessKeyNotIn(List<String> values) {
			addCriterion("accessKey not in", values, "accessKey");
			return (Criteria) this;
		}
		
		public Criteria andAccessKeyBetween(String value1, String value2) {
			addCriterion("accessKey between", value1, value2, "accessKey");
			return (Criteria) this;
		}
		
		public Criteria andAccessKeyNotBetween(String value1, String value2) {
			addCriterion("accessKey not between", value1, value2, "accessKey");
			return (Criteria) this;
		}
		public Criteria andTypeIsNull() {
			addCriterion("type is null");
			return (Criteria) this;
		}
		
		public Criteria andTypeIsNotNull() {
			addCriterion("type is not null");
			return (Criteria) this;
		}
		
		public Criteria andTypeEqualTo(Integer value) {
			addCriterion("type =", value, "type");
			return (Criteria) this;
		}
		
		public Criteria andTypeNotEqualTo(Integer value) {
			addCriterion("type <>", value, "type");
			return (Criteria) this;
		}
		
		public Criteria andTypeGreaterThan(Integer value) {
			addCriterion("type >", value, "type");
			return (Criteria) this;
		}
		
		public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
			addCriterion("type >=", value, "type");
			return (Criteria) this;
		}
		
		public Criteria andTypeLessThan(Integer value) {
			addCriterion("type <", value, "type");
			return (Criteria) this;
		}
		
		public Criteria andTypeLessThanOrEqualTo(Integer value) {
			addCriterion("type <=", value, "type");
			return (Criteria) this;
		}
		
		public Criteria andTypeIn(List<Integer> values) {
			addCriterion("type in", values, "type");
			return (Criteria) this;
		}
		
		public Criteria andTypeNotIn(List<Integer> values) {
			addCriterion("type not in", values, "type");
			return (Criteria) this;
		}
		
		public Criteria andTypeBetween(Integer value1, Integer value2) {
			addCriterion("type between", value1, value2, "type");
			return (Criteria) this;
		}
		
		public Criteria andTypeNotBetween(Integer value1, Integer value2) {
			addCriterion("type not between", value1, value2, "type");
			return (Criteria) this;
		}
		public Criteria andInvalidTimeIsNull() {
			addCriterion("invalidTime is null");
			return (Criteria) this;
		}
		
		public Criteria andInvalidTimeIsNotNull() {
			addCriterion("invalidTime is not null");
			return (Criteria) this;
		}
		
		public Criteria andInvalidTimeEqualTo(java.util.Date value) {
			addCriterion("invalidTime =", value, "invalidTime");
			return (Criteria) this;
		}
		
		public Criteria andInvalidTimeNotEqualTo(java.util.Date value) {
			addCriterion("invalidTime <>", value, "invalidTime");
			return (Criteria) this;
		}
		
		public Criteria andInvalidTimeGreaterThan(java.util.Date value) {
			addCriterion("invalidTime >", value, "invalidTime");
			return (Criteria) this;
		}
		
		public Criteria andInvalidTimeGreaterThanOrEqualTo(java.util.Date value) {
			addCriterion("invalidTime >=", value, "invalidTime");
			return (Criteria) this;
		}
		
		public Criteria andInvalidTimeLessThan(java.util.Date value) {
			addCriterion("invalidTime <", value, "invalidTime");
			return (Criteria) this;
		}
		
		public Criteria andInvalidTimeLessThanOrEqualTo(java.util.Date value) {
			addCriterion("invalidTime <=", value, "invalidTime");
			return (Criteria) this;
		}
		
		public Criteria andInvalidTimeIn(List<java.util.Date> values) {
			addCriterion("invalidTime in", values, "invalidTime");
			return (Criteria) this;
		}
		
		public Criteria andInvalidTimeNotIn(List<java.util.Date> values) {
			addCriterion("invalidTime not in", values, "invalidTime");
			return (Criteria) this;
		}
		
		public Criteria andInvalidTimeBetween(java.util.Date value1, java.util.Date value2) {
			addCriterion("invalidTime between", value1, value2, "invalidTime");
			return (Criteria) this;
		}
		
		public Criteria andInvalidTimeNotBetween(java.util.Date value1, java.util.Date value2) {
			addCriterion("invalidTime not between", value1, value2, "invalidTime");
			return (Criteria) this;
		}
		public Criteria andRemarkIsNull() {
			addCriterion("remark is null");
			return (Criteria) this;
		}
		
		public Criteria andRemarkIsNotNull() {
			addCriterion("remark is not null");
			return (Criteria) this;
		}
		
		public Criteria andRemarkEqualTo(String value) {
			addCriterion("remark =", value, "remark");
			return (Criteria) this;
		}
		
		public Criteria andRemarkNotEqualTo(String value) {
			addCriterion("remark <>", value, "remark");
			return (Criteria) this;
		}
		
		public Criteria andRemarkGreaterThan(String value) {
			addCriterion("remark >", value, "remark");
			return (Criteria) this;
		}
		
		public Criteria andRemarkGreaterThanOrEqualTo(String value) {
			addCriterion("remark >=", value, "remark");
			return (Criteria) this;
		}
		
		public Criteria andRemarkLessThan(String value) {
			addCriterion("remark <", value, "remark");
			return (Criteria) this;
		}
		
		public Criteria andRemarkLessThanOrEqualTo(String value) {
			addCriterion("remark <=", value, "remark");
			return (Criteria) this;
		}
		
		public Criteria andRemarkLike(String value) {
			addCriterion("remark like", value, "remark");
			return (Criteria) this;
		}
		
		public Criteria andRemarkNotLike(String value) {
			addCriterion("remark not like", value, "remark");
			return (Criteria) this;
		}
		
		public Criteria andRemarkIn(List<String> values) {
			addCriterion("remark in", values, "remark");
			return (Criteria) this;
		}
		
		public Criteria andRemarkNotIn(List<String> values) {
			addCriterion("remark not in", values, "remark");
			return (Criteria) this;
		}
		
		public Criteria andRemarkBetween(String value1, String value2) {
			addCriterion("remark between", value1, value2, "remark");
			return (Criteria) this;
		}
		
		public Criteria andRemarkNotBetween(String value1, String value2) {
			addCriterion("remark not between", value1, value2, "remark");
			return (Criteria) this;
		}
		public Criteria andIdIsNull() {
			addCriterion("id is null");
			return (Criteria) this;
		}
		
		public Criteria andIdIsNotNull() {
			addCriterion("id is not null");
			return (Criteria) this;
		}
		
		public Criteria andIdEqualTo(Long value) {
			addCriterion("id =", value, "id");
			return (Criteria) this;
		}
		
		public Criteria andIdNotEqualTo(Long value) {
			addCriterion("id <>", value, "id");
			return (Criteria) this;
		}
		
		public Criteria andIdGreaterThan(Long value) {
			addCriterion("id >", value, "id");
			return (Criteria) this;
		}
		
		public Criteria andIdGreaterThanOrEqualTo(Long value) {
			addCriterion("id >=", value, "id");
			return (Criteria) this;
		}
		
		public Criteria andIdLessThan(Long value) {
			addCriterion("id <", value, "id");
			return (Criteria) this;
		}
		
		public Criteria andIdLessThanOrEqualTo(Long value) {
			addCriterion("id <=", value, "id");
			return (Criteria) this;
		}
		
		public Criteria andIdIn(List<Long> values) {
			addCriterion("id in", values, "id");
			return (Criteria) this;
		}
		
		public Criteria andIdNotIn(List<Long> values) {
			addCriterion("id not in", values, "id");
			return (Criteria) this;
		}
		
		public Criteria andIdBetween(Long value1, Long value2) {
			addCriterion("id between", value1, value2, "id");
			return (Criteria) this;
		}
		
		public Criteria andIdNotBetween(Long value1, Long value2) {
			addCriterion("id not between", value1, value2, "id");
			return (Criteria) this;
		}

	}

}