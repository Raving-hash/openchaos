package io.openchaos.driver.elasticsearch.core;

import junit.framework.TestCase;

import java.util.ArrayList;

public class ElasticSearchFactoryTest extends TestCase {

    public void testGetClient() {
        ElasticSearchFactory.initial(new ArrayList<String>() {{
            add("localhost");
        }}, "elastic", "elastic", true);
        assertNotNull(ElasticSearchFactory.getClient());
    }
}