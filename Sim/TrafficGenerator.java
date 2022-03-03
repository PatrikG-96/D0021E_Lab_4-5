package Sim;

import Sim.events.Event;
import Sim.events.Message;

import java.util.ArrayList;

public class TrafficGenerator extends Node {


    private int packages_sent;
    private int _seq;
    private int _destNetwork;
    private int _destHost;

    private Logger log;
    private ArrayList<Double> distribution;

    public TrafficGenerator(int network, int node, ArrayList<Double> distribution) {
        super(network, node);
        this.distribution = distribution;
        this.log = Logger.get();
    }

    public void startSending(int network, int node, int startSeq) {
        packages_sent = 0;
        _destNetwork = network;
        _destHost = node;
        _seq = startSeq;
        send(this, new TimerEvent(), 0);
    }

    @Override
    public void recv(SimEnt src, Event ev) {
        super.recv(src, ev);
        if (ev instanceof TimerEvent) {

            if (packages_sent < distribution.size()) {

                send(this.getPeer(), new Message(getAddr(), new NetworkAddr(_destNetwork, _destHost), _seq), 0);

                double next_msg = distribution.get(packages_sent);
                send(this, new TimerEvent(), next_msg);

                log.log(Logger.DEBUG, "Generator sending message with seq= '" + _seq + "'.");

                packages_sent++;
                _seq ++;
            }
            else {
                log.log(Logger.INFO, "END: Generator sent '"+packages_sent +"' packages.");
            }
        }


    }

}
