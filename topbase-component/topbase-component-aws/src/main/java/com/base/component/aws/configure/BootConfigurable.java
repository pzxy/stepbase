package com.base.component.aws.configure;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author start 
 */
@Configurable
@ComponentScan(basePackages = {"com.base.component.aws.s3"})
public class BootConfigurable {

}
