package Sim;

import Sim.events.BindingUpdate;
import Sim.events.Event;
import Sim.events.MoveTimerEvent;
import Sim.events.NodeMoveEvent;

public class MobileNetworkNode extends NetworkNode {

    // Address to router that it first connected to
    private NetworkAddr _homeAgent;
    // The address it has in the home network, may be obsolete as of OSPF implementation
    private NetworkAddr _homeAddr;

    // Trigger a MoveTimerEvent that indicates node will move to "destination" router after a delay
    public void moveNode(NetworkRouter destination, double delay) {
        send(this, new MoveTimerEvent(destination), delay);
    }

    public void setHomeAddr(NetworkAddr home) {
        _homeAddr = home;
    }

    public NetworkAddr getHomeAddress() {
        return _homeAddr;
    }

    public void setHomeAgent(NetworkAddr ha) {
        _homeAgent = ha;
    }

    public NetworkAddr getHomeAgent() {
        return _homeAgent;
    }

    @Override
    public void recv(SimEnt src, Event ev) {
        super.recv(src, ev);

        if (ev instanceof MoveTimerEvent) {
            MoveTimerEvent event = (MoveTimerEvent) ev;

            // Notify current router of intention of moving, also will perform the moving
            send(getPeer(), new NodeMoveEvent(this, event._dest), 0);

            // Send binding update to home agent
            send(getPeer(), new BindingUpdate(_homeAgent, _homeAddr, getAddr()), 0);
        }

    }



}
