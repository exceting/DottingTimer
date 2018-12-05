package dotting.timer.server.config.properties;

/**
 * Create by 18073 on 2018/12/3.
 */
public class ConfigParam {

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

    private String esClusterName;

    private String esHosts;

    private String esIndex;

    private Integer receiverPort;

    public Integer getReceiverPort() {
        return receiverPort;
    }

    public void setReceiverPort(Integer receiverPort) {
        this.receiverPort = receiverPort;
    }

    public String getEsClusterName() {
        return esClusterName;
    }

    public void setEsClusterName(String esClusterName) {
        this.esClusterName = esClusterName;
    }

    public String getEsHosts() {
        return esHosts;
    }

    public void setEsHosts(String esHosts) {
        this.esHosts = esHosts;
    }

    public String getEsIndex() {
        return esIndex;
    }

    public void setEsIndex(String esIndex) {
        this.esIndex = esIndex;
    }
}
