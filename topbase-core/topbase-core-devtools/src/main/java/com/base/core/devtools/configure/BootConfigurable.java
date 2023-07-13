package com.base.core.devtools.configure;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author start 
 */
@Configurable
@ComponentScan(basePackages = {"com.base.core.devtools.controller"})
public class BootConfigurable {

}
