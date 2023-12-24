package com.library.library.ElasticSearchQuery;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.library.library.Entity.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ElasticSearchQuery {

    @Autowired
    private ElasticsearchClient elasticsearchClient;
    //@Value("{indexName}")
    String indexName = "logsearch";

    public String createDocumentLog(Logs log){
        try {
            IndexResponse response = elasticsearchClient.index(i -> i
                    .index(indexName)
                    .id(log.getId())
                    .document(log)
            );
            System.out.println("Index Response: " + response);
            return "Create Document Log";

        }catch (Exception e){
            return e.getMessage();
        }

    }

    public List<Logs> getDocumentLogByLevel(String level) throws NoSuchFieldException {
        List<Logs> logs = null;
        try {
            SearchResponse<Logs> searchResponse = elasticsearchClient.search(s -> s
                            .index(indexName)
                            .size(100)
                            .query(q -> q
                                    .match(t -> t
                                            .field("level")
                                            .query("(?i)" + level) // case_insensitive
                                    )
                            ),
                    Logs.class
            );

            if(!searchResponse.hits().hits().isEmpty()) {
                logs = searchResponse.hits().hits().stream().map(Hit::source).toList();
            }else{
                System.out.println("the response Empty Logs List");
            }
        }catch (Exception e){
            throw new NoSuchFieldException();
        }
        return logs;

    }

}
