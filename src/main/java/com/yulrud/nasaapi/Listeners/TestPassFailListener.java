package com.yulrud.nasaapi.Listeners;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import static com.yulrud.nasaapi.TestLogger.LOGGER;

public class TestPassFailListener extends TestListenerAdapter {
    @Override
    public void onTestFailure(ITestResult tr) {
        LOGGER.info("T E S T   S C E N A R I O   F A I L E D:  " + tr.getName());
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        LOGGER.info("T E S T   S C E N A R I O   S K I P P E D:  " + tr.getName());
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        LOGGER.info("T E S T   S C E N A R I O   S U C C E S S:  " + tr.getName());
    }
}
