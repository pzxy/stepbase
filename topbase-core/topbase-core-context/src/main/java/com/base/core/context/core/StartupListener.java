package com.base.core.context.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import com.base.core.context.utils.ProxyService;
import com.base.core.head.converter.LongConverterEditor;
import com.gitee.magic.context.PropertyConverterEditor;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.spring.SpringContext;

/**
 * @author start 
 */
@Order(1)
@Component
public class StartupListener implements ApplicationContextAware,ApplicationRunner {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    @Value("${start.framework.sendconfig:false}")
    private Boolean sendConfigMessage;
	@Autowired
    private ConfigurableEnvironment environment;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContext.setAppContext(applicationContext);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		PropertyConverterEditor.putBasicConverterType(long.class, LongConverterEditor.class);
		PropertyConverterEditor.putBasicConverterType(Long.class, LongConverterEditor.class);
		
		StringBuilder title = new StringBuilder();
    	title.append("Run>>");
    	title.append(Config.getFullName());
    	title.append("["+Config.getBalancedDataCenterId());
    	title.append(",");
    	title.append(Config.getBalancedWorkerId()+"]");
        try {
        	title.append("("+InetAddress.getLocalHost().getHostAddress()+")");
        } catch (UnknownHostException e) {
//            throw new ApplicationException(e);
        	title.append("(获取HostAddress异常,vi /etc/hosts Message:"+e.getMessage()+")");
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(title.toString());
        }
        if(sendConfigMessage) {
            MutablePropertySources propSrcs = environment.getPropertySources();        
        	Map<String, String> props = StreamSupport.stream(propSrcs.spliterator(), false)
        			.filter(ps -> ps instanceof EnumerablePropertySource)
        			.map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames())
        			.flatMap(Arrays::stream).distinct().collect(Collectors.toMap(Function.identity(), environment::getProperty));
            ProxyService.send(title.toString(),new JsonObject(props).toString());
        }else {
        	ProxyService.send(title.toString(),null);
        }
	}
    
}
