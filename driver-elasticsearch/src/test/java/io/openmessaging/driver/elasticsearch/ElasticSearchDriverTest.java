package io.openmessaging.driver.elasticsearch;

import io.openmessaging.driver.elasticsearch.config.ElasticSearchConfig;
import io.openmessaging.driver.elasticsearch.core.ElasticSearchFactory;
import junit.framework.TestCase;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElasticSearchDriverTest extends TestCase {
    static ElasticSearchDriver driver = new ElasticSearchDriver();
    static File file = Mockito.mock(File.class);
    static List<String> nodes = new ArrayList<>();
    static {
        ElasticSearchFactory.initial(new ArrayList<String>() {{
            add("localhost");
        }}, "elastic", "elastic");
    }


    public void testShutdown() {
        driver.shutdown();
    }

    public void testCreateChaosNode() {
        ElasticSearchConfig config = Mockito.mock(ElasticSearchConfig.class);
        driver.setElasticsearchConfig(config);
        assertNotNull(driver.createChaosNode("", nodes));
    }

    public void testGetStateName() {
        assertNotNull(driver.getStateName());
    }

    public void testCreateClient() {
        assertNotNull(driver.createClient());
    }
}