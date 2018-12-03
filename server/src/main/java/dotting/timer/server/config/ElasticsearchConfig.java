package dotting.timer.server.config;

import com.google.common.base.Strings;
import dotting.timer.server.config.properties.ElasticsearchConfigParam;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Create by 18073 on 2018/12/3.
 */
@Configuration
public class ElasticsearchConfig {

    private Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);

    @Primary
    @ConfigurationProperties(prefix = "dotting.es")
    @Bean(name = "esParam")
    public ElasticsearchConfigParam esParam() {
        return new ElasticsearchConfigParam();
    }

    @Bean(name = "esClient", destroyMethod = "close")
    public TransportClient esClient(@Qualifier("esParam") ElasticsearchConfigParam esParam) {
        try {
            Settings settings = Settings.builder()
                    .put("cluster.name", esParam.getClusterName())
                    .put("client.transport.sniff", true).build();
            if (Strings.isNullOrEmpty(esParam.getHosts())) {
                logger.error("dotting-server es init error, the hosts is empty!");
                return null;
            }
            String[] hosts = esParam.getHosts().split(",");
            if (hosts.length < 1) {
                logger.error("dotting-server es init error, the hosts size is empty!");
                return null;
            }
            PreBuiltTransportClient client = new PreBuiltTransportClient(settings);
            for (String node : hosts) {
                String[] hp = node.split(":");
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hp[0]),
                        Integer.parseInt(hp[1])));
                logger.info("dotting-server init es client, host-ip={}, host-port={}", hp[0], hp[1]);
            }
            logger.info("dotting-server es init success!!");
            return client;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.error("dotting-server es host unknown host error!", e);
        }
        return null;
    }

}
