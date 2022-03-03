package Sim;
import Sim.events.Event;
import Sim.events.Message;

import java.util.concurrent.ThreadLocalRandom;

public class LossyLink extends Link{

	private int _now=0;
	private double _delay;
	private double _loss_rate;
	private double _jitter;

	private int id;
	private int packages_lost;

	public LossyLink(int delay, double jitter, double loss_rate, int id)
	{
		super();
		this.id = id;
		this._delay = delay;
		this._jitter = jitter;
		this._loss_rate = loss_rate;
		this.packages_lost = 0;
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
		if (ev instanceof Message)
		{

			double loss_test = ThreadLocalRandom.current().nextDouble(0, 1);

			if (loss_test < _loss_rate) {
				this.packages_lost++;
				System.out.println("Package " + ((Message) ev).seq() +" lost. This link has lost " + this.packages_lost + " packages.");
				return;
			}

			double next_delay = _delay + ThreadLocalRandom.current().nextDouble(0,2*_jitter);
			next_delay = Math.round(next_delay * 100) / 100;
			System.out.println("Message " + ((Message) ev).seq() +  " took " + next_delay + " time units to transit");
			if (src == _connectorA)
			{
				send(_connectorB, ev, next_delay);
			}
			else
			{
				send(_connectorA, ev, next_delay);
			}
		}
	}

/*
	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof Message)
		{

			int loss_test = (new Random()).nextInt(101);
			if (loss_test < _loss_rate) {
				this.packages_lost++;
				System.out.println("Package " + ((Message) ev).seq() +" lost. Link " + this.id + " has lost " + this.packages_lost + " packages.");
				return;
			}

			int prev_delay = (int)(SimEngine.getTime() - ((Message) ev).getTimestamp());
			int next_delay = ThreadLocalRandom.current().nextInt(0, (int)_delay);

			this._jitter = jitter_rfc1889(prev_delay, next_delay, _jitter);
			System.out.println("Link " + this.id + " jitter: " + this._jitter);
			if (src == _connectorA)
			{
				send(_connectorB, ev, next_delay);
			}
			else
			{
				send(_connectorA, ev, next_delay);
			}
		}
	}

	public double jitter_rfc1889(int prev, int next, double jitter) {
		int d = Math.abs(next - prev);
		return jitter+ ((1./16.) * ((double) d - jitter));
	}
*/
}
