package Sim.obsolete;

import Sim.events.Event;
import Sim.Node;
import Sim.SimEnt;

public class NodeMoveEvent implements Event {

    private Node _src;
    private int new_interface;

    public NodeMoveEvent(Node src, int new_interface) {
        _src = src;
        this.new_interface = new_interface;
    }

    public Node getSrc() {
        return this._src;
    }

    public int getNewInterface() {
        return this.new_interface;
    }

    @Override
    public void entering(SimEnt locale) {

    }
}
