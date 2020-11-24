package main;

import model.components.*;
import model.geometry.Point;

import java.util.List;

public class DevUtils {

    public static List<Component> createTestComponents() {

        // Drop Depot Layer by layer
        // out is down, in is up

        // 70KV breakers
        Wire w3 = new Wire("", canvasPos(6,-8));
        Wire w5 = new Wire("", canvasPos(3,-8));
        Wire w7 = new Wire("", canvasPos(0,-8));
        Wire w8 = new Wire("", canvasPos(-3,-8));
        Wire w9 = new Wire("", canvasPos(-6,-8));
        Breaker dd3 = new Breaker("DD3",
                canvasPos(6, -4),
                Voltage.KV12, false);
        Breaker dd5 = new Breaker("DD5",
                canvasPos(3, -4),
                Voltage.KV12, false);
        Breaker dd7 = new Breaker("DD7",
                canvasPos(0, -4),
                Voltage.KV12, false);
        Breaker dd8 = new Breaker("DD8",
                canvasPos(-3, -4),
                Voltage.KV12, false);
        Breaker dd9 = new Breaker("DD9",
                canvasPos(-6, -4),
                Voltage.KV12, false);
        Wire bbus = new Wire("", canvasPos(6, -4), canvasPos(-6, -4));
        dd3.connectInWire(bbus);
        dd5.connectInWire(bbus);
        dd7.connectInWire(bbus);
        dd8.connectInWire(bbus);
        dd9.connectInWire(bbus);
        dd3.connectOutWire(w3);
        dd5.connectOutWire(w5);
        dd7.connectOutWire(w7);
        dd8.connectOutWire(w8);
        dd9.connectOutWire(w9);
        w3.connect(dd3);
        w5.connect(dd5);
        w7.connect(dd7);
        w8.connect(dd8);
        w9.connect(dd9);
        bbus.setConnections(List.of(dd3, dd5, dd7, dd8, dd9));

        // DD1main
        Breaker dd1main = new Breaker("DD1 Main", canvasPos(0, 0), Voltage.KV12, true);
        Wire dd1mw = new Wire("", canvasPos(0,0));
        dd1main.connectOutWire(bbus);
        dd1main.connectInWire(dd1mw);
        dd1mw.connect(dd1main);

        //Transformer
        Transformer xdd1 = new Transformer("Transformer DD1", canvasPos(0, 3));
        Wire xw = new Wire("", canvasPos(0, 3), canvasPos(2, 3));
        xdd1.connectInWire(xw);
        xdd1.connectOutWire(dd1mw);
        xw.connect(xdd1);

        // S switch
        Switch dd101 = new Switch("DD-101", canvasPos(0, 6), false);
        dd101.connectOutWire(xw);
        Wire dd101w = new Wire("", canvasPos(0, 6));
        dd101.connectInWire(dd101w);
        dd101w.connect(dd101);

        // Side route switch
        Switch dd105 = new Switch("DD-105", canvasPos(2, 9), true);
        Wire dd105bw = new Wire("", canvasPos(2, 3), canvasPos(2, 6));
        dd105bw.connect(xw);
        xw.connect(dd105bw);
        dd105bw.connect(dd105);
        dd105.connectOutWire(dd105bw);
        Wire dd105aw = new Wire("", canvasPos(2, 9), canvasPos(2, 12));
        dd105aw.connect(dd105);
        dd105.connectInWire(dd105aw);

        // dd1
        Breaker dd1 = new Breaker("DD-1", canvasPos(0, 9), Voltage.KV70, true);
        dd1.connectOutWire(dd101w);
        Wire dd1w = new Wire("", canvasPos(0, 9));
        dd1.connectInWire(dd1w);
        dd1w.connect(dd1);

        // dd103
        Switch dd103 = new Switch("DD-103", canvasPos(0, 12), false);
        dd103.connectOutWire(dd1w);
        Wire nrw = new Wire("", canvasPos(0, 12), canvasPos(2, 12));
        dd103.connectInWire(nrw);
        nrw.setConnections(List.of(dd103, dd105aw));
        dd105aw.connect(nrw);

        // power source
        PowerSource source = new PowerSource("A", canvasPos(0, 15), true);
        source.connectWire(nrw);

        return List.of(w3, w5, w7, w8, w9, dd3, dd5, dd7, dd8, dd9, bbus, dd1main, dd1mw, xdd1, xw, dd101w,
                dd101, dd105, dd105bw, dd105aw, dd1, dd1w, dd103, nrw, source);

        // Return components
    }

    private static Point canvasPos(int x, int y) {
        return new Point(x, -y).scale(20).translate(6000, 3000);
    }
}
