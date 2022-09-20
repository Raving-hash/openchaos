package io.openmessaging.driver.elasticsearch;

import io.openchaos.common.utils.KillProcessUtil;
import io.openchaos.common.utils.NetUtil;
import io.openchaos.common.utils.PauseProcessUtil;
import io.openchaos.common.utils.SshUtil;
import io.openchaos.driver.kv.KVNode;
import io.openmessaging.driver.elasticsearch.config.ElasticSearchClientConfig;
import io.openmessaging.driver.elasticsearch.config.ElasticSearchConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ElasticSearchChaosNode implements KVNode {
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchChaosNode.class);
    private static final String ES_PROCESS_NAME = "elasticsearch";
    private String installDir = "es-chaos-test";
    private String node;
    private List<String> nodes;
    private String elasticsearchVersion = "8.10";

    public ElasticSearchChaosNode(String node, List<String> nodes, ElasticSearchConfig elasticSearchConfig) {
        this.node = node;
        this.nodes = nodes;
        if (elasticSearchConfig.installDir != null && !elasticSearchConfig.installDir.isEmpty()) {
            this.installDir = elasticSearchConfig.installDir;
        }
        if (elasticSearchConfig.elasticsearchVersion != null && !elasticSearchConfig.elasticsearchVersion.isEmpty()) {
            this.elasticsearchVersion = elasticSearchConfig.elasticsearchVersion;
        }
    }
    @Override
    public void setup() {

    }

    @Override
    public void teardown() {
        stop();
    }

    @Override
    public void start() {
        try {
            SshUtil.execCommandInDir(node, installDir, "./bin/elasticsearch -d");
        } catch (Exception e) {
            log.error("Node {} start es node failed", node, e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void stop() {
        try {
            KillProcessUtil.kill(node, ES_PROCESS_NAME);
        } catch (Exception e) {
            log.error("Node {} stop es node failed", node, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void kill() {
        try {
            KillProcessUtil.forceKill(node, ES_PROCESS_NAME);
        } catch (Exception e) {
            log.error("Node {} kill es node failed", node, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pause() {
        try {
            PauseProcessUtil.suspend(node, ES_PROCESS_NAME);
        } catch (Exception e) {
            log.error("Node {} pause es node failed", node, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resume() {
        try {
            PauseProcessUtil.resume(node, ES_PROCESS_NAME);
        } catch (Exception e) {
            log.error("Node {} resume es node failed", node, e);
            throw new RuntimeException(e);
        }
    }
}
