/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */

package dotting.timer.ui.config;

import dotting.timer.ui.db.ConnectionPool;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author sunqinwen
 * @version \: AppConfig.java,v 0.1 2018-08-15 16:44
 */
@Configuration
@ComponentScan(basePackages = {
        "dotting.timer.ui"
})
@EnableConfigurationProperties({ServerProperties.class})
public class AppConfig {

    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        Resource[] resources = new Resource[]{
                new ClassPathResource("app.yml")
        };
        yamlPropertiesFactoryBean.setResources(resources);
        properties.setProperties(yamlPropertiesFactoryBean.getObject());
        ConnectionPool.initConnectionPool();//初始化数据源
        return properties;
    }

}
