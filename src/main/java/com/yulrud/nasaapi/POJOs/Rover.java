package com.yulrud.nasaapi.POJOs;

import lombok.Data;

@Data
public class Rover {
    private int id;
    private String name;
    private String landing_date;
    private String launch_date;
    private String status;
}
