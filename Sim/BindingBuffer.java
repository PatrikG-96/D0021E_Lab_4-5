package Sim;

import Sim.events.Message;

import java.util.concurrent.ArrayBlockingQueue;

public class BindingBuffer {


    boolean _bindingInProgress;
    ArrayBlockingQueue<Message> _msgBuffer;

    BindingBuffer(int max) {
        _msgBuffer = new ArrayBlockingQueue<>(max);
        _bindingInProgress = false;
    }
}
