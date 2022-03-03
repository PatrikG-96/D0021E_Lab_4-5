package Sim;

import Sim.events.Event;


// Basically same as Node but you don't set the address beforehand, it is assigned by the NetworkRouter
public class NetworkNode extends Node{


    public NetworkNode() {
        super(-1, -1);
    }

    public void setNetworkAddr(int networkID) {
        getAddr().setNetworkId(networkID);
    }

    public void setNodeAddr(int nodeAddr) {
        getAddr().setNodeId(nodeAddr);
    }



    public void setAddr(NetworkAddr addr) {
        getAddr().setNetworkId(addr.networkId());
        getAddr().setNodeId(addr.nodeId());
    }

    @Override
    public void recv(SimEnt source, Event event) {
        if (getAddr().networkId() == -1) {
            System.out.println("Network address not set");
            return;
        }
        super.recv(source, event);
    }
}
