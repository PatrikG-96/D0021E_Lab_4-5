package Sim;

import Sim.events.Event;
import Sim.events.Message;

public class Sink extends Node{

    private Logger log;
    private int messages_recieved = 0;

    public Sink(int network, int node) {
        super(network, node);
        this.log = Logger.get();
    }

    @Override
    public void recv(SimEnt src, Event ev) {

        if (ev instanceof Message) {

            log.log(Logger.INFO,"Sink recieved message with seq= '" + ((Message) ev).seq() + "'.");
            this.messages_recieved++;
            log.log(Logger.INFO,"Sink has recieved '" + messages_recieved +"' message so far.");
        }
    }
}
