package Sim.obsolete;

import Sim.NetworkAddr;
import Sim.SimEnt;
import Sim.events.Event;
import Sim.events.LostMessage;

import java.util.Stack;

public class RouteResponse implements Event {

    NetworkAddr _origin;
    NetworkAddr _missingAddr;
    int _cost;
    Stack<Integer> _path;
    LostMessage _lostMessage;

    RouteResponse(NetworkAddr missingAddr, NetworkAddr origin, Stack path, int cost, LostMessage lostMessage) {
        _cost = cost;
        _origin = origin;
        _missingAddr = missingAddr;
        _path = path;
        _lostMessage = lostMessage;
    }


    public Stack getPath() {
        return _path;
    }

    public int pathLength() {
        return _path.size();
    }

    @Override
    public void entering(SimEnt locale) {

    }
}
