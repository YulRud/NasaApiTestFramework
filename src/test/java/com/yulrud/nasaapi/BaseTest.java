package com.yulrud.nasaapi;

import com.yulrud.nasaapi.Utils.ConfigFileReader;
import io.qameta.allure.Description;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.apache.log4j.BasicConfigurator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

public abstract class BaseTest {
    @BeforeClass
    public void init() {
        BasicConfigurator.configure();
        RestAssured.baseURI = new ConfigFileReader().getApplicationBaseUrl();
    }

    @BeforeTest
    public void setFilter(){
        RestAssured.filters(new AllureRestAssured());
    }
}
