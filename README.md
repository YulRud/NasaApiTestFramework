# NasaApiTestFramework
This is an API project,
Here I get, download and compare Mars photos made with different cameras using free nasa api (baseUrl=https://api.nasa.gov)
Technologies: 
TestNG, RestAssured, Jackson for tests;
AllureReport and TestNG report for reporting; 
Log4j, testng.Reporter for logging; 

also I rolled out the project on my local Jenkins with AllureReport, TestNG Report and custom HTML report

Prerequisites for project:
1. There is maven 3.6.1 or higher installed
2. There is allure 2.13.8 or higher installed

To start the project:
1. to download a project:
git clone https://github.com/YulRud/NasaApiTestFramework.git
2. To start testing:
mvn clean test
3. To start allure report with results:
mvn allure:serve

TestNG standard report: target/surefire-reports/index.html
Custom html report: target/surefire-reports/custom-report.html

