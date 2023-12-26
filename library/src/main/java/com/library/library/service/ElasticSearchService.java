package com.library.library.service;

import com.library.library.elasticSearchQuery.ElasticSearchQuery;
import com.library.library.entity.Logs;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ElasticSearchService {

    ElasticSearchQuery elasticSearchQuery;


    public List<Logs> searchByLevel(String level) throws NoSuchFieldException {
        List<Logs> logs = elasticSearchQuery.getDocumentLogByLevel(level);
        return logs;
    }
}
