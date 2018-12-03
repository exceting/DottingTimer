package dotting.timer.server.es;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by 18073 on 2018/12/3.
 */
@Component
public class EsTask<T> {

    private Logger logger = LoggerFactory.getLogger(EsTask.class);

    @Resource
    private TransportClient esClient;

    /**
     * 存放数据
     */
    public int setData(String index, String type, String id, T t) {
        try {
            if (esClient != null && t != null) {
                String json = JSON.toJSONString(t);
                esClient.prepareIndex(index, type, id).setSource(json).get();
                return 1;
            }
        } catch (Exception e) {
            logger.error("es set data error! id = " + id, e);
        }
        return -1;
    }

    /**
     * ES中id的搜索
     */
    public T getDataById(String index, String type, String id, Class clazz) {
        try {
            if (esClient != null) {
                GetResponse response = esClient.prepareGet(index, type, id).execute().actionGet();
                String result = response.getSourceAsString();
                if (!StringUtils.isEmpty(result)) {
                    return (T) JSON.parseObject(result, clazz);
                }
            }
        } catch (Exception e) {
            logger.error("es get data by id error! id = " + id, e);
        }
        return null;
    }

    /**
     * 关键词搜索
     */
    public List<T> getDatasByKeyWord(String index, String type, String keyWord, Class clazz, int from, int size) {
        try {
            if (esClient != null) {
                SearchResponse scrollResp = esClient.prepareSearch(index)
                        .setTypes(type)
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                        .setQuery(QueryBuilders.termQuery("title", keyWord))
                        .setFrom(from).setSize(size).setExplain(true).execute().actionGet();
                List<T> result = new ArrayList<T>();
                SearchHits hits = scrollResp.getHits();
                for (SearchHit hit : hits) {
                    String json = hit.getSourceAsString();
                    if (!StringUtils.isEmpty(json)) {
                        result.add((T) JSON.parseObject(json, clazz));
                    }
                }
                return result;
            }
        } catch (Exception e) {
            logger.error("es get data by key error! key = " + keyWord, e);
        }
        return null;
    }

}
