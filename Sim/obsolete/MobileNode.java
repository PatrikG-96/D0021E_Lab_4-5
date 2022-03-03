package Sim.obsolete;

import Sim.events.Event;
import Sim.Node;
import Sim.SimEnt;

public class MobileNode extends Node {

    public MobileNode(int network, int node) {
        super(network, node);
    }

    public void moveNode(int new_interface, double delay) {
        send(this, new MoveLater(new_interface), delay);
    }

    @Override
    public void recv(SimEnt src, Event ev) {
        super.recv(src, ev);

        if (ev instanceof MoveLater) {
            send(getPeer(), new NodeMoveEvent(this, ((MoveLater) ev).getNewInterface()), 0);
        }

    }
}
