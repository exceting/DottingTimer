/**
 * sharemer.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.check.config;

import dotting.timer.core.aop.DottingTracerProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sunqinwen
 * @version \: InitConfig.java,v 0.1 2018-12-05 19:14
 */
@Configuration
public class InitConfig {

    @Bean(name = "dottingTracerProxy")
    public DottingTracerProxy dottingTracerProxy() {
        return new DottingTracerProxy("127.0.0.1:8009");
    }

}
