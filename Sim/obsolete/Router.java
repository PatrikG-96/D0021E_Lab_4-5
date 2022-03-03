package Sim.obsolete;

// This class implements a simple router

import Sim.*;
import Sim.events.Event;
import Sim.events.Message;

public class Router extends SimEnt {


	private RouteTableEntry[] _routingTable;
	private int _interfaces;
	private int _now=0;
	private InterfaceChanges _changes;
	// When created, number of interfaces are defined
	
	Router(int interfaces)
	{
		_routingTable = new RouteTableEntry[interfaces];
		_interfaces=interfaces;
		_changes = new InterfaceChanges(interfaces);
	}

	// This method connects links to the router and also informs the 
	// router of the host connects to the other end of the link
	
	public void connectInterface(int interfaceNumber, SimEnt link, SimEnt node)
	{
		if (interfaceNumber<_interfaces)
		{
			_routingTable[interfaceNumber] = new RouteTableEntry(link, node);
		}
		else
			System.out.println("Trying to connect to port not in router");
		
		((Link) link).setConnector(this);
	}

	// This method searches for an entry in the routing table that matches
	// the network number in the destination field of a messages. The link
	// represents that network number is returned
	
	private SimEnt getInterface(int networkAddress)
	{
		SimEnt routerInterface=null;
		for(int i=0; i<_interfaces; i++)
			if (_routingTable[i] != null)
			{
				if (((Node) _routingTable[i].node()).getAddr().networkId() == networkAddress)
				{
					routerInterface = _routingTable[i].link();
				}
			}
		return routerInterface;
	}
	
	public int getNodeInterfaceNumber(int networkID) {

		for(int i=0; i<_interfaces; i++)
			if (_routingTable[i] != null)
			{
				if (((Node) _routingTable[i].node()).getAddr().networkId() == networkID)
				{
					return i;
				}
			}
		return -1;
	}


	// When messages are received at the router this method is called

	public Boolean[] activeInterfaces() {
		Boolean[] active = new Boolean[_interfaces];
		for (int i = 0; i < _routingTable.length; i++) {
			if (_routingTable[i] != null) {
				active[i] = true;
			}
			else {
				active[i] = false;
			}
		}
		return active;
	}

	public void recv(SimEnt source, Event event)
	{
		if (event instanceof Message)
		{
			// Router interface of the sender node
			int send_interface = getNodeInterfaceNumber(((Message) event).source().networkId());
			// Router interface of the destination node
			int dest_interface = getNodeInterfaceNumber(((Message)event).destination().networkId());

			System.out.println("Router handles packet with seq: " + ((Message) event).seq()+" from node: "+((Message) event).source().networkId()+"." + ((Message) event).source().nodeId() );
			SimEnt sendNext = getInterface(((Message) event).destination().networkId());
			System.out.println("Router sends to node: " + ((Message) event).destination().networkId()+"." + ((Message) event).destination().nodeId()
			 				   + " on interface "+ dest_interface);
			send (sendNext, event, _now);

			// Check if there is a change in the destination interface that this interface hasnt received.
			if (_changes.changeExists(send_interface, dest_interface)) {

				// There was a change in the destination interface, get it
				InterfaceChanges.ChangeEvent change = _changes.getChange(send_interface, dest_interface);
				// Update the sender node with the fact that the destination node has changed interface
				send(source, new NodeInterfaceUpdate(change.node.getAddr(), change.from, change.to), _now);
				// Remove the change, only notify sender one
				_changes.removeChange(send_interface, dest_interface);

			}


		}
		if (event instanceof NodeMoveEvent) {

			NodeMoveEvent n = (NodeMoveEvent) event;

			if (_routingTable[n.getNewInterface()] == null) {

				// Get the current interface number of this node
				int old_interface = getNodeInterfaceNumber(n.getSrc().getAddr().networkId());
				// Create a new table entry at the destination interface
				_routingTable[n.getNewInterface()] = new RouteTableEntry(n.getSrc().getPeer(), n.getSrc());
				// Remove the old table entry
				_routingTable[old_interface] = null;
				System.out.println("Node " + n.getSrc().getAddr().networkId() + "." + n.getSrc().getAddr().nodeId() +" moved from interface " + old_interface +
						" to interface " + n.getNewInterface()+ " at time: " + SimEngine.getTime());
				// Log the change in interface
				_changes.addChange(n.getSrc(), old_interface, n.getNewInterface(), activeInterfaces());
			}
			else {
				System.out.println("Moving failed");
			}
		}
	}
}
