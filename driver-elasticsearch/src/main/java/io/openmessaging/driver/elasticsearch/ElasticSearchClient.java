package io.openmessaging.driver.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ElasticSearchClient {
    private RestClient client;

    public ElasticSearchClient(RestClient _client) {
        client = _client;
    }

    public List<String> sendRequestAsyc(Request request) {
        List<String> res = new ArrayList<>();
        try {
            client.performRequestAsync(request, new ResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                                response.getEntity().getContent()));
                        String str = "";
                        while ((str = bufferedReader.readLine()) != null) {
                            res.add(str);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    try {
                        throw e;
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Send Request faild!");
        }
        return res;
    }

}
