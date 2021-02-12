package construction;

import application.events.GridChangedEvent;
import application.events.GridEnergizedEvent;
import application.events.GridFlowEventManager;

import java.util.ArrayList;
import java.util.List;

public class PropertiesManager {
    //private static GridFlowEventManager gridFlowEventManager = new GridFlowEventManager();

    private static List<PropertiesObserver> observers = new ArrayList<>();

    public static void attach(PropertiesObserver po) {
        observers.add(po);
    }

    public static void detach(PropertiesObserver po) {
        observers.remove(po);
    }

    // Could take a parameter of some of the properties
    public static void notifyObservers(PropertiesData PD) {
        for (PropertiesObserver po : observers){
            po.updateProperties(PD);
        }
    }

    // Asynchronous rendering of grid when current components are updated
//    public static void sendGridEvent() {
//        gridFlowEventManager.sendEvent(new GridEnergizedEvent());
//    }

}
