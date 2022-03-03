package Sim;

// An example of how to build a topology and starting the simulation engine

public class Run {




	public static void main (String [] args)
	{
		Logger log = Logger.get();
		log.startLogging(Logger.INFO);

		Link link1 = new Link();
		Link link2 = new Link();
		Link link3 = new Link();
		Link link4 = new Link();


		NetworkNode node1 = new NetworkNode();
		NetworkNode node2 = new NetworkNode();
		NetworkNode node3 = new NetworkNode();
		MobileNetworkNode node4 = new MobileNetworkNode();

		node1.setPeer(link1);
		node2.setPeer(link2);
		node3.setPeer(link3);
		node4.setPeer(link4);

		NetworkRouter router = new NetworkRouter(0, 255);
		NetworkRouter router1 = new NetworkRouter(1, 255);
		NetworkRouter router2 = new NetworkRouter(2, 255);
		NetworkRouter router3 = new NetworkRouter(3, 255);
		NetworkRouter router4 = new NetworkRouter(4, 255);
		NetworkRouter router5 = new NetworkRouter(5, 255);
		//router.connectToPort(3, link1, node1);
		//router.connectToPort(4, link2, node2);

		Topology topology = new Topology();
		topology.addRouter(router);
		topology.addRouter(router1);
		topology.addRouter(router2);
		topology.addRouter(router3);
		topology.addRouter(router4);
		topology.addRouter(router5);

		//router2.connectToPort(4, link3, node3);


		//router3.connectToPort(5, link4, node4);

		router.connectRouters(router1);
		//router.connectRouters(router4);
		router1.connectRouters(router2);
		router1.connectRouters(router3);
		router1.connectRouters(router4);
		router3.connectRouters(router4);
		router4.connectRouters(router5);

		topology.initOSPFProtocol();
		//node1.StartSending(node4.getAddr().networkId(), node4.getAddr().nodeId(), 10, 5, 0);
		//node4.moveNode(router2, 25);

		router.connect(link1, node1);

		router2.connect(link4, node4);

		node4.moveNode(router5, 4);
		node4.moveNode(router2, 7);

		node1.StartSending(node4.getAddr().networkId(), node4.getAddr().nodeId(), 10, 1, 0);

		// Start the simulation engine and of we go!
		Thread t=new Thread(SimEngine.instance());
	
		t.start();
		try
		{
			t.join();

		}
		catch (Exception e)
		{
			System.out.println("The motor seems to have a problem, time for service?" + e.getMessage());
		}

		//log.close();
	}
}
