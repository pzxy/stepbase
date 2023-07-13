package com.base.core.aop.service;

import java.lang.reflect.Method;

import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.base.core.context.mvc.Constants;

/**
 * @author start
 */
public abstract class AbstractAopAspect {
    
    private ExpressionParser parser = new SpelExpressionParser();

    private StandardReflectionParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();

    /**
     * 获取Redis缓存Key
     * @param key
     * @param value
     * @param method
     * @param args
     * @return
     */
    public String getKey(String key,String value,Method method,Object[] args) {
    	return Constants.getKey(parse(value, method, args),parse(key, method, args));
    }
    
    /**
     * 解析spring EL表达式
     *
     * @param key    key
     * @param method method
     * @param args   args
     * @return parse result
     */
    public String parse(String key, Method method, Object[] args) {
        String[] params = discoverer.getParameterNames(method);
        String s="#";
        if (null == params || params.length == 0 || !key.contains(s)) {
            return key;
        }
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }
	
}
