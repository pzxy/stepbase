package com.common.core;

import org.junit.Test;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;

/**
 * MySQL 代码生成
 *
 * @author lanjerry
 * @since 3.5.3
 */
public class MySQLGeneratorTest extends BaseGeneratorTest {

	/**
	 * 数据源配置
	 */
	private static final DataSourceConfig DATA_SOURCE_CONFIG = new DataSourceConfig.Builder(
			"jdbc:mysql://39.100.66.128:3306/edu_laboratory_dev?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai", "root", "Mysql_native_password123").schema("edu_laboratory_dev")
			.build();

	@Test
	public void testSimple() {
		AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
		generator.strategy(strategyConfig().build());
		generator.global(globalConfig().build());
		generator.execute();
	}
}
