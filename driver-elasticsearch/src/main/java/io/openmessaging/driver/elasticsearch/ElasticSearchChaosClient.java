package io.openmessaging.driver.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.openchaos.common.InvokeResult;
import io.openchaos.driver.kv.KVClient;
import io.openmessaging.driver.elasticsearch.core.Document;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ElasticSearchChaosClient implements KVClient {
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchChaosClient.class);
    private RestClient esClient;
    private final String endpoint = "/openchaos";
    private final String type = "/it";
    private ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public ElasticSearchChaosClient(RestClient client) {
        esClient = client;
    }

    @Override
    public void start() {
        Request request = new Request("PUT", endpoint);
        try {
            esClient.performRequest(request);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void close() {}

    @Override
    public InvokeResult put(Optional<String> key, String value) {
        try {
            String jsonStr = serialize(key, value);
            String id = "/" + key + value;
            Request request = new Request("PUT", endpoint + type + id);
            NStringEntity entity = new NStringEntity(jsonStr, ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            Response response = esClient.performRequest(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.error("Method failed: " + response.getStatusLine());
            } else {
                return InvokeResult.SUCCESS;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return InvokeResult.FAILURE;
    }

    @Override
    public List<String> getAll(Optional<String> key, int putInvokeCount) {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < putInvokeCount; ++i) {
            String id = "/" + key + i;
            String method = "GET";
            Request request = new Request(method, endpoint + type + id);
            try {
                Response response = esClient.performRequest(request);
                Document document = deserialize(response.getEntity());
                values.add(document.getValue());
            } catch (IOException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return values;
    }

    @Override
    public List<String> getAll(Optional<String> key) {
        return null;
    }

    private String serialize(Optional<String> key, String value) throws JsonProcessingException {
        Document document = new Document();
        document.setKey(key);
        document.setValue(value);
        return objectMapper.writeValueAsString(document);
    }

    private Document deserialize(HttpEntity _entity) throws IOException {
        String entity = EntityUtils.toString(_entity);
        return objectMapper.readValue(entity, Document.class);
    }

    public RestClient getEsClient() {
        return esClient;
    }

    public void setEsClient(RestClient esClient) {
        this.esClient = esClient;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getType() {
        return type;
    }
}
