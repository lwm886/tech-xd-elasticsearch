package cn.tech.service.impl;

import cn.tech.dao.NBAPlayerDao;
import cn.tech.model.NBAPlayer;
import cn.tech.service.NBAPlayerService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lw
 * @since 2021/4/22
 **/
@Slf4j
@Service
public class NBAPlayerServiceImpl implements NBAPlayerService {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private NBAPlayerDao nbaPlayerDao;

    private static final String NBA_INDEX = "nba_latest";

    private static final int START_OFFSET = 0;

    private static final int MAX_COUNT = 1000;

    @Override
    public boolean addPlayer(NBAPlayer nbaPlayer, String id) throws IOException {
        IndexRequest request = new IndexRequest(NBA_INDEX).id(id).source(beanToMap(nbaPlayer));
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        log.info(JSON.toJSONString(response));
        return true;
    }

    @Override
    public Map<String, Object> getPlayer(String id) throws IOException {
        GetRequest getRequest = new GetRequest(NBA_INDEX, id);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        return getResponse.getSource();
    }

    @Override
    public boolean updatePlayer(NBAPlayer nbaPlayer, String id) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(NBA_INDEX, id).doc(beanToMap(nbaPlayer));
        UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
        log.info(JSON.toJSONString(update));
        return true;
    }

    @Override
    public boolean deletePlayer(String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(NBA_INDEX, id);
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        log.info(JSON.toJSONString(deleteResponse));
        return true;
    }

    @Override
    public boolean deleteAllPlayer() throws IOException {
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(NBA_INDEX).setQuery(new MatchAllQueryBuilder());
        BulkByScrollResponse response = client.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        log.info(JSON.toJSONString(response));
        return true;
    }

    @Override
    public boolean importAll() throws IOException {
        List<NBAPlayer> nbaPlayers = nbaPlayerDao.selectAll();
        for (NBAPlayer nbaPlayer : nbaPlayers) {
            addPlayer(nbaPlayer, nbaPlayer.getPlayerId());
        }
        return true;
    }

    @Override
    public List<NBAPlayer> searchMatch(String key, String val) throws IOException {
        SearchRequest searchRequest = new SearchRequest(NBA_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(key, val));
        searchSourceBuilder.from(START_OFFSET);
        searchSourceBuilder.size(MAX_COUNT);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        log.info(JSON.toJSONString(searchResponse));

        SearchHit[] hits = searchResponse.getHits().getHits();
        List<NBAPlayer> list = new ArrayList<>();
        for (SearchHit searchHit : hits) {
            NBAPlayer nbaPlayer = JSON.parseObject(searchHit.getSourceAsString(), NBAPlayer.class);
            list.add(nbaPlayer);
        }
        return list;
    }

    @Override
    public List<NBAPlayer> searchTerm(String key, String val) throws IOException {
        SearchRequest searchRequest = new SearchRequest(NBA_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery(key, val));
        searchSourceBuilder.from(START_OFFSET);
        searchSourceBuilder.size(MAX_COUNT);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//        log.info(JSON.toJSONString(searchResponse));

        SearchHit[] hits = searchResponse.getHits().getHits();
        List<NBAPlayer> list = new ArrayList<>();
        for (SearchHit searchHit : hits) {
            NBAPlayer nbaPlayer = JSON.parseObject(searchHit.getSourceAsString(), NBAPlayer.class);
            list.add(nbaPlayer);
        }
        return list;
    }

    @Override
    public List<NBAPlayer> searchMatchPrefix(String key, String val) throws IOException {
        SearchRequest searchRequest = new SearchRequest(NBA_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.prefixQuery(key, val));
        searchSourceBuilder.from(START_OFFSET);
        searchSourceBuilder.size(MAX_COUNT);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//        log.info(JSON.toJSONString(searchResponse));

        SearchHit[] hits = searchResponse.getHits().getHits();
        List<NBAPlayer> list = new ArrayList<>();
        for (SearchHit searchHit : hits) {
            NBAPlayer nbaPlayer = JSON.parseObject(searchHit.getSourceAsString(), NBAPlayer.class);
            list.add(nbaPlayer);
        }
        return list;
    }

    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                if (beanMap.get(key) != null) {
                    map.put(key + "", beanMap.get(key));
                }
            }
        }
        return map;
    }
}
