package model;

import model.components.Component;
import model.components.sources.Source;

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
