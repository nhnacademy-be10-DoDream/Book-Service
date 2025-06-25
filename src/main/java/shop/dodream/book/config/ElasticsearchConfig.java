package shop.dodream.book.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.auth.UsernamePasswordCredentials;



@Configuration
public class ElasticsearchConfig{


    @Value("${elasticsearch.uris}")
    private String host;

    @Value("${elasticsearch.user-id}")
    private String userId;

    @Value("${elasticsearch.user-password}")
    private String userPassword;


    @Bean
    public ElasticsearchClient elasticsearchClient() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        JacksonJsonpMapper mapper = new JacksonJsonpMapper(objectMapper);


        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userId, userPassword));

        RestClientBuilder restClientBuilder = RestClient.builder(HttpHost.create(host))
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                );

        RestClient restClient = restClientBuilder.build();

        return new ElasticsearchClient(
                new RestClientTransport(restClient, mapper)
        );
    }


}
