package com.yulrud.nasaapi.reporting;

import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.yulrud.nasaapi.TestLogger.LOGGER;

public class CustomHTMLReport implements IReporter {
    private static final String TEMPLATE_LOCATION = "src/main/resources/custom-report-template.html";

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

        try {
            // Get content data in TestNG report template file.
            String customReportTemplateStr = this.readReportTemplate();

            // Create custom report title.
            String customReportTitle = this.getCustomReportTitle("Test Results Report");

            // Create test suite summary data.
            String customSuiteSummary = this.getTestSuiteSummary(suites);

            // Create test methods summary data.
            String customTestMethodSummary = this.getTestMethodSummary(suites);

            // Replace report title place holder with custom title.
            customReportTemplateStr = customReportTemplateStr.replaceAll("\\$TestNG_Custom_Report_Title\\$", customReportTitle);

            // Replace test suite place holder with custom test suite summary.
            customReportTemplateStr = customReportTemplateStr.replaceAll("\\$Test_Case_Summary\\$", customSuiteSummary);

            // Replace test methods place holder with custom test method summary.
            customReportTemplateStr = customReportTemplateStr.replaceAll("\\$Test_Case_Detail\\$", customTestMethodSummary);

            // Write created report content to custom-report.html for Jenkins HTML report.
            File targetFile = new File(outputDirectory + "/custom-report.html");
            FileWriter fw = new FileWriter(targetFile);
            fw.write(customReportTemplateStr);
            fw.flush();
            fw.close();

        } catch (Exception ex) {
            LOGGER.error("Error during report generation: " + ex);
            ex.printStackTrace();
        }
    }

    /* Read template content. */
    private String readReportTemplate() {
        StringBuilder template = new StringBuilder();

        try {
            File file = new File(TEMPLATE_LOCATION);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while (line != null) {
                template.append(line);
                line = br.readLine();
            }

        } catch (IOException ex) {
            LOGGER.error("Error during reading template file: " + ex);
            ex.printStackTrace();
        }
        return template.toString();
    }

    /* Build custom report title. */
    private String getCustomReportTitle(String title) {
        return title + " " + this.getDateInStringFormat(new Date());
    }

    /* Build test suite summary data. */
    private String getTestSuiteSummary(List<ISuite> suites) {
        StringBuilder retBuf = new StringBuilder();

        try {
            int summarizedTestCount = 0;
            int summarizedTestPassed = 0;
            int summarizedTestFailed = 0;
            int summarizedTestSkipped = 0;

            long timeStart = Long.MAX_VALUE;
            long timeEnd = Long.MIN_VALUE;

            for (ISuite tempSuite : suites) {
                retBuf.append("<tr><td colspan=8><center><b>");
                retBuf.append(tempSuite.getName());
                retBuf.append("</b></center></td></tr>");

                Map<String, ISuiteResult> testResults = tempSuite.getResults();

                for (ISuiteResult result : testResults.values()) {

                    retBuf.append("<tr>");

                    ITestContext testObj = result.getTestContext();

                    int totalTestPassed = testObj.getPassedTests().getAllMethods().size();
                    int totalTestSkipped = testObj.getSkippedTests().getAllMethods().size();
                    int totalTestFailed = testObj.getFailedTests().getAllMethods().size();

                    int totalTestCount = totalTestPassed + totalTestSkipped + totalTestFailed;

                    summarizedTestCount += totalTestCount;
                    summarizedTestPassed += totalTestPassed;
                    summarizedTestFailed += totalTestFailed;
                    summarizedTestSkipped += totalTestSkipped;

                    /* Test name. */
                    retBuf.append("<td>");
                    retBuf.append(testObj.getName());
                    retBuf.append("</td>");

                    /* Total method count. */
                    retBuf.append("<td>");
                    retBuf.append(totalTestCount);
                    retBuf.append("</td>");

                    /* Passed method count. */
                    retBuf.append("<td bgcolor=green>");
                    retBuf.append(totalTestPassed);
                    retBuf.append("</td>");

                    /* Skipped method count. */
                    retBuf.append("<td bgcolor=yellow>");
                    retBuf.append(totalTestSkipped);
                    retBuf.append("</td>");

                    /* Failed method count. */
                    retBuf.append("<td bgcolor=red>");
                    retBuf.append(totalTestFailed);
                    retBuf.append("</td>");

                    /* Start Date*/
                    Date startDate = testObj.getStartDate();
                    retBuf.append("<td>");
                    retBuf.append(this.getDateInStringFormat(startDate));
                    retBuf.append("</td>");

                    /* End Date*/
                    Date endDate = testObj.getEndDate();
                    retBuf.append("<td>");
                    retBuf.append(this.getDateInStringFormat(endDate));
                    retBuf.append("</td>");

                    /* Execute Time */
                    long deltaTime = endDate.getTime() - startDate.getTime();
                    String deltaTimeStr = this.convertDeltaTimeToString(deltaTime);
                    retBuf.append("<td>");
                    retBuf.append(deltaTimeStr);
                    retBuf.append("</td>");

                    retBuf.append("</tr>");

                    timeStart = Math.min(testObj.getStartDate().getTime(), timeStart);
                    timeEnd = Math.max(testObj.getEndDate().getTime(), timeEnd);
                }

                /* Total test quantity. */
                retBuf.append("<td>");
                retBuf.append("Total");
                retBuf.append("</td>");

                /* Total method count. */
                retBuf.append("<td>");
                retBuf.append(summarizedTestCount);
                retBuf.append("</td>");

                /* Passed method count. */
                retBuf.append("<td bgcolor=green>");
                retBuf.append(summarizedTestPassed);
                retBuf.append("</td>");

                /* Skipped method count. */
                retBuf.append("<td bgcolor=yellow>");
                retBuf.append(summarizedTestSkipped);
                retBuf.append("</td>");

                /* Failed method count. */
                retBuf.append("<td bgcolor=red>");
                retBuf.append(summarizedTestFailed);
                retBuf.append("</td>");

                retBuf.append("<td>");
                retBuf.append("</td>");
                retBuf.append("<td>");
                retBuf.append("</td>");
                retBuf.append("<td>");
                retBuf.append(this.convertDeltaTimeToString(timeEnd - timeStart));
                retBuf.append("</td>");
                retBuf.append("</tr>");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retBuf.toString();
    }

    /* Get date string format value. */
    private String getDateInStringFormat(Date date) {
        StringBuilder retBuf = new StringBuilder();
        if (date == null) {
            date = new Date();
        }
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
        retBuf.append(df.format(date));
        return retBuf.toString();
    }

    /* Convert long type deltaTime to format HH:mm:ss.SSS. */
    private String convertDeltaTimeToString(long milliseconds) {
        return String.format("%02d:%02d:%02d.%d", TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1),
                milliseconds % TimeUnit.SECONDS.toMillis(1));
    }

    /* Get test method summary info. */
    private String getTestMethodSummary(List<ISuite> suites) {
        StringBuilder retBuf = new StringBuilder();

        try {
            for (ISuite tempSuite : suites) {
                retBuf.append("<tr><td colspan=5><center><b>");
                retBuf.append(tempSuite.getName());
                retBuf.append("</b></center></td></tr>");

                Map<String, ISuiteResult> testResults = tempSuite.getResults();

                for (ISuiteResult result : testResults.values()) {

                    ITestContext testObj = result.getTestContext();

                    String testName = testObj.getName();

                    /* Get failed test method related data. */
                    IResultMap testFailedResult = testObj.getFailedTests();
                    String failedTestMethodInfo = this.getTestMethodReport(testName, testFailedResult, false, false);
                    retBuf.append(failedTestMethodInfo);

                    /* Get skipped test method related data. */
                    IResultMap testSkippedResult = testObj.getSkippedTests();
                    String skippedTestMethodInfo = this.getTestMethodReport(testName, testSkippedResult, false, true);
                    retBuf.append(skippedTestMethodInfo);

                    /* Get passed test method related data. */
                    IResultMap testPassedResult = testObj.getPassedTests();
                    String passedTestMethodInfo = this.getTestMethodReport(testName, testPassedResult, true, false);
                    retBuf.append(passedTestMethodInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retBuf.toString();
    }

    /* Get failed, passed or skipped test methods report. */
    private String getTestMethodReport(String testName, IResultMap testResultMap, boolean passedResult, boolean skippedResult) {
        StringBuilder retStrBuf = new StringBuilder();

        String resultTitle = testName;

        String color;

        if (skippedResult) {
            resultTitle += " - Skipped ";
            color = "yellow";
        } else {
            if (!passedResult) {
                resultTitle += " - Failed ";
                color = "red";
            } else {
                resultTitle += " - Passed ";
                color = "green";
            }
        }

        retStrBuf.append("<tr bgcolor=" + color + "><td colspan=7><center><b>" + resultTitle + "</b></center></td></tr>");

        Set<ITestResult> testResultSet = testResultMap.getAllResults();

        for (ITestResult testResult : testResultSet) {
            String testClassName;
            String testMethodName;
            String startDateStr;
            String executeTimeStr;
            String exceptionMessage = "";

            //Get testClassName
            testClassName = testResult.getTestClass().getName();

            //Get testMethodName
            testMethodName = testResult.getMethod().getMethodName();

            //Get startDateStr
            long startTimeMillis = testResult.getStartMillis();
            startDateStr = this.getDateInStringFormat(new Date(startTimeMillis));

            //Get Execute time.
            long deltaMillis = testResult.getEndMillis() - testResult.getStartMillis();
            executeTimeStr = this.convertDeltaTimeToString(deltaMillis);

            //Get exception message.
            Throwable exception = testResult.getThrowable();
            if (exception != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                exception.printStackTrace(pw);

                exceptionMessage = sw.toString();
            }

            retStrBuf.append("<tr bgcolor=" + color + ">");

            /* Add test class name. */
            retStrBuf.append("<td>");
            retStrBuf.append(testClassName);
            retStrBuf.append("</td>");

            /* Add test method name. */
            retStrBuf.append("<td>");
            retStrBuf.append(testMethodName);
            retStrBuf.append("</td>");

            /* Add start time. */
            retStrBuf.append("<td>");
            retStrBuf.append(startDateStr);
            retStrBuf.append("</td>");

            /* Add execution time. */
            retStrBuf.append("<td>");
            retStrBuf.append(executeTimeStr);
            retStrBuf.append("</td>");

            /* Add exception message. */
            retStrBuf.append("<td>");
            String shortMessage = exceptionMessage.length() > 300 ? exceptionMessage.substring(0, 300) + "..." : exceptionMessage;
            retStrBuf.append(shortMessage);
            retStrBuf.append("</td>");

            retStrBuf.append("</tr>");

        }
        return retStrBuf.toString();
    }
}