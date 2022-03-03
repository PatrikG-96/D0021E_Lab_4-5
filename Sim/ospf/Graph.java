package Sim.ospf;

import java.lang.reflect.Array;
import java.util.*;

// Bidirected graph
public class Graph {

    private ArrayList<Node> _nodes;
    private ArrayList<Edge> _edges;

    public Graph() {
        _nodes = new ArrayList<>();
        _edges = new ArrayList<>();
    }

    public ArrayList<Node> getNodes() {
        return _nodes;
    }

    public void addNode(Node node) {
        _nodes.add(node);

    }

    public Node getNode(int nodeID) {
        for(Node n : _nodes) {
            if (n.nodeID() == nodeID) {
                return n;
            }
        }
        return null;
    }

    public ArrayList<Node> getNeighbors(Node node) {
        ArrayList<Node> neighbors = new ArrayList<>();
        for (Edge e : _edges) {

            if(e.origin() == node.nodeID()) {
                neighbors.add(getNode(e.destination()));
            }
            if(e.destination() == node.nodeID()) {
                neighbors.add(getNode(e.origin()));
            }

        }
        return neighbors;
    }

    public int numNodes() {
        return _nodes.size();
    }

    public void connectNodes(Node node1, Node node2) {
        Edge e = Edge.connectNodes(node1.nodeID(), node2.nodeID());
        _edges.add(e);
    }

    // Combine two different graphs by adding all the unique nodes and edges from the other graph
    public void combine(Graph other) {

        // For each node in the other graph
        for(Node n : other._nodes) {
            Node n_copy = new Node(n.nodeID(), n.cost());
            // If the current graph doesn't have a node with that ID, add it to this graph
            if(!(_nodes.contains(n_copy))) {
                _nodes.add(n_copy);
            }
        }

        // For each edge in the other graph
        for (Edge e : other._edges) {
            Edge e_copy = Edge.connectNodes(e.origin(), e.destination());
            // If the current graph doesn't have an edge between these 2 nodes, add it
            if(!(_edges.contains(e))) {
                _edges.add(e_copy);
            }
        }

    }

    // Use djisktras to perform shortest path first
    public TreeNode spf(int nodeID) {

        Set<Integer> visited = new HashSet<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();

        // Because node IDs wont match array indices, use maps for distance
        HashMap<Integer, Integer> dist = new HashMap<>();

        // To compute the tree, a map of nodeID as key and the path to get to that node ID is used
        HashMap<Integer, ArrayList<Integer>> paths = new HashMap<>();

        // Initialize distances and the paths
        for(Node node : _nodes) {
            dist.put(node.nodeID(), Integer.MAX_VALUE);
            paths.put(node.nodeID(), new ArrayList<>());
        }

        // Initialize starting node
        Node start = getNode(nodeID);
        dist.replace(start.nodeID(), 0);

        pq.add(start);

        // Continue until all nodes have been visited, just normal djikstras stuff
        while(visited.size() != numNodes()) {

            if (pq.isEmpty()) {
                break;
            }

            Node n = pq.remove();

            if (visited.contains(n.nodeID())) {
                continue;
            }

            visited.add(n.nodeID());

            for (Node _n : getNeighbors(n)) {

                if (!visited.contains(_n.nodeID())) {

                    // To avoid reference issues, copy the node
                    Node copy = new Node(_n.nodeID(), _n.cost());

                    int distance = copy.cost() + dist.get(n.nodeID());

                    if (distance < dist.get(_n.nodeID())) {
                        // Append this node (_n) to the path that was taken to its neighbor node (n)
                        ArrayList<Integer> newPath = (ArrayList<Integer>) paths.get(n.nodeID()).clone();
                        newPath.add(copy.nodeID());

                        // Replace with new path and distance
                        paths.replace(copy.nodeID(), newPath);
                        dist.replace(copy.nodeID(), distance);
                    }

                    //Add the copy to the min heap (here updating costs without copying node can cause issues)
                    copy.setCost(copy.cost() + distance);
                    pq.add(copy);
                }

            }

        }

        // Building the Shortest path tree
        TreeNode tree = new TreeNode(start.nodeID());

        // For each distance entry (nodeID : cost)
        for(Map.Entry entry : dist.entrySet()) {

            // Get the path to this node (key is node ID)
            // This can be a list like [1,2,4] which denotes the path from starting
            // node 0 to end node 4. Basically, the optimal path to take from starting
            // node to end node for each node.
            ArrayList<Integer> s = paths.get(entry.getKey());

            // It's a tree, start node is the root
            int prev = start.nodeID();

            for(int k = 0; k < s.size(); k++) {

                // If the node next in the optimal path doesn't exist, add it as a child to previous node
                if(tree.canAdd(s.get(k))) {
                    tree.addChild(prev, new TreeNode(s.get(k)));
                }
                prev = s.get(k);
            }

        }

        return tree;

    }


}
