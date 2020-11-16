package main;

import model.components.*;
import model.geometry.Point;

import java.util.List;

public class DevUtils {

    public static List<Component> createTestComponents() {

        // Components
        Wire w4 = new Wire("w4", new Point(1, 3));
        Wire w5 = new Wire("w5", new Point(-1, 3));
        Breaker dd3 = new Breaker("dd3",
                new Point(2, 2),
                Voltage.KV12, false);
        Breaker dd5 = new Breaker("dd5",
                new Point(1, 2),
                Voltage.KV12, false);
        Breaker dd7 = new Breaker("dd7",
                new Point(0, 2),
                Voltage.KV12, false);
        Breaker dd8 = new Breaker("dd8",
                new Point(-1, 2),
                Voltage.KV12, false);
        Breaker dd9 = new Breaker("dd9",
                new Point(-2, 2),
                Voltage.KV12, false);
        Wire w1 = new Wire("w1", new Point(0, 1));
        Switch dd1main = new Switch("dd1 Main", Point.origin(), true);
        Wire w2 = new Wire("w2", new Point(0, -1));
        Transformer dd1 = new Transformer("xform-dd1", new Point(0, -2));
        Wire w3 = new Wire("w3", new Point(0, -3));
        Switch dd105 = new Switch("dd105", new Point(1, -2), true);
        PowerSource p1 = new PowerSource("p1", new Point(0, -4), true);

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
