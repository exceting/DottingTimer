package dotting.timer.core.utils;

/**
 * @author sunqinwen
 * @version \: SpanTags.java,v 0.1 2018-09-26 10:15
 */
public class SpanTags {

    //生成此Span所相关的软件包，框架，类库或模块。如 "grpc", "django", "JDBI"
    public static final String COMPONENT = "component";

    //数据库实例名称。以Java为例，如果 jdbc.url="jdbc:mysql://127.0.0.1:3306/customers"，实例名为 "customers"
    public static final String DB_INSTANCE = "db.instance";

    //一个针对给定数据库类型的数据库访问语句。例如， 针对数据库类型 db.type="sql"，语句可能是 "SELECT * FROM wuser_table"; 针对数据库类型为 db.type="redis"，语句可能是 "SET mykey 'WuValue'"
    public static final String DB_STATEMENT = "db.statement";

    //数据库类型。对于任何支持SQL的数据库，取值为 "sql". 否则，使用小写的数据类型名称，如 "cassandra", "hbase", or "redis"
    public static final String DB_TYPE = "db.type";

    //访问数据库的用户名。如 "readonly_user" 或 "reporting_user"
    public static final String DB_USER = "db.user";

    //设置为true，说明整个Span失败。译者注：Span内发生异常不等于error=true，这里由被监控的应用系统决定
    public static final String ERROR = "error";

    //Span相关的HTTP请求方法。例如 "GET", "POST"
    public static final String HTTP_METHOD = "http.method";

    //Span相关的HTTP返回码。例如 200, 503, 404
    public static final String HTTP_STATUS_CODE = "http.status_code";

    //Span相关的HTTP请求业务code，比如站内请求一般会返回业务code码
    public static final String HTTP_BUS_CODE = "http.bus_code";

    //被处理的trace片段锁对应的请求URL。 例如 "https://domain.net/path/to?resource=here"
    public static final String HTTP_URL = "http.url";

    //消息投递或交换的地址。例如，在Kafka中，在生产者或消费者两端，可以使用此tag来存储"topic name"
    public static final String MESSAGE_BUS_DESTINATION = "message_bus.destination";

    //远程地址。 适合在网络调用的客户端使用。存储的内容可能是"ip:port"， "hostname"，域名，甚至是一个JDBC的连接串，如 "mysql://prod-db:3306"
    public static final String PEER_ADDRESS = "peer.address";

    //远端主机名。例如 "opentracing.io", "internal.dns.name"
    public static final String PEER_HOSTNAME = "peer.hostname";

    //远端 IPv4 地址，使用 . 分隔。例如 "127.0.0.1"
    public static final String PEER_IPV4 = "peer.ipv4";

    //远程 IPv6 地址，使用冒号分隔的元祖，每个元素为4位16进制数。例如 "2001:0db8:85a3:0000:0000:8a2e:0370:7334"
    public static final String PEER_IPV6 = "peer.ipv6";

    //远程端口。如 80
    public static final String PEER_PORT = "peer.port";

    //远程服务名（针对没有被标准化定义的"service"）。例如 "elasticsearch", "a_custom_microservice", "memcache"
    public static final String PEER_SERVICE = "peer.service";

    //基于RPC的调用角色，"client" 或 "server". 基于消息的调用角色，"producer" 或 "consumer"
    public static final String SPAN_KIND = "span.kind";

    // 自定义字段

    //工程名
    public static final String PRO_NAME = "project.name";

    //类名
    public static final String CLASS_NAME = "class.name";

    //方法名，格式“SpanTags.Xxx”
    public static final String MEHODE_NAME = "method.name";

    //错误信息
    public static final String ERROR_MSG = "error.msg";

}
