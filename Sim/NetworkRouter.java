package Sim;

import Sim.events.*;
import Sim.events.LinkStateAdvertisement;
import Sim.ospf.OSPFState;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class NetworkRouter extends SimEnt{

    private Logger log = Logger.get();

    // The network ID of this router
    private int _network;

    // OSPF state, link state and shortest path tree etc
    private OSPFState _ospfState;

    // Destination networkID mapped to the next network a message should be forwarded to
    // Value of this map can be used to index the _routerLinks map
    private HashMap<Integer, Integer> _globalRoutingTable;

    // Connected network with ID as key and the link to that network as value
    private HashMap<Integer, Link> _routerLinks;

    // A local routing table binding local addresses to a link to that node
    private HashMap<NetworkAddr, Link> _localRoutingTable;

    // Represents mapping of mobile nodes home address and CoA
    private BindingTable _bindingTable;

    // Queue for assigned node IDs, just pops the lowest number
    private PriorityQueue<Integer> _addressSpace;

    // Address to this specific router, always ends with node ID 1.
    private NetworkAddr _addr;

    private int _now = 0;


    public NetworkRouter(int network, int max_node_id) {
        _ospfState = new OSPFState(network);
        _routerLinks = new HashMap<>();
        _localRoutingTable = new HashMap<>();
        _bindingTable = new BindingTable();
        _network = network;
        _addressSpace = new PriorityQueue<>();
        _addr = new NetworkAddr(network, 1);

        // Fill in the address space according to maximum allowed node ID
        for (int i = 2; i < max_node_id; i++) {
            _addressSpace.add(i);
        }
    }

    public void connect(Link link, NetworkNode node) {

        // Create a new address by combining network ID of this router and the first available node ID
        NetworkAddr assignedAddress = new NetworkAddr(_network, _addressSpace.poll());
        node.setAddr(assignedAddress);

        // Add the new node to the local routing table, its address mapped to the link to that node
        _localRoutingTable.put(assignedAddress, link);

        link.setConnector(this);

        // When connecting a mobile node that does not have a home agent yet, we want to set its home agent
        // to be this router
        if (node instanceof MobileNetworkNode && ((MobileNetworkNode) node).getHomeAgent() == null) {

            homeAgent((MobileNetworkNode) node);
        }

    }

    // Just remove node, set its address to -1.-1 to indicate it doesnt have a valid address, not used yet.
    public void disconnect(NetworkNode node) {
        _localRoutingTable.remove(node.getAddr());
        _addressSpace.add(node.getAddr().nodeId());
        node.setAddr(new NetworkAddr(-1, -1));
    }

    // Set the home agent of a mobile node to be this router
    public void homeAgent(MobileNetworkNode node) {
        node.setHomeAgent(_addr);
        node.setHomeAddr(new NetworkAddr(node.getAddr().networkId(), node.getAddr().nodeId()));

        // When setting the home agent, we create a binding table entry too, to be used for binding updates
        // Initial value is the same address mapped to itself
        _bindingTable.put(node.getHomeAddress(), node.getHomeAddress());

    }

    public void connectRouters(NetworkRouter other) {

        // Is the router already connected?
        if(_routerLinks.containsKey(other._network)) {
            return;
        }

        // Create link and attach this router and the other router
        Link link = new Link();
        link.setConnector(this);
        link.setConnector(other);

        // Map link connecting the routing in the router link tables,
        // key is the networkID, value is the link connecting the networks.
        _routerLinks.put(other._network, link);
        other._routerLinks.put(_network, link);


        // Update the OSPF link state, we have a new connection that we have to take into account
        _ospfState.addConnection(other._network);
        other._ospfState.addConnection(_network); // both routers need updating...

        // To initialize global routing tables, get them every time a new router is connected
        _globalRoutingTable = _ospfState.getGlobalRoutingTable();
        other._globalRoutingTable = other._ospfState.getGlobalRoutingTable();
    }

    // Initialization of setting up the OSPF link states
    public void advertiseState() {
        LinkStateAdvertisement lsa = new LinkStateAdvertisement(_ospfState.getLinkState(), _network);
        lsa.visit(_network); // to avoid infinite loops

        // For the initial advertisement, announce it to all connected routers
        for(Map.Entry entry : _routerLinks.entrySet()) {
            Link link = (Link) entry.getValue();
            send(link, lsa, _now);
        }
    }

    //#########################################//
    //  HANDLER FOR DIFFERENT TYPES OF EVENTS  //
    //#########################################//


    // Handler for Message events
    private void handleMessage(Message msg) {
        NetworkAddr dest = msg.destination();

        log.log(Logger.INFO, self());
        log.log(Logger.INFO, "Received message from " + msg.source() + " to "+ msg.destination());

        // If there is an entry in the binding table for the destination address, it means that the destination
        // is a mobile node, and we may have to translate the address to the mobile nodes CoA
        if (_bindingTable.containsKey(dest)) {

            log.log(Logger.DEBUG, "Found binding table entry for " + dest);

            // BindingBuffers keep an eye on whether or not binding is in progress, and contain
            // a FIFO queue for buffering messages during this time
            BindingBuffer buffer = _bindingTable.getBuffer(dest);

            // Get the CoA of the mobile node
            dest = _bindingTable.get(dest);

            msg.setDestination(dest); //update message destination to CoA

            // BindingBuffer signaled that there is a binding in progress that isn't finished
            if (buffer._bindingInProgress) {

                log.log(Logger.DEBUG, "Binding is currently in progress, buffering messages");

                buffer._msgBuffer.add(msg); // add the message to the buffer
                return;
            }
        }

        // If the destination network is this network, the node must exist on a link to this router
        if (dest.networkId() == _network) {

            // If the node still isnt here, something is wrong
            if (!(_localRoutingTable.containsKey(dest))) {
                log.log(Logger.DEBUG, "Local routing table did not contain an entry for key: " + dest);
                return;
            }

            log.log(Logger.INFO, "Destination node found on current network, forwarding...");

            Link link = _localRoutingTable.get(dest); // Get the link to the node
            send(link, msg, _now);

        }
        else {

            // Router links connect adjacent routers. If there is no entry for the destination entry,
            // that means that we need don't have a direct link to that network. We need to forward it.
            if (!(_routerLinks.containsKey(dest.networkId()))) {

                log.log(Logger.INFO,"No direct link to network found on this interface");

                // The only way forward is to use the global routing table, it is generated using the
                // OSPF link state and tells us the best path to take to get to the destination network
                if(!(_globalRoutingTable.containsKey(dest.networkId()))) {
                    log.log(Logger.DEBUG,"Global routing table has no entry for network: " + dest.networkId());
                    return;
                }

                int next_network = _globalRoutingTable.get(dest.networkId()); // get the next network to visit
                Link link = _routerLinks.get(next_network); // get the link to that network
                send(link, msg, _now);

            }
            else {
                log.log(Logger.INFO, "Found direct link to destination network!");
                send(_routerLinks.get(dest.networkId()), msg, _now);

            }

        }
        log.lineBreak(Logger.INFO);
    }

    // Handle a OSPF link state advertisement message
    private void handleLSA(LinkStateAdvertisement lsa) {
        if(lsa.hasVisited(_network)) {
            return;
        }

        log.log(Logger.DEBUG, self());
        log.log(Logger.DEBUG, "Received LSA from network " + lsa.getOrigin());

        lsa.visit(_network); // this LSA should not be processed here again

        // Combine the link state of the advertisement and this routers state
        _ospfState.mergeLinkStates(lsa.getLinkState());

        // Compute a new global routing table using the generated SPF from step above
        _globalRoutingTable = _ospfState.getGlobalRoutingTable();

        // We forward the LSA to all known neighbor routers. No infinite loops due to the check in the
        // beginning of the function
        for(Map.Entry entry : _routerLinks.entrySet()) {

            log.log(Logger.DEBUG, "Forwarding LSA to network " + entry.getKey());

            Link link = (Link) entry.getValue();
            send(link, lsa, _now);
        }
        log.lineBreak(Logger.DEBUG);
    }


    // Handle a node moving or disconnecting
    private void handleNodeMove(NodeMoveEvent event) {

        log.log(Logger.INFO, self());
        log.log(Logger.INFO, "Node moving from " + _network + " to " + event._destination._network);

        // For some reason I worried about having referenes to the same address everywhere
        NetworkAddr copy = new NetworkAddr(event._src.getAddr().networkId(), event._src.getAddr().nodeId());

        // Node has disconnected from this network, so we need to remove it from the local routing table
        _localRoutingTable.remove(copy);

        // Create a new link, set it as the peer of the MN who sent this message, and connect it to the destination
        // router.
        Link newLink = new Link();
        event._src.setPeer(newLink);
        event._destination.connect(newLink, event._src);

        // To make it possible to buffer messages during the binding process, we also check if the node that moved was
        // a mobile with this router as its home agent.
        if (_bindingTable.containsKey(event._src.getHomeAddress())) {

            log.log(Logger.INFO, "Moving node has a Binding Table entry, expecting a BindingUpdate");

            // Get the binding buffer of this MN, and flag that binding is in process
            BindingBuffer buffer = _bindingTable.getBuffer(event._src.getHomeAddress());
            buffer._bindingInProgress = true;
        } else {

            log.log(Logger.INFO, "Node disconnected without moving");

        }

        log.lineBreak(Logger.INFO);

    }


    // Handle receiving binding updates from MNs
    private void handleBindingUpdate(BindingUpdate update)  {

        log.log(Logger.INFO, self());
        log.log(Logger.DEBUG, "Binding update received: dest: " + update.destination() + " source: " + update.source() +
                " home address: " + update._homeAddr);

        // Check if update has arrived to the same place
        if (update.destination().equals(_addr)) {

            log.log(Logger.INFO, "Received binding update from " + update.source() + " with home address " +
                                         update._homeAddr + ". Updating binding table.");


            _bindingTable.replace(update._homeAddr, update.source()); // update binding table with new CoA
            BindingBuffer buffer = _bindingTable.getBuffer(update._homeAddr);
            buffer._bindingInProgress = false; // Complete binding process, dont need to buffer anymore

            // Send the binding ack
            handleMessage(new BindingAcknowledgement(_addr, update.source(), 0));

            // Empty the binding buffer
            while(!(buffer._msgBuffer.isEmpty())) {
                Message msg = buffer._msgBuffer.poll();
                handleMessage(msg);
            }
        }
        else {
            // Forward the binding update, wasn't meant for us
            handleMessage(update);
        }

        log.lineBreak(Logger.INFO);

    }

    @Override
    public void recv(SimEnt source, Event event) {


        if (event instanceof LinkStateAdvertisement) {
            LinkStateAdvertisement lsa = (LinkStateAdvertisement) event;
            handleLSA(lsa);
        }
        else if (event instanceof BindingUpdate) {
            BindingUpdate update = (BindingUpdate) event;
            handleBindingUpdate(update);


        }
        else if (event instanceof Message) {
            Message msg = ((Message) event);

            handleMessage(msg);

        }

        if (event instanceof NodeMoveEvent) {

            NodeMoveEvent ev = (NodeMoveEvent) event;
            handleNodeMove(ev);

        }

    }


    public String self() {
        return "[ROUTER '(" + _addr  + ")']";
    }



}
