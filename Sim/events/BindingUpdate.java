package Sim.events;

import Sim.NetworkAddr;
import Sim.SimEnt;
import Sim.events.Message;

public class BindingUpdate extends Message {

    public NetworkAddr _homeAddr;

    public BindingUpdate(NetworkAddr dest, NetworkAddr homeAddr, NetworkAddr coa) {
        super(coa, dest, 0);
        _homeAddr = homeAddr;
    }

    @Override
    public void entering(SimEnt locale) {

    }
}
