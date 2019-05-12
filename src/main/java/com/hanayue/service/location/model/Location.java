package com.hanayue.service.location.model;

import lombok.Data;

@Data
public class Location {
    private String id;
    private String country;
    private String province;
    private String city;
    private String area;
    private String value;
}
