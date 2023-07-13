package com.base.core.context.configure;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author start 
 */
@Configurable
@ComponentScan(basePackages = {
		"com.base.core.context.core"
		})
public class BootConfigurable {

}
