package com.brnx.coreservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private Long id;
    private String name;
    private int groupId;
    private String email;

}
