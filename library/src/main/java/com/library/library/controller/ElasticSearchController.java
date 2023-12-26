package com.library.library.controller;

import com.library.library.entity.Logs;
import com.library.library.service.ElasticSearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "elastic")
public class ElasticSearchController {

    ElasticSearchService elasticSearchService;
    @GetMapping("/search/{level}")
    public ResponseEntity<Object> searchByLevel(@PathVariable String level) throws NoSuchFieldException {
        List<Logs> logs = elasticSearchService.searchByLevel(level);
        return ResponseEntity.ok(logs);
    }
}
