package main;

import model.Grid;
import model.components.*;
import model.geometry.Point;

import java.util.List;

public class DevUtils {

    public static List<Component> createTestComponents(int width, int height) {
        // Points used to place components
        Point center = new Point(width/2, height/2);
        Point xShift = new Point(100, 0);
        Point yShift = new Point(0, 75);

        // Components
        Wire w4 = new Wire("w4", center.add(yShift.multiply(3)).add(xShift));
        Wire w5 = new Wire("w5", center.add(yShift.multiply(3)).add(xShift.negative()));
        Breaker dd3 = new Breaker("dd3",
                center.add(yShift.multiply(2)).add(xShift.multiply(2)),
                Voltage.KV12, false);
        Breaker dd5 = new Breaker("dd5",
                center.add(yShift.multiply(2)).add(xShift),
                Voltage.KV12, false);
        Breaker dd7 = new Breaker("dd7",
                center.add(yShift.multiply(2)),
                Voltage.KV12, false);
        Breaker dd8 = new Breaker("dd8",
                center.add(yShift.multiply(2)).add(xShift.negative()),
                Voltage.KV12, false);
        Breaker dd9 = new Breaker("dd9",
                center.add(yShift.multiply(2)).add(xShift.negative().multiply(2)),
                Voltage.KV12, false);
        Wire w1 = new Wire("w1", center.add(yShift));
        Switch dd1main = new Switch("dd1 Main", center, true);
        Wire w2 = new Wire("w2", center.add(yShift.negative()));
        Transformer dd1 = new Transformer("transformer dd1", center.add(yShift.negative().multiply(2)));
        Wire w3 = new Wire("w3", center.add(yShift.negative().multiply(3)));
        Switch dd105 = new Switch("dd105", center.add(yShift.negative().multiply(2)).add(xShift), true);
        PowerSource p1 = new PowerSource("p1", center.add(yShift.negative().multiply(4)), true);

        // Connections
        dd3.connectOutWire(w4);
        dd5.connectOutWire(w4);
        dd7.connectOutWire(w4);
        dd8.connectOutWire(w5);
        dd9.connectOutWire(w5);
        w4.setConnections(List.of(dd3, dd5, dd7));
        w5.setConnections(List.of(dd8, dd9));

        dd3.connectInWire(w1);
        dd5.connectInWire(w1);
        dd7.connectInWire(w1);
        dd8.connectInWire(w1);
        dd9.connectInWire(w1);
        w1.setConnections(List.of(dd3, dd5, dd7, dd8, dd9, dd1main));

        dd1main.connectInWire(w2);
        dd1main.connectOutWire(w1);
        w2.setConnections(List.of(dd1main, dd1, dd105));
        dd1.connectInWire(w2);
        dd1.connectOutWire(w3);
        dd105.connectInWire(w2);
        dd105.connectOutWire(w3);
        w3.setConnections(List.of(dd1, p1, dd105));
        p1.connectWire(w3);

        // Return components
        return List.of(dd3, dd5, dd7, dd8, dd9, w1, dd1main, w2, dd1, w3, p1, w4, w5, dd105);
    }
}
