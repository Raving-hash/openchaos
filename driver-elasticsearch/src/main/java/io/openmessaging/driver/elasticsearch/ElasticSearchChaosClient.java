package io.openmessaging.driver.elasticsearch;

import io.openchaos.common.InvokeResult;
import io.openchaos.driver.kv.KVClient;
import io.openmessaging.driver.elasticsearch.core.ElasticSearchClient;
import org.elasticsearch.client.RestClient;

import java.util.List;
import java.util.Optional;

public class ElasticSearchChaosClient implements KVClient {
    private ElasticSearchClient esClient;

    public ElasticSearchChaosClient(RestClient client){
        esClient = new ElasticSearchClient(client);
    }
    @Override
    public void start() {
        
    }

    @Override
    public void close() {

    }

    @Override
    public InvokeResult put(Optional<String> key, String value) {

        return null;
    }

    @Override
    public List<String> getAll(Optional<String> key, int putInvokeCount) {
        return null;
    }

    @Override
    public List<String> getAll(Optional<String> key) {
        return null;
    }
}
