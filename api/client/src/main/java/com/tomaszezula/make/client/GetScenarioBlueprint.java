package com.tomaszezula.make.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class GetScenarioBlueprint {

    public static void main(String[] args) {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            int scenarioId = 335282; // Replace with YOUR SCENARIO ID
            HttpGet httpGet = new HttpGet("http://localhost:8080/scenarios/" + scenarioId + "/blueprint");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpGet);
            assert(response.getStatusLine().getStatusCode() == 200);
            response.getEntity().writeTo(System.out);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
