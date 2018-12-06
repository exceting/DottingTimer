/**
 * sharemer.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import dotting.timer.server.config.properties.ConfigParam;
import dotting.timer.server.pipline.PipLineFactory;
import dotting.timer.server.receive.Receiver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sunqinwen
 * @version \: InitConfig.java,v 0.1 2018-12-05 19:14
 */
@Configuration
public class InitConfig {

    @ConfigurationProperties(prefix = "dotting.param")
    @Bean(name = "configParam")
    public ConfigParam configParam() {
        return new ConfigParam();
    }

    @ConfigurationProperties(prefix = "dotting.datasource")
    @Bean(name = "dataSource")
    public DruidDataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "bangumiSqlSession")
    public Receiver bangumiSqlSession(@Qualifier("configParam") ConfigParam configParam,
                                      @Qualifier("dataSource") DruidDataSource dataSource) throws Exception {
        if (configParam.getReceiverPort() == null) {
            throw new Exception("receiver port is null !");
        }
        if (configParam.getPiplineSize() == null || configParam.getPiplineSize() <= 0) {
            throw new Exception("receiver pipline size is error !");
        }
        return new Receiver(configParam.getReceiverPort(),
                new PipLineFactory(configParam.getPiplineSize(), dataSource));
    }

}
