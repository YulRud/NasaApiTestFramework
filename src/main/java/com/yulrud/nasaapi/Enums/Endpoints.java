package com.yulrud.nasaapi.Enums;

public enum Endpoints {
    GET_CURIOSITY_PHOTOS("/mars-photos/api/v1/rovers/curiosity/photos");
    private String url;

    Endpoints(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
