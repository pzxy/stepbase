package com.base.component.upload.configure;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author start 
 */
@Configurable
@ComponentScan(basePackages = {
		"com.base.component.upload.controller"
})
public class BootConfigurable {

}
