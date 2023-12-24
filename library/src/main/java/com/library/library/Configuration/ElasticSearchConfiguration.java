package com.library.library.Configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfiguration {

    @Value("${elasticSearchUrl}")
    String elasticSearchUrl;
    @Bean
    public RestClient getRestClient(){
        // Create the low-level client
        RestClient restClient = RestClient
                .builder(HttpHost.create(elasticSearchUrl))
                .build();
        return  restClient;
    }

    @Bean
    public ElasticsearchTransport getElasticSearchTransport(){
        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                getRestClient(), new JacksonJsonpMapper());
        return transport;
    }

    @Bean
    public ElasticsearchClient getElasticSearchClient(){
        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(getElasticSearchTransport());
        return esClient;
    }

}
