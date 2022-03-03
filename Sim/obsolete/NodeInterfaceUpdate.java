package Sim.obsolete;

import Sim.events.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

public class NodeInterfaceUpdate implements Event {

    private NetworkAddr node;
    private int from;
    private int to;

    NodeInterfaceUpdate(NetworkAddr node, int from, int to) {
        this.node = node;
        this.from = from;
        this.to = to;
    }

    public NetworkAddr getNode() {
        return node;
    }

    public int fromInterface() {
        return from;
    }

    public int toInterface() {
        return to;
    }
    @Override
    public void entering(SimEnt locale) {

    }
}
