package domain.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

public class Breaker extends Closeable {

    private Voltage voltage;

    public Breaker(String name, Point position, Voltage voltage, boolean closedByDefault) {
        super(name, position, closedByDefault);
        this.voltage = voltage;
        createComponentIcon();
    }

    private void createComponentIcon() {
        DeviceIcon icon;
        if (voltage == Voltage.KV12) {
            icon = ComponentIconCreator.get12KVBreakerIcon(getPosition(), isClosed(), isClosedByDefault());
        } else {
            icon = ComponentIconCreator.get70KVBreakerIcon(getPosition(), isClosed(), isClosedByDefault());
        }
        icon.setComponentName(getName());
        icon.setDeviceEnergyStates(false, false);
        icon.setComponentIconID(getId().toString());
        icon.setAngle(getAngle(), getPosition());
        setComponentIcon(icon);
    }

    @Override
    public void updateComponentIcon() {
        DeviceIcon icon = (DeviceIcon)getComponentIcon();
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
    }

    public Voltage getVoltage() {
        return voltage;
    }

    @Override
    public ObjectNode getJSONObject(ObjectMapper mapper) {
        ObjectNode breaker = super.getJSONObject(mapper);
        breaker.put("voltage", voltage.toString());
        return breaker;
    }

    @Override
    public void toggle() {
        toggleClosed();
        createComponentIcon();
    }
}
