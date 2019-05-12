package com.hanayue.service.location.model;

import lombok.Data;

@Data
public class City {
    private int id;
    private String name;
    private int pid;
    private String value;
    private int isLeaf;
}
