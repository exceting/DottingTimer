/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.server.config;

import dotting.timer.server.config.properties.ConfigParam;
import dotting.timer.server.config.receive.Receiver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author sunqinwen
 * @version \: InitConfig.java,v 0.1 2018-12-05 19:14 
 *
 */
@Configuration
public class InitConfig {

    @ConfigurationProperties(prefix = "dotting")
    @Bean(name = "configParam")
    public ConfigParam configParam() {
        return new ConfigParam();
    }

    @Bean(name = "bangumiSqlSession")
    public Receiver bangumiSqlSession(@Qualifier("configParam") ConfigParam configParam) throws Exception {
        if(configParam.getReceiverPort() == null){
            throw new Exception("receiver port is null !");
        }
        return new Receiver(configParam.getReceiverPort());
    }

}
