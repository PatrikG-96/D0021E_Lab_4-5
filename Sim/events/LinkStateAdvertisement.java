package Sim.events;

import Sim.events.Event;
import Sim.SimEnt;
import Sim.ospf.Graph;

import java.util.ArrayList;

public class LinkStateAdvertisement implements Event {

    private Graph _linkState;
    private ArrayList<Integer> _visitedRouters;
    private int _origin;

    public LinkStateAdvertisement(Graph g, int origin) {
        _linkState = g;
        _origin = origin;
        _visitedRouters = new ArrayList<>();
    }

    public int getOrigin() {
        return _origin;
    }
    public Graph getLinkState() {
        return _linkState;
    }

    public boolean hasVisited(int network) {
        return _visitedRouters.contains(network);
    }

    public void visit(int network) {
        _visitedRouters.add(network);
    }

    @Override
    public void entering(SimEnt locale) {

    }
}
