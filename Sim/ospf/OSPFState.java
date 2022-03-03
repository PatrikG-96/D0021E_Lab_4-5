package Sim.ospf;


import java.util.ArrayList;
import java.util.HashMap;


// Representation of an OSPF state for a single router
public class OSPFState {

    // Graph representation of the network Topology
    private Graph _linkState;

    // ID of the network router that uses this state
    private int _networkID;

    // Shortest Path Tree computed from the graph
    private TreeNode _spt;

    // All links have the same cost, which is set here
    public static final int LINK_DEFAULT_COST = 1;

    public OSPFState(int networkID) {
        _networkID = networkID;
        _linkState = new Graph();
        _linkState.addNode(new Node(networkID,LINK_DEFAULT_COST));
        _spt = new TreeNode(networkID);
    }

    // Add a new neighboring router to the OSPF state
    public void addConnection(int otherNetwork) {
        Node newNode = new Node(otherNetwork, LINK_DEFAULT_COST); // Represents the new router

        // Add node and an edge for the node, representing the link between us and them
        _linkState.addNode(newNode);
        _linkState.connectNodes(_linkState.getNode(_networkID), newNode);

        // Perform djikstras on the new graph to compute the Shortest path tree for the graph
        // with this network router as the origin
        _spt = _linkState.spf(_networkID);
    }


    public Graph getLinkState() {
        return _linkState;
    }

    // Combine 2 link states and compute the new SPF
    public void mergeLinkStates(Graph otherState) {
        _linkState.combine(otherState);
        _spt = _linkState.spf(_networkID);
    }


    // From the current shortest path tree, compute a new global routing table

    public HashMap<Integer, Integer> getGlobalRoutingTable() {

        HashMap<Integer, Integer> map = new HashMap<>();

        // We start of by getting the direct connections to this router. We do this by getting all the
        // children of the root node of the SPF result
        for(TreeNode node : _spt.getChildren()) {

            // For each child node...
            int neighbor = node.getID(); // get its ID

            // Get all nodes in the rest of the subtree
            /*
                O
               / \
              1   2
             / \
            3   4
           /
          5
            For example, given this tree, the children of the root nodes are 1 and 2.
            All the subnodes of 1 are 3, 4 and 5. Basically all nodes that you have to travel
            past 1 to get to.
            */
            ArrayList<Integer> subnodes = new ArrayList<>();
            node.allSubNodes(subnodes);

            // Being a subnode to a child node means that we have to use the child node to get to the subnode
            // Therefore, in a routing context, all networks that are represented by these subnodes must be
            // routed to this child node to build the global routing table
            for (int i : subnodes) {
                map.put(i, neighbor);
            }
        }
        return map;
    }

}
