package Sim.events;

import Sim.NetworkRouter;
import Sim.SimEnt;
import Sim.events.Event;

public class MoveTimerEvent implements Event {

    public NetworkRouter _dest;

    public MoveTimerEvent(NetworkRouter dest) {
        _dest = dest;
    }

    @Override
    public void entering(SimEnt locale) {

    }
}
