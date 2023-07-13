package com.base.core.mvc.configure;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author start 
 */
@Configurable
@ComponentScan(basePackages = {
		"com.base.core.mvc.core"
		})
public class BootConfigurable {

}
