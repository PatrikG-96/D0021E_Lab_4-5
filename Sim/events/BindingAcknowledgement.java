package Sim.events;

import Sim.NetworkAddr;
import Sim.SimEnt;
import Sim.events.Message;

public class BindingAcknowledgement extends Message {


    public BindingAcknowledgement(NetworkAddr from, NetworkAddr to, int seq) {
        super(from, to, seq);
    }

    @Override
    public void entering(SimEnt locale) {

    }
}
