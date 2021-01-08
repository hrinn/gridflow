package domain.components;

import com.fasterxml.jackson.databind.JsonNode;
import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

import java.util.UUID;

public class Switch extends Closeable {


    public Switch(String name, Point position, boolean closedByDefault) {
        super(name, position, closedByDefault);
        createComponentIcon();
    }

    public Switch(JsonNode node) {
        super(UUID.fromString(node.get("id").asText()), node.get("name").asText(),
                Point.fromString(node.get("pos").asText()), node.get("angle").asDouble(),
                node.get("closedByDefault").asBoolean(), node.get("closed").asBoolean());
        createComponentIcon();
    }

    private void createComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getSwitchIcon(getPosition(), isClosed(), isClosedByDefault());
        icon.setDeviceEnergyStates(false, false);
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setAngle(getAngle(), getPosition());
        setComponentIcon(icon);
    }

    @Override
    public void updateComponentIcon() {
        DeviceIcon icon = (DeviceIcon) getComponentIcon();
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
    }

    @Override
    public void toggle() {
        toggleClosed();
        createComponentIcon();
    }
}
