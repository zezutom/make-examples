# Make API
A thin wrapper around [Make API](https://www.make.com/en/api-documentation).

## Prerequisites
1. Create a [Make](https://www.make.com/en/register) account.
2. Generate an [authentication token](https://www.make.com/en/api-documentation/authentication-token).

## Start the Start

First, build and package the server.
```yaml
./gradlew shadowJar
```

Next, run the server passing your authentication token.
```shell
java -cp build/libs/api-0.0.1-all.jar com.tomaszezula.make.ApplicationKt -DMAKE_AUTH_TOKEN=<YOUR AUTHENTICATION TOKEN>
```

Beware, your authentication token is sensitive information. Never store in this repository!

## Use the API
### Create Scenario

`POST /scenarios`

#### Request Parameters
| Parameter          | Type    | Description                                                                              |
|--------------------|---------|------------------------------------------------------------------------------------------|
| blueprint          | String  | Base64-encoded scenario blueprint.                                                       |
| schedulingType     | String  | Determines scenario scheduling. Only `INDEFINITE` scheduling is supported at the moment. |
 | schedulingInterval | Integer | Completes the scheduling type.                                                           |
 | folderId           | Integer | Identifies the folder where the scenario should be stored.                               |
 | teamId             | Integer | Identifies the target team.                                                              |

#### Responses
* 201 Created
* 400 Bad Request
* 500 Internal Server Error

A successful response (201 Created) contains an identifier of the created scenario. For example:
```json
{
    "scenarioId": 373482
}
```

#### Passing of the Scenario Blueprint
The `blueprint` is passed as base64-encoded string. This is to avoid the hassle with having to escape
JSON within JSON.

Consider the following blueprint of an empty scenario:
```json
                {
                    "name": "New scenario",
                    "flow": [
                        {
                            "id": null,
                            "module": "placeholder:Placeholder",
                            "metadata": {
                                "designer": {
                                    "x": 0,
                                    "y": 0
                                }
                            }
                        }
                    ],
                    "metadata": {
                        "instant": false,
                        "version": 1,
                        "scenario": {
                            "roundtrips": 1,
                            "maxErrors": 3,
                            "autoCommit": true,
                            "autoCommitTriggerLast": true,
                            "sequential": false,
                            "confidential": false,
                            "dataloss": false,
                            "dlq": false
                        },
                        "designer": {
                            "orphans": []
                        },
                        "zone": "eu1.make.com"
                    }
                }
```
The blueprint would have to be passed as an escaped JSON string, like this:
```json
{
  "blueprint": "{\"name\":\"New scenario\", ... }"
}
```
Instead, encode the blueprint as a base64 string and pass the resulting output:
```json
{
  "blueprint": "ICAgICAgICAgICAgICAgIHsKICAgICAgICAgICAgICAgICAgICAibmFtZSI6ICJOZXcgc2NlbmFyaW8iLAogICAgICAgICAgICAgICAgICAgICJmbG93IjogWwogICAgICAgICAgICAgICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAiaWQiOiBudWxsLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgIm1vZHVsZSI6ICJwbGFjZWhvbGRlcjpQbGFjZWhvbGRlciIsCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAibWV0YWRhdGEiOiB7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgImRlc2lnbmVyIjogewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAieCI6IDAsCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICJ5IjogMAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIH0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIH0KICAgICAgICAgICAgICAgICAgICAgICAgfQogICAgICAgICAgICAgICAgICAgIF0sCiAgICAgICAgICAgICAgICAgICAgIm1ldGFkYXRhIjogewogICAgICAgICAgICAgICAgICAgICAgICAiaW5zdGFudCI6IGZhbHNlLAogICAgICAgICAgICAgICAgICAgICAgICAidmVyc2lvbiI6IDEsCiAgICAgICAgICAgICAgICAgICAgICAgICJzY2VuYXJpbyI6IHsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICJyb3VuZHRyaXBzIjogMSwKICAgICAgICAgICAgICAgICAgICAgICAgICAgICJtYXhFcnJvcnMiOiAzLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgImF1dG9Db21taXQiOiB0cnVlLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgImF1dG9Db21taXRUcmlnZ2VyTGFzdCI6IHRydWUsCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAic2VxdWVudGlhbCI6IGZhbHNlLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgImNvbmZpZGVudGlhbCI6IGZhbHNlLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgImRhdGFsb3NzIjogZmFsc2UsCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAiZGxxIjogZmFsc2UKICAgICAgICAgICAgICAgICAgICAgICAgfSwKICAgICAgICAgICAgICAgICAgICAgICAgImRlc2lnbmVyIjogewogICAgICAgICAgICAgICAgICAgICAgICAgICAgIm9ycGhhbnMiOiBbXQogICAgICAgICAgICAgICAgICAgICAgICB9LAogICAgICAgICAgICAgICAgICAgICAgICAiem9uZSI6ICJldTEubWFrZS5jb20iCiAgICAgICAgICAgICAgICAgICAgfQogICAgICAgICAgICAgICAgfQ=="
}
```

#### Example of a Request Body
```json
{
	"blueprint": "ICAgICAgICAgICAgICAgIHsKICAgICAgICAgICAgICAgICAgICAibmFtZSI6ICJOZXcgc2NlbmFyaW8iLAogICAgICAgICAgICAgICAgICAgICJmbG93IjogWwogICAgICAgICAgICAgICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAiaWQiOiBudWxsLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgIm1vZHVsZSI6ICJwbGFjZWhvbGRlcjpQbGFjZWhvbGRlciIsCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAibWV0YWRhdGEiOiB7CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgImRlc2lnbmVyIjogewogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAieCI6IDAsCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICJ5IjogMAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIH0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIH0KICAgICAgICAgICAgICAgICAgICAgICAgfQogICAgICAgICAgICAgICAgICAgIF0sCiAgICAgICAgICAgICAgICAgICAgIm1ldGFkYXRhIjogewogICAgICAgICAgICAgICAgICAgICAgICAiaW5zdGFudCI6IGZhbHNlLAogICAgICAgICAgICAgICAgICAgICAgICAidmVyc2lvbiI6IDEsCiAgICAgICAgICAgICAgICAgICAgICAgICJzY2VuYXJpbyI6IHsKICAgICAgICAgICAgICAgICAgICAgICAgICAgICJyb3VuZHRyaXBzIjogMSwKICAgICAgICAgICAgICAgICAgICAgICAgICAgICJtYXhFcnJvcnMiOiAzLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgImF1dG9Db21taXQiOiB0cnVlLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgImF1dG9Db21taXRUcmlnZ2VyTGFzdCI6IHRydWUsCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAic2VxdWVudGlhbCI6IGZhbHNlLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgImNvbmZpZGVudGlhbCI6IGZhbHNlLAogICAgICAgICAgICAgICAgICAgICAgICAgICAgImRhdGFsb3NzIjogZmFsc2UsCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAiZGxxIjogZmFsc2UKICAgICAgICAgICAgICAgICAgICAgICAgfSwKICAgICAgICAgICAgICAgICAgICAgICAgImRlc2lnbmVyIjogewogICAgICAgICAgICAgICAgICAgICAgICAgICAgIm9ycGhhbnMiOiBbXQogICAgICAgICAgICAgICAgICAgICAgICB9LAogICAgICAgICAgICAgICAgICAgICAgICAiem9uZSI6ICJldTEubWFrZS5jb20iCiAgICAgICAgICAgICAgICAgICAgfQogICAgICAgICAgICAgICAgfQ==",
	"schedulingType": "INDEFINITE",
	"schedulingInterval": 900,
	"folderId": 1,
	"teamId": 2
}
```




