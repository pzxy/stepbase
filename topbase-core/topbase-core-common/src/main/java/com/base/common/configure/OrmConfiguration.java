package com.base.common.configure;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gitee.magic.jdbc.persistence.source.jdbc.EntityJdbcManager;
import com.gitee.magic.jdbc.spring.annotations.EnabledOrmMapperScan;
import com.gitee.magic.jdbc.spring.context.PersistenceEntityRegistryPostProcessor;

/**
 * DB配置
 * @author start
 *
 */
@Configuration
@EnabledOrmMapperScan
@ComponentScan(basePackages = {
		"com.base.service"
		})
@EnableTransactionManagement
public class OrmConfiguration {
	
	@Bean
	@Autowired
	public EntityJdbcManager entityJdbcManager(DataSource dataSource) {
		return new EntityJdbcManager(dataSource);
	}
	
	@Bean
	@Autowired
	public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	/**
	 * 持久层实体管理类
	 * @return
	 */
	@Bean
	public static PersistenceEntityRegistryPostProcessor persistenceEntityRegistryPostProcessor() {
		return new PersistenceEntityRegistryPostProcessor();
	}
	
}
