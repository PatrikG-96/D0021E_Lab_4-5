package Sim.events;

import Sim.events.Message;

public class LostMessage {

    public boolean _hasBeenSent;
    public Message _msg;

    public LostMessage(Message msg) {
        _msg = msg;
        _hasBeenSent = false;
    }

}
