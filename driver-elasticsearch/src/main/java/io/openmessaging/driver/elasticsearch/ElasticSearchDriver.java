package io.openmessaging.driver.elasticsearch;

import io.openchaos.driver.ChaosNode;
import io.openchaos.driver.kv.KVClient;
import io.openchaos.driver.kv.KVDriver;
import io.openmessaging.driver.elasticsearch.core.ElasticSearchFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ElasticSearchDriver implements KVDriver {
    @Override
    public String getMetaNode() {
        return null;
    }

    @Override
    public String getMetaName() {
        return null;
    }

    @Override
    public void initialize(File configurationFile, List<String> nodes) throws IOException {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public ChaosNode createChaosNode(String node, List<String> nodes) {
        return null;
    }

    @Override
    public String getStateName() {
        return null;
    }

    @Override
    public KVClient createClient() {
        return null;
    }
}
