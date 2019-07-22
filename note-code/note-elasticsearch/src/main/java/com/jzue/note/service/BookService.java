package com.jzue.note.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchPhaseResult;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @Author: junzexue
 * @Date: 2019/7/19 上午1:13
 * @Description:
 **/
@Service
@Slf4j
public class BookService {

    @Autowired
    RestHighLevelClient restHighLevelClient;


    public boolean testESRestClient(){
        SearchRequest searchRequest=new SearchRequest("test_A*");
        SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("test","book"));
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse response=restHighLevelClient.search(searchRequest);
            Arrays.stream(response.getHits().getHits())
                    .forEach(item->{
                        log.info(item.getIndex());
                    });
            log.info("total:{}",response.getHits().totalHits());
            return true;
        }catch (IOException e){
            log.error(e.getMessage());
            return false;
        }
    }

}
