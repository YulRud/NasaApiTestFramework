package com.yulrud.nasaapi.APIRequestSteps;

import com.yulrud.nasaapi.Utils.ConfigFileReader;

public class BaseRequestSteps {
    protected static final String key = new ConfigFileReader().getKey();
}
