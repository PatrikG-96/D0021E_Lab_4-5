package Sim.events;

// This class implements an event that send a Message, currently the only
// fields in the message are who the sender is, the destination and a sequence 
// number

import Sim.NetworkAddr;
import Sim.SimEngine;
import Sim.SimEnt;
import Sim.events.Event;

public class Message implements Event {
	private NetworkAddr _source;
	private NetworkAddr _destination;
	private int _seq=0;
	private double timestamp;
	
	public Message (NetworkAddr from, NetworkAddr to, int seq)
	{
		_source = from;
		_destination = to;
		_seq=seq;
		this.timestamp = SimEngine.getTime();
	}

	public void setSource(NetworkAddr src) {
		_source = src;
	}

	public void setDestination(NetworkAddr dest) {
		_destination = dest;
	}

	public NetworkAddr source()
	{
		return _source; 
	}
	
	public NetworkAddr destination()
	{
		return _destination; 
	}

	public double getTimestamp(){
		return this.timestamp;
	}
	
	public int seq()
	{
		return _seq; 
	}

	public void entering(SimEnt locale)
	{
	}
}
	
