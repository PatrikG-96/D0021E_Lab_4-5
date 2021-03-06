package Sim.events;

// This class holds information about the simulation entity (like e.g. a node, switch or router) that triggered
// the even (like sending a message,timer event etc.), the target (receiver of the event) and information in the event
// Finally the time (simSlot) is the time when the target should have the event. 

import Sim.SimEnt;
import Sim.events.Event;

public class EventHandle<SimTimeSlot> {
	public final SimEnt _registrator, _target;
	public final Event _event;
	public final SimTimeSlot _simSlot;
	
	public EventHandle(SimEnt registrator, SimEnt target, Event event, SimTimeSlot simSlot)
	{
		_registrator = registrator;
		_target = target;
		_event = event;
		_simSlot = simSlot;
	}
}
