package Sim.obsolete;

import Sim.NetworkAddr;
import Sim.SimEnt;
import Sim.events.Event;
import Sim.events.LostMessage;

import java.util.Stack;

public class RouteRequest implements Event {

    LostMessage _lostMessage;
    NetworkAddr _missingEntry;
    int _hops;
    NetworkAddr _origin;
    Stack<NetworkAddr> _path;

    public RouteRequest(NetworkAddr missing, NetworkAddr originNetwork, LostMessage lostMessage) {
        _lostMessage = lostMessage;
        _missingEntry = missing;
        _origin = originNetwork;
        _path = new Stack<>();
        _path.push(originNetwork);
    }

    public boolean hasVisited(NetworkAddr currentNetwork) {
        return _path.contains(currentNetwork);
    }

    public void visitNetwork(NetworkAddr currentNetwork) {
        _path.push(currentNetwork);
    }

    public Stack getPath() {
        return _path;
    }

    @Override
    public void entering(SimEnt locale) {

    }
}
