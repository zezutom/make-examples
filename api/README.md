# Make API
A thin wrapper around [Make API](https://www.make.com/en/api-documentation).

## Prerequisites
1. Create a [Make](https://www.make.com/en/register) account.
2. Generate an [authentication token](https://www.make.com/en/api-documentation/authentication-token).

## Start the Server

First, build and package the server.
```yaml
./gradlew shadowJar
```

Next, run the server passing your authentication token.
```shell
java -cp build/libs/api-0.0.1-all.jar com.tomaszezula.make.ApplicationKt -DMAKE_AUTH_TOKEN=<YOUR AUTHENTICATION TOKEN>
```

Beware, your authentication token is sensitive information. Never store in this repository!

## Endpoints
### Create Scenario

`POST /scenarios`

> #### Client Code
> [Java](client/docs/scenarios/create-scenario.md#java) | [JavaScript](client/docs/scenarios/create-scenario.md#javascript) | [Command Line](client/docs/scenarios/create-scenario.md#command-line)

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

### Get Scenario Blueprint

`GET /scenarios/{scenarioId}/blueprint`

> #### Client Code
> [Java](client/docs/scenarios/get-scenario-blueprint.md#java) | [JavaScript](client/docs/scenarios/get-scenario-blueprint.md#javascript) | [Command Line](client/docs/scenarios/get-scenario-blueprint.md#command-line)

#### Request Parameters
| Parameter          | Type    | Description                                                                              |
|--------------------|---------|------------------------------------------------------------------------------------------|
| scenarioId         | String  | Identifies the scenario whose blueprint we want to fetch.                                |

#### Responses
* 200 OK
* 400 Bad Request
* 500 Internal Server Error

A successful response (200 OK) contains a list of scenario modules as well as the raw blueprint as JSON. For example:
```json
{
 "name": "Emails to Google Spreadsheet",
 "flows": [
  {
   "id": 6,
   "module": "email:TriggerNewEmail"
  },
  {
   "id": 5,
   "module": "google-sheets:addRow"
  }
 ],
 "json": "{\"code\":\"OK\",\"response\":{\"blueprint\":{\"flow\":[{\"id\":6,\"mapper\":{},\"module\":\"email:TriggerNewEmail\",\"version\":7,\"metadata\":{\"restore\":{\"parameters\":{\"folder\":{\"path\":[\"INBOX\"]},\"account\":{\"data\":{\"scoped\":\"true\",\"connection\":\"google-restricted\"},\"label\":\"My Google Restricted connection (zezutomtest@gmail.com)\"},\"criteria\":{\"label\":\"Only Unread emails\"}}},\"designer\":{\"x\":-285,\"y\":-1},\"parameters\":[{\"name\":\"account\",\"type\":\"account:imap,google-restricted\",\"label\":\"Connection\",\"required\":true},{\"name\":\"criteria\",\"type\":\"select\",\"label\":\"Criteria\",\"required\":true,\"validate\":{\"enum\":[\"ALL\",\"SEEN\",\"UNSEEN\"]}},{\"name\":\"from\",\"type\":\"email\",\"label\":\"Sender email address\"},{\"name\":\"to\",\"type\":\"email\",\"label\":\"Recipient email address\"},{\"name\":\"subject\",\"type\":\"text\",\"label\":\"Subject\"},{\"name\":\"text\",\"type\":\"text\",\"label\":\"Phrase\"},{\"name\":\"markSeen\",\"type\":\"boolean\",\"label\":\"Mark message(s) as read when fetched\"},{\"name\":\"maxResults\",\"type\":\"number\",\"label\":\"Maximum number of results\"},{\"name\":\"folder\",\"type\":\"folder\",\"label\":\"Folder\",\"required\":true}]},\"parameters\":{\"to\":\"\",\"from\":\"\",\"text\":\"\",\"folder\":\"INBOX\",\"account\":342062,\"subject\":\"\",\"criteria\":\"UNSEEN\",\"markSeen\":false,\"maxResults\":1}},{\"id\":5,\"mapper\":{\"from\":\"drive\",\"mode\":\"select\",\"values\":{\"0\":\"{{now}}\",\"1\":\"{{6.from.name}}\",\"2\":\"{{6.subject}}\",\"3\":\"{{6.from.address}}\",\"4\":\"=SUBSTITUTE(\\\"{{map(6.attachments; \\\"fileName\\\")}}\\\",\\\",\\\",CHAR(10))\",\"5\":\"{{sum(map(6.attachments; \\\"fileSize\\\"))}}\",\"6\":\"{{substring(6.text; 0; 100)}}\"},\"sheetId\":\"emails\",\"spreadsheetId\":\"/1jnTgFYLUau2YuC5fC67i7OEEuX5ECZGezbo_NZD7BtM\",\"includesHeaders\":true,\"insertDataOption\":\"INSERT_ROWS\",\"valueInputOption\":\"USER_ENTERED\"},\"module\":\"google-sheets:addRow\",\"version\":2,\"metadata\":{\"expect\":[{\"name\":\"mode\",\"type\":\"select\",\"label\":\"Choose a Method\",\"required\":true,\"validate\":{\"enum\":[\"select\",\"map\"]}},{\"name\":\"valueInputOption\",\"type\":\"select\",\"label\":\"Value input option\",\"validate\":{\"enum\":[\"USER_ENTERED\",\"RAW\"]}},{\"name\":\"insertDataOption\",\"type\":\"select\",\"label\":\"Insert data option\",\"validate\":{\"enum\":[\"INSERT_ROWS\",\"OVERWRITE\"]}},{\"name\":\"from\",\"type\":\"select\",\"label\":\"Choose a Drive\",\"required\":true,\"validate\":{\"enum\":[\"drive\",\"share\",\"team\"]}},{\"name\":\"spreadsheetId\",\"type\":\"file\",\"label\":\"Spreadsheet ID\",\"required\":true},{\"name\":\"sheetId\",\"type\":\"select\",\"label\":\"Sheet Name\",\"required\":true},{\"name\":\"includesHeaders\",\"type\":\"select\",\"label\":\"Table contains headers\",\"required\":true,\"validate\":{\"enum\":[true,false]}},{\"name\":\"values\",\"spec\":[{\"name\":\"0\",\"type\":\"text\",\"label\":\"A\"},{\"name\":\"1\",\"type\":\"text\",\"label\":\"B\"},{\"name\":\"2\",\"type\":\"text\",\"label\":\"C\"},{\"name\":\"3\",\"type\":\"text\",\"label\":\"D\"},{\"name\":\"4\",\"type\":\"text\",\"label\":\"E\"},{\"name\":\"5\",\"type\":\"text\",\"label\":\"F\"},{\"name\":\"6\",\"type\":\"text\",\"label\":\"G\"},{\"name\":\"7\",\"type\":\"text\",\"label\":\"H\"},{\"name\":\"8\",\"type\":\"text\",\"label\":\"I\"},{\"name\":\"9\",\"type\":\"text\",\"label\":\"J\"},{\"name\":\"10\",\"type\":\"text\",\"label\":\"K\"},{\"name\":\"11\",\"type\":\"text\",\"label\":\"L\"},{\"name\":\"12\",\"type\":\"text\",\"label\":\"M\"},{\"name\":\"13\",\"type\":\"text\",\"label\":\"N\"},{\"name\":\"14\",\"type\":\"text\",\"label\":\"O\"},{\"name\":\"15\",\"type\":\"text\",\"label\":\"P\"},{\"name\":\"16\",\"type\":\"text\",\"label\":\"Q\"},{\"name\":\"17\",\"type\":\"text\",\"label\":\"R\"},{\"name\":\"18\",\"type\":\"text\",\"label\":\"S\"},{\"name\":\"19\",\"type\":\"text\",\"label\":\"T\"},{\"name\":\"20\",\"type\":\"text\",\"label\":\"U\"},{\"name\":\"21\",\"type\":\"text\",\"label\":\"V\"},{\"name\":\"22\",\"type\":\"text\",\"label\":\"W\"},{\"name\":\"23\",\"type\":\"text\",\"label\":\"X\"},{\"name\":\"24\",\"type\":\"text\",\"label\":\"Y\"},{\"name\":\"25\",\"type\":\"text\",\"label\":\"Z\"}],\"type\":\"collection\",\"label\":\"Values\"}],\"restore\":{\"expect\":{\"from\":{\"label\":\"My Drive\"},\"mode\":{\"label\":\"Select from the list\"},\"sheetId\":{\"label\":\"emails\"},\"spreadsheetId\":{\"path\":[\"test\"]},\"includesHeaders\":{\"label\":\"Yes\",\"nested\":[{\"name\":\"values\",\"spec\":[{\"name\":\"0\",\"type\":\"text\",\"label\":\"A\"},{\"name\":\"1\",\"type\":\"text\",\"label\":\"B\"},{\"name\":\"2\",\"type\":\"text\",\"label\":\"C\"},{\"name\":\"3\",\"type\":\"text\",\"label\":\"D\"},{\"name\":\"4\",\"type\":\"text\",\"label\":\"E\"},{\"name\":\"5\",\"type\":\"text\",\"label\":\"F\"},{\"name\":\"6\",\"type\":\"text\",\"label\":\"G\"},{\"name\":\"7\",\"type\":\"text\",\"label\":\"H\"},{\"name\":\"8\",\"type\":\"text\",\"label\":\"I\"},{\"name\":\"9\",\"type\":\"text\",\"label\":\"J\"},{\"name\":\"10\",\"type\":\"text\",\"label\":\"K\"},{\"name\":\"11\",\"type\":\"text\",\"label\":\"L\"},{\"name\":\"12\",\"type\":\"text\",\"label\":\"M\"},{\"name\":\"13\",\"type\":\"text\",\"label\":\"N\"},{\"name\":\"14\",\"type\":\"text\",\"label\":\"O\"},{\"name\":\"15\",\"type\":\"text\",\"label\":\"P\"},{\"name\":\"16\",\"type\":\"text\",\"label\":\"Q\"},{\"name\":\"17\",\"type\":\"text\",\"label\":\"R\"},{\"name\":\"18\",\"type\":\"text\",\"label\":\"S\"},{\"name\":\"19\",\"type\":\"text\",\"label\":\"T\"},{\"name\":\"20\",\"type\":\"text\",\"label\":\"U\"},{\"name\":\"21\",\"type\":\"text\",\"label\":\"V\"},{\"name\":\"22\",\"type\":\"text\",\"label\":\"W\"},{\"name\":\"23\",\"type\":\"text\",\"label\":\"X\"},{\"name\":\"24\",\"type\":\"text\",\"label\":\"Y\"},{\"name\":\"25\",\"type\":\"text\",\"label\":\"Z\"}],\"type\":\"collection\",\"label\":\"Values\"}]},\"insertDataOption\":{\"mode\":\"chose\",\"label\":\"Insert rows\"},\"valueInputOption\":{\"mode\":\"chose\",\"label\":\"User entered\"}},\"parameters\":{\"__IMTCONN__\":{\"data\":{\"scoped\":\"true\",\"connection\":\"google\"},\"label\":\"My Google connection (zezutomtest@gmail.com)\"}}},\"advanced\":true,\"designer\":{\"x\":300,\"y\":0},\"parameters\":[{\"name\":\"__IMTCONN__\",\"type\":\"account:google\",\"label\":\"Connection\",\"required\":true}]},\"parameters\":{\"__IMTCONN__\":331456}}],\"name\":\"Emails to Google Spreadsheet\",\"metadata\":{\"instant\":false,\"version\":1,\"designer\":{\"orphans\":[],\"samples\":{\"5\":{\"updates\":{\"updatedRows\":1,\"updatedCells\":7,\"updatedRange\":\"emails!A6:G6...\",\"spreadsheetId\":\"1jnTgFYLUau2YuC5fC67i7OEEuX5ECZGezbo_NZD7BtM...\",\"updatedColumns\":7},\"rowNumber\":6,\"sheetName\":\"emails...\",\"tableRange\":\"emails!A1:G5...\",\"spreadsheetId\":\"1jnTgFYLUau2YuC5fC67i7OEEuX5ECZGezbo_NZD7BtM...\"},\"6\":{\"id\":30,\"to\":{\"name\":\"...\",\"address\":\"zezutomtest@gmail.com...\"},\"date\":\"2022-06-16T05:30:49.000Z\",\"from\":{\"name\":\"Google...\",\"address\":\"no-reply@accounts.google.com...\"},\"html\":\"<!DOCTYPE html><html lang=\\\"en\\\"><head><meta name=\\\"format-detection\\\" content=\\\"email=no\\\"/><meta name=\\\"f...\",\"size\":11653,\"text\":\"[image: Google]\\nIntegromat was granted access to your Google account\\n\\n\\nzezutomtest@gmail.com\\n\\nIf you...\",\"folder\":\"INBOX...\",\"headers\":{\"to\":[\"zezutomtest@gmail.com...\"],\"date\":[\"Thu, 16 Jun 2022 05:30:47 GMT...\"],\"from\":[\"Google <no-reply@accounts.google.com>...\"],\"subject\":[\"Security alert...\"],\"arc-seal\":[\"i=1; a=rsa-sha256; t=1655357449; cv=none;\\r\\n        d=google.com; s=arc-20160816;\\r\\n        b=X0aXWylR...\"],\"received\":[\"by 2002:adf:bac6:0:0:0:0:0 with SMTP id w6csp943643wrg;\\r\\n        Wed, 15 Jun 2022 22:30:49 -0700 (PD...\"],\"message-id\":[\"<mRbv5LjNA4lmjqXK9Krz2w@notifications.google.com>...\"],\"x-received\":[\"by 2002:a05:6638:238b:b0:332:2816:62f2 with SMTP id q11-20020a056638238b00b00332281662f2mr1796744jat...\"],\"feedback-id\":[\"127:account-notifier...\"],\"return-path\":[\"<3CMCqYggTC0845-8v62Frtt5B4A9.x55x2v.t53GvGBA53Av9Ax3rz2.t53@gaia.bounces.google.com>...\"],\"content-type\":[\"multipart/alternative; boundary=\\\"000000000000b426c805e189f00c\\\"...\"],\"delivered-to\":[\"zezutomtest@gmail.com...\"],\"mime-version\":[\"1.0...\"],\"received-spf\":[\"pass (google.com: domain of 3cmcqyggtc0845-8v62frtt5b4a9.x55x2v.t53gvgba53av9ax3rz2.t53@gaia.bounces...\"],\"dkim-signature\":[\"v=1; a=rsa-sha256; c=relaxed/relaxed;\\r\\n        d=accounts.google.com; s=20210112;\\r\\n        h=mime-ve...\"],\"x-notifications\":[\"2405aa6fec900000...\"],\"x-gm-message-state\":[\"AJIora/CK9n1hyhyd6MLkMTK4+ITGD6kLvxaLn2Gfmvuaen9P0Ce70B6\\r\\n\\tA2AjW0xsywfU2ChuLiH0vUrDhCtNR9KmQPxI2e0Z7...\"],\"x-google-smtp-source\":[\"AGRyM1vhEXen2aoQnLOAon8Xt5WwvZEWIxCfxnrAywSrHr1N+5I+wFbOUl0ck35cMprK1BHgyyVTwf6twCWfcqzfRt4QXA==...\"],\"arc-message-signature\":[\"i=1; a=rsa-sha256; c=relaxed/relaxed; d=google.com; s=arc-20160816;\\r\\n        h=to:from:subject:messa...\"],\"authentication-results\":[\"mx.google.com;\\r\\n       dkim=pass header.i=@accounts.google.com header.s=20210112 header.b=\\\"JFQ/XhX1\\\"...\"],\"x-google-dkim-signature\":[\"v=1; a=rsa-sha256; c=relaxed/relaxed;\\r\\n        d=1e100.net; s=20210112;\\r\\n        h=x-gm-message-stat...\"],\"arc-authentication-results\":[\"i=1; mx.google.com;\\r\\n       dkim=pass header.i=@accounts.google.com header.s=20210112 header.b=\\\"JFQ/...\"],\"x-account-notification-type\":[\"127...\"],\"x-notifications-bounce-info\":[\"AXvZQxeaYXK-gzXtXDWvZwxWne4tI1IZUILcJX7_ZPLSX-ZBfU8npuyhRxeWzktUxw0Jra3nyr-p0u1nkj8CMEGuf9Zz6b06fOeq...\"]},\"subject\":\"Security alert...\",\"attachments\":[],\"headersList\":[{\"key\":\"content-type...\",\"value\":[\"multipart/alternative; boundary=\\\"000000000000b426c805e189f00c\\\"...\"]}]}}},\"scenario\":{\"dlq\":false,\"dataloss\":false,\"maxErrors\":3,\"autoCommit\":true,\"roundtrips\":1,\"sequential\":false,\"confidential\":false,\"autoCommitTriggerLast\":true}}},\"scheduling\":{\"type\":\"indefinitely\",\"interval\":900},\"concept\":false,\"idSequence\":7,\"created\":\"2022-06-17T05:24:47.776Z\",\"last_edit\":\"2022-07-10T19:09:18.080Z\"}}"
}
```



