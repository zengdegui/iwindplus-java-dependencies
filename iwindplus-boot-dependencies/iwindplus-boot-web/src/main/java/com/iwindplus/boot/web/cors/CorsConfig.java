/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.web.cors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域相关配置.
 *
 * @author zengdegui
 * @since 2019/6/12
 */
@Slf4j
@Configuration
public class CorsConfig {
	/**
	 * 创建 CorsFilter.
	 *
	 * @return CorsFilter
	 */
	@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		// 允许访问的客户端域名
		corsConfiguration.addAllowedOrigin("*");
		// 允许服务端访问的客户端请求头
		corsConfiguration.addAllowedHeader("*");
		// 允许访问的方法名,GET POST等
		corsConfiguration.addAllowedMethod("*");
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		CorsFilter corsFilter = new CorsFilter(urlBasedCorsConfigurationSource);
		log.info("CorsFilter [{}]", corsFilter);
		return corsFilter;
	}
}
