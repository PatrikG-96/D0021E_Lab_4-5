package Sim.obsolete;

import Sim.events.Event;
import Sim.SimEnt;

public class MoveLater implements Event {

    private int new_interface;

    public MoveLater(int new_interface) {
        this.new_interface = new_interface;
    }

    public int getNewInterface() {
        return new_interface;
    }

    @Override
    public void entering(SimEnt locale) {

    }
}
