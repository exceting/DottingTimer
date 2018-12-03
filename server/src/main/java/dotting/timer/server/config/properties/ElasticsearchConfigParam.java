package dotting.timer.server.config.properties;

/**
 * Create by 18073 on 2018/12/3.
 */
public class ElasticsearchConfigParam {

    /**
     * #es相关配置
     * es.cluster.name=shion
     * es.host01=127.0.0.1
     * es.host01.port=9300
     * es.host02=127.0.0.1
     * es.host02.port=9301
     * es.host03=127.0.0.1
     * es.host03.port=9302
     * <p>
     * es.index=media_index
     */

    private String clusterName;

    private String hosts;

    private String index;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
