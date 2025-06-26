//package shop.dodream.book.core.config;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.transport.rest_client.RestClientTransport;
//import co.elastic.clients.json.jackson.JacksonJsonpMapper;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
//@Configuration
//public class ElasticsearchConfig {
//
//    @Value("${spring.elasticsearch.uris}")
//    private String host;
//
//    @Bean
//    public ElasticsearchClient elasticsearchClient() {
//        RestClient restClient = RestClient.builder(host).build();
//        return new ElasticsearchClient(new RestClientTransport(restClient, new JacksonJsonpMapper()));
//    }
//}
