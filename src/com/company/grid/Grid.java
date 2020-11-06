package com.company.grid;

import com.company.components.Component;
import com.company.components.source.Source;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    private List<Component> components;

    public Grid() {
        components = new ArrayList<>();
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public List<Source> getSources() {
        return List.of(); //nothing
    }


}
