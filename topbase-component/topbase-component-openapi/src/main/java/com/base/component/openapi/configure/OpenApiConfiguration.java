package com.base.component.openapi.configure;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import com.base.core.context.annotation.AuthenticationCheck;
import com.base.core.context.annotation.LoginAuthorityCheck;
import com.base.core.context.annotation.OpenAuthorityCheck;
import com.base.core.context.annotation.RestfulCheck;
import com.base.core.context.annotation.SecretKeyAuthorityCheck;
import com.gitee.magic.framework.base.constant.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * 
 * @author start
 * @see https://springdoc.org/v2
 *
 */
@Configuration
@ComponentScan(basePackages = { "com.base.component.openapi.customizer" })
public class OpenApiConfiguration {

	@Value("${swagger.custom.description.restful:}")
	private String restfulDescription;
	@Value("${swagger.custom.description.client:}")
	private String clientDescription;
	@Value("${swagger.custom.description.open:}")
	private String openDescription;
	@Value("${swagger.custom.description.admin:}")
	private String backstageDescription;
	@Value("${swagger.custom.description.secretKey:}")
	private String secretKeyDescription;

	@Value("${swagger.custom.check.client:1}")
	private int clientType;
	@Value("${swagger.custom.check.admin:1}")
	private int backstageType;
	@Value("${swagger.custom.check.open:2}")
	private int openType;
	@Value("${swagger.custom.check.secretKey:2}")
	private int secretKeyType;

	@Value("${swagger.contact.name:start}")
	private String contactName;
	@Value("${swagger.contact.url:/static/doc/index.html}")
	private String contactUrl;
	@Value("${swagger.contact.email:start.wei@upblocks.io}")
	private String contactEmail;

	@Bean
	public GroupedOpenApi commonGroup() {
		return groupedOpenApi("common","通用接口", null, 0, null);
	}

	@ConditionalOnProperty("swagger.custom.name.restful")
	@Bean
	public GroupedOpenApi restfulGroup(@Value("${swagger.custom.name.restful}") String name) {
		return groupedOpenApi("restful",name, restfulDescription, 0, RestfulCheck.class);
	}

	@ConditionalOnProperty("swagger.custom.name.open")
	@Bean
	public GroupedOpenApi openGroup(@Value("${swagger.custom.name.open}") String name) {
		return groupedOpenApi("open",name, openDescription, openType, OpenAuthorityCheck.class);
	}

	@ConditionalOnProperty("swagger.custom.name.secretKey")
	@Bean
	public GroupedOpenApi secretKeyGroup(@Value("${swagger.custom.name.secretKey}") String name) {
		return groupedOpenApi("secretKey",name, secretKeyDescription, secretKeyType, SecretKeyAuthorityCheck.class);
	}

	@ConditionalOnProperty("swagger.custom.name.client")
	@Bean
	public GroupedOpenApi clientGroup(@Value("${swagger.custom.name.client}") String name) {
		return groupedOpenApi("client",name, clientDescription, clientType, LoginAuthorityCheck.class);
	}

	@ConditionalOnProperty("swagger.custom.name.admin")
	@Bean
	public GroupedOpenApi backstageGroup(@Value("${swagger.custom.name.admin}") String name) {
		return groupedOpenApi("backstage",name, backstageDescription, backstageType, AuthenticationCheck.class);
	}

	public GroupedOpenApi groupedOpenApi(String groupName,String displayName, String description, Integer type,
			Class<? extends Annotation> annotationClass) {
		return GroupedOpenApi.builder().group(groupName).displayName(displayName).addOpenApiMethodFilter(method -> {
			if (annotationClass == null) {
				return !(method.isAnnotationPresent(RestfulCheck.class)
						|| method.isAnnotationPresent(OpenAuthorityCheck.class)
						|| method.isAnnotationPresent(LoginAuthorityCheck.class)
						|| method.isAnnotationPresent(AuthenticationCheck.class)
						|| method.isAnnotationPresent(SecretKeyAuthorityCheck.class));
			} else {
				return method.isAnnotationPresent(annotationClass);
			}
		}).addOperationCustomizer(new OperationCustomizer() {

			@Override
			public Operation customize(Operation operation, HandlerMethod handlerMethod) {
				if (type == 2) {
					List<Parameter> list = new ArrayList<>();
					HeaderParameter accessId = new HeaderParameter();
					accessId.setName("accessId");
					accessId.setRequired(true);
					accessId.setDescription(
							"[accessId](/static/doc/doc.html#appendix_accessid_accesskey)");
					list.add(accessId);
					HeaderParameter sign = new HeaderParameter();
					sign.setName("signature");
					sign.setRequired(true);
					sign.setDescription(
							"[HMAC-SHA1](/static/doc/doc.html#sign) [Test Tools](/static/doc/test_tools.html)");
					list.add(sign);
					operation.setParameters(list);
				}
				return operation;
			}
		}).addOpenApiCustomiser(openApi->{
			openApi.info(new Info()
					.title(Config.getSystemName())
					.version(Config.getSystemVersion())
					.description(description)
					.contact(new Contact()
							.name(contactName)
							.email(contactEmail)
							.url(contactUrl)));
		}).build();
	}

	@Bean
	public OpenAPI globalOpenApi() {
		OpenAPI openApi=new OpenAPI();
		openApi.components(new Components().addSecuritySchemes("Authorization",new SecurityScheme()
				.type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER).name("Authorization").description("登录后返回的Token")));
		openApi.addSecurityItem(new SecurityRequirement().addList("Authorization"));
		return openApi;
	}

}
