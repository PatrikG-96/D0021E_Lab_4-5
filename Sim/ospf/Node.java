package Sim.ospf;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Node implements Comparable {

    private int _nodeID;
    private int _cost;


    public Node(int nodeID, int cost) {
        _nodeID = nodeID;
        _cost = cost;
    }


    public void setID(int nodeID) {
        _nodeID = nodeID;
    }

    public void setCost(int cost) {
        _cost = cost;
    }

    public int nodeID() {
        return _nodeID;
    }

    public int cost() {
        return _cost;
    }

    @Override
    public int compareTo(Object o) {
        if (_nodeID == _nodeID && _cost == _cost) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node)) {
            return false;
        }
        Node n = (Node) o;
        if (_nodeID == n._nodeID && _cost == n._cost) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "" + _nodeID;
    }
}
