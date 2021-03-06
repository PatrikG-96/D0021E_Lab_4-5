package Sim;

// This class implements a link without any loss, jitter or delay

import Sim.events.*;
import Sim.obsolete.RouteRequest;
import Sim.obsolete.RouteResponse;

public class Link extends SimEnt{
	protected SimEnt _connectorA=null;
	protected SimEnt _connectorB=null;
	private int _now=0;
	
	public Link()
	{
		super();	
	}
	
	// Connects the link to some simulation entity like
	// a node, switch, router etc.
	
	public void setConnector(SimEnt connectTo)
	{
		if (_connectorA == null) 
			_connectorA=connectTo;
		else
			_connectorB=connectTo;
	}

	// Called when a message enters the link
	
	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof Message ||  ev instanceof NodeMoveEvent || ev instanceof BindingUpdate ||
				ev instanceof BindingAcknowledgement || ev instanceof LinkStateAdvertisement)
		{
			//System.out.println("Link recv msg, passes it through");

			if (src == _connectorA)
			{
				send(_connectorB, ev, _now);
			}
			else
			{
				send(_connectorA, ev, _now);
			}
		}
		/*
		if (ev instanceof NodeMoveEvent) {
			if (src == _connectorA)
			{
				send(_connectorB, ev, _now);
			}
			else
			{
				send(_connectorA, ev, _now);
			}
		}

		if (ev instanceof NodeInterfaceUpdate) {
			if (src == _connectorA)
			{
				send(_connectorB, ev, _now);
			}
			else
			{
				send(_connectorA, ev, _now);
			}
		}

		if (ev instanceof RouteRequest || ev instanceof RouteResponse) {

		}
		*/

	}	
}