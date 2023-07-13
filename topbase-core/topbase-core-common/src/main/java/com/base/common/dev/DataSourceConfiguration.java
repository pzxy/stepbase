package com.base.common.dev;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.gitee.magic.core.exception.ApplicationException;

/**
 * 数据源配置,配置start.framework.print.sqltablescript=true生成数据表可打开以下代码进行数据表生成
 * @author start
 * 使用shardingsphere会导致无法自动创建数据表开发环境下可加入该类
 *<dependency>
 *	<groupId>org.apache.shardingsphere</groupId>
 *	<artifactId>sharding-jdbc-spring-boot-starter</artifactId>
 *	<version>4.1.0</version>
 *</dependency>	
 */
@Configuration
@Profile({"dev","test"})
public class DataSourceConfiguration {
	
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.shardingsphere.datasource.master")
	public DataSource datasource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		// 最小连接池数量
		dataSource.setMinIdle(200);
		// 最大连接池数量
		dataSource.setMaxActive(200);
		// 获取连接时最大等待时间
		dataSource.setMaxWait(3000);
		dataSource.setTimeBetweenEvictionRunsMillis(60000);
		try {
			dataSource.setFilters("stat,wall");
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
		return dataSource;
	}

	@Bean
	public ServletRegistrationBean<StatViewServlet> druidServlet() {
		ServletRegistrationBean<StatViewServlet> servletRegistrationBean = 
				new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
		FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.css,/druid/*");
		return filterRegistrationBean;
	}
	
}
