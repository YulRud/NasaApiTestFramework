package com.yulrud.nasaapi;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Reporter;

public class TestLogger {

    public static final Logger LOGGER = LogManager.getLogger(TestLogger.class.getName());

    public static void log(String info){
        LOGGER.info(info);
        Reporter.log(info);
    }

    private TestLogger() {
    }
}
