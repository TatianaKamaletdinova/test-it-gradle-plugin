# Test IT gradle plugin

Official github Test IT is *[Test IT Test Management System](https://github.com/testit-tms)*.

Plugin does:
1) Create or update autotest (by REST: "/autoTests") from your allure test result 
2) Link to test-case (by REST: "/autoTests/{globalId}/workItems") 
3) Send attachment if it exists (by REST: "Attachments")
4) Create testRun (by REST: "/testRuns") 
5) Send result (by REST: "testRuns/{testRunId}/testResults")

Plugin was written in Kotlin. Gradle use Kotlin DSL.

## Getting Started

### Installation
#### Gradle Users

1. Add this dependency to your project build file:

#### build.gradle
```groovy
plugins {
    id 'ru.kamal.testit' version '0.0.1'
}

testIT {
    projectId = "your projectId"
    configurationId = "your configurationId"
    testITUrl = "your testITUrl"
    privateToken = "your privateToken"
    namespace = "your namespace"
    testRunName = "your testRunName"
    reportDir.set(file("your path to folder allure-result"))
}
```

Fill parameters with your configuration, where:
* `PROJECT_ID` - ID of a project in TMS instance.
    1. Create a project.
    2. Open DevTools > Network.
    3. Go to the project `https://{DOMAIN}/projects/{PROJECT_GLOBAL_ID}/tests`.
    4. GET-request project, Preview tab, copy iID field.

* `CONFIGURATION_ID` - ID of a configuration in TMS instance.
    1. Create a project.
    2. Open DevTools > Network.
    3. Go to the project `https://{DOMAIN}/projects/{PROJECT_GLOBAL_ID}/tests`.
    4. GET-request configurations, Preview tab, copy id field.
* `TEST_IT_URL` - location of the TMS instance.
* `PRIVATE_TOKEN` - API secret key. To do that:
    1. Go to the `https://{DOMAIN}/user-profile` profile.
    2. Copy the API secret key.
* `NAMESPASE` - name of the new name spase.`NAMESPASE` is optional. If it is not provided, it is created automatically.
* `TEST_RUN_NAME` - name of the new test-run.`TEST_RUN_NAME` is optional. If it is not provided, it is created automatically.
* `REPORT_DIR` - path to folder with test report.

2. Press the **Reload All Gradle Projects** button.

### Configuration
* You should to put your file of test report in folder your project. For instance, in '/build/allure-result'.
* Format file should be '.json'.
* It easy do by 'io.qameta.allure-report' plugin for generate file of test report.

###Required fields in the file:
#### test_report_example.json
```json
{
    "name": "test",
    "start": 1672698390761,
    "stop": 1672698402325,
    "status": "passed",
    "attachments": [
        {
            "source": "cc70ea72-0585-4769-935e-5d1a4769c0b0-attachment.png",
            "name": "Test675382mock_step_1.png",
            "type": "image/png"
        },
        {
            "source": "65abf81e-3a7b-4216-8939-269693d1779e-attachment.txt",
            "name": "TestLogcat.txt",
            "type": "text/plain"
        }
    ],
    "historyId": "1e9e8128599ca6f7c2ac1142e7f3070e",
    "labels": [
        {
            "name": "testName",
            "value": "Проверяем корректную загрузку данных на экране"
        }
    ],
    "links": [
        {
            "name": "675382",
            "url": "https://domain/projects/80439/tests/675382",
            "type": "tms"
        }
    ]
}
```
* `links` - It is optional. Link to test-case.
* `labels` - It is optional. If defines custom name for your test. If it is not provided, it get from root field "name".

### Execute Gradle Task
Run the task and look at the logs
```
 gradle initAutoTestsAndReport
```


* If you run gradle task 'gradle tasks' you will see the Test IT task of list tasks task.
<img width="385" alt="Screenshot 2023-01-11 at 23 36 07" src="https://user-images.githubusercontent.com/23185210/211912358-e66531b2-817c-478c-9762-6760577c0389.png">


### Need Gradle support?
Contact me if you need help with Gradle at Telegram [@ITSurgeon](https://t.me/ITSurgeon).

# License

Distributed under the Apache-2.0 License.
