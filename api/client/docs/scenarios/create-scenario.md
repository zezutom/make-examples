# Create Scenario

## Java
Reference implementation: [CreateScenario.java](../../src/main/java/com/tomaszezula/make/client/CreateScenario.java)

```java
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
```
## JavaScript
Reference implementation: [scenarios.js](../../nodejs/routes/scenarios.js)
```javascript
const axios = require('axios');
const fs = require('fs');

fs.readFile(__dirname + '/blueprint.json', (err, blueprint) => {
    if (err) throw err;

    const buffer = new Buffer(blueprint);
    const encodedBlueprint = buffer.toString('base64');

    axios.post('http://localhost:8080/scenarios', {
        blueprint: encodedBlueprint,
        schedulingType: "INDEFINITE",
        schedulingInterval: 900,
        folderId: 22143,
        teamId: 55228
    }).then(response => {
            console.log(`Status code: ${response.status}`);
            console.log(response.data);
            res.json(response.data);
        }
    ).catch(err => {
        console.error(err);
        res.render("");
    });
});
```
## Command Line
```shell
curl -X POST \
  http://localhost:8080/scenarios \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"blueprint": "ICAgICAgICAgICAgICAgIHsKICAgICAgICAgICAgICAgICAgICAibmFtZSI6ICJOZXcgc2NlbmFyaW8iLAogICAgICAgICAgICAgICAgICAgICJmbG93IjogWwogICAgICAgICAgICAgICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAiaWQiOiBudWxsLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgIm1vZHVsZSI6ICJwbGFjZWhvbGRlcjpQbGFjZWhvbGRlciIsCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAibWV0YWRhdGEiOiB7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgImRlc2lnbmVyIjogewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAieCI6IDAsCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICJ5IjogMAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIH0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIH0KICAgICAgICAgICAgICAgICAgICAgICAgfQogICAgICAgICAgICAgICAgICAgIF0sCiAgICAgICAgICAgICAgICAgICAgIm1ldGFkYXRhIjogewogICAgICAgICAgICAgICAgICAgICAgICAiaW5zdGFudCI6IGZhbHNlLAogICAgICAgICAgICAgICAgICAgICAgICAidmVyc2lvbiI6IDEsCiAgICAgICAgICAgICAgICAgICAgICAgICJzY2VuYXJpbyI6IHsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICJyb3VuZHRyaXBzIjogMSwKICAgICAgICAgICAgICAgICAgICAgICAgICAgICJtYXhFcnJvcnMiOiAzLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgImF1dG9Db21taXQiOiB0cnVlLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgImF1dG9Db21taXRUcmlnZ2VyTGFzdCI6IHRydWUsCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAic2VxdWVudGlhbCI6IGZhbHNlLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgImNvbmZpZGVudGlhbCI6IGZhbHNlLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgImRhdGFsb3NzIjogZmFsc2UsCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAiZGxxIjogZmFsc2UKICAgICAgICAgICAgICAgICAgICAgICAgfSwKICAgICAgICAgICAgICAgICAgICAgICAgImRlc2lnbmVyIjogewogICAgICAgICAgICAgICAgICAgICAgICAgICAgIm9ycGhhbnMiOiBbXQogICAgICAgICAgICAgICAgICAgICAgICB9LAogICAgICAgICAgICAgICAgICAgICAgICAiem9uZSI6ICJldTEubWFrZS5jb20iCiAgICAgICAgICAgICAgICAgICAgfQogICAgICAgICAgICAgICAgfQ==",
	"schedulingType": "INDEFINITE",
	"schedulingInterval": 900,
	"folderId": 22143,
	"teamId": 55228
}'
```
