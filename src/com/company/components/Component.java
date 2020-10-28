package com.company.components;

import java.util.UUID;

public class Component {

    private UUID id;
    private String name;

    public Component(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

}
