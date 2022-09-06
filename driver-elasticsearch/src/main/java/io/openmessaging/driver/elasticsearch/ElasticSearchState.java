package io.openmessaging.driver.elasticsearch;

import io.openchaos.driver.ChaosState;

import java.util.Set;

public class ElasticSearchState implements ChaosState {
    @Override
    public void initialize(String metaName, String metaNode) {

    }

    @Override
    public Set<String> getLeader() {
        return null;
    }

    @Override
    public void close() {

    }
}
