package domain;

import domain.components.Component;
import domain.components.Source;
import domain.components.Wire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Grid {

    private final List<Component> components;

    public Grid() {
        components = new ArrayList<>();
    }

    public List<Component> getComponents() {
        return components;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void addComponents(Component... components) {
        this.components.addAll(Arrays.asList(components));
    }

    public void deleteComponent(String ID) {
        for (Component component : components) {
            if (component.getId().toString().equals(ID)) {
                try {
                    component.delete();
                    components.remove(component);
                } catch (UnsupportedOperationException e) {
                    System.out.println("Cannot delete Wire: " + component.getId());
                }
                return;
            }
        }
    }

    public void loadComponents(List<Component> components) {
        for (Component component : components) addComponent(component);
    }

    public List<Wire> getWires() {
        return components.stream()
                .filter(component -> component instanceof Wire)
                .map(component -> (Wire)component)
                .collect(Collectors.toList());
    }

    public List<Source> getSources() {
        return components.stream()
                .filter(component -> component instanceof Source)
                .map(component -> (Source)component)
                .collect(Collectors.toList());
    }

    public Component getComponent(String id) {
        return components.stream()
                .filter(comp -> comp.getId().toString().equals(id))
                .findFirst().orElse(null);
    }

}
