package Sim.events;

import Sim.MobileNetworkNode;
import Sim.NetworkRouter;
import Sim.SimEnt;
import Sim.events.Event;

public class NodeMoveEvent implements Event {

    public NetworkRouter _destination;
    public MobileNetworkNode _src;

    public NodeMoveEvent(MobileNetworkNode src, NetworkRouter destination) {
        _destination = destination;
        _src = src;
    }

    @Override
    public void entering(SimEnt locale) {

    }
}
