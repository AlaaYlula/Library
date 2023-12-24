package com.library.library.Service;

import com.library.library.ElasticSearchQuery.ElasticSearchQuery;
import com.library.library.Entity.Logs;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
