package io.openmessaging.driver.elasticsearch.core;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ElasticSearchFactory {
    private static volatile RestClient client;
    private static String username;
    private static String password;
    private static List<String> nodes;

    public static void initial(List<String> nodes, String username, String password) {
        ElasticSearchFactory.nodes = nodes;
        ElasticSearchFactory.username = username;
        ElasticSearchFactory.password = password;
    }

    public static RestClient getClient() {
        if (client == null || !client.isRunning()) {
            synchronized (ElasticSearchFactory.class) {
                if (client == null || !client.isRunning()) {
                    HttpHost[] hosts = getHosts(nodes);
                    RestClientBuilder clientBuilder = RestClient
                            .builder(hosts)
                            .setCompressionEnabled(true);
                    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
                    try {
                        final SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
                        clientBuilder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                                .setSSLContext(sslContext)
                                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                                .setDefaultCredentialsProvider(credentialsProvider));
                    } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                        throw new RuntimeException(e);
                    }

                    client = clientBuilder.build();
                }
            }
        }
        return client;
    }

    private static HttpHost[] getHosts(List<String> _nodes) {
        HttpHost[] hosts = new HttpHost[_nodes.size()];
        for (int i = 0; i < _nodes.size(); ++i) {
            hosts[i] = new HttpHost(_nodes.get(i), 9200, "https");
        }
        return hosts;
    }

    public static void close() {
        if (client != null || !client.isRunning()) {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
