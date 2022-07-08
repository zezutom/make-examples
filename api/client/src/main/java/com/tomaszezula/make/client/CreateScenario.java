package com.tomaszezula.make.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class CreateScenario {

    public static void main(String[] args) {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            Path filePath = Path.of(ClassLoader.getSystemResource("blueprint.json").getPath());
            String blueprint = Files.readString(filePath);
            String encodedBlueprint = Base64.getEncoder().encodeToString(blueprint.getBytes());
            StringEntity entity = new StringEntity(
                    "{" +
                            "\"blueprint\":" + encodedBlueprint + "," +
                            "\"schedulingType\":\"INDEFINITE\"" +
                            "\"schedulingInterval\":900" +
                            "\"folderId\":22143" +
                            "\"teamId\":55228" +
                    "}");
            HttpPost httpPost = new HttpPost("http://localhost:8080/scenarios");
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            assert(response.getStatusLine().getStatusCode() == 201);
            response.getEntity().writeTo(System.out);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
