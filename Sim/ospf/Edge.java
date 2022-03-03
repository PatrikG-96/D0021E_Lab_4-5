package Sim.ospf;

public class Edge {

    private int _from;
    private int _to;


    public static Edge connectNodes(int n1, int n2) {
        Edge e = new Edge();
        e._from = n1;
        e._to = n2;
        return e;
    }

    public int origin() {
        return _from;
    }

    public int destination() {
        return _to;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Edge)) {
            return false;
        }
        Edge e = (Edge) o;
        if ((_from == e._from && _to == e._to) || (_from == e._to && _to == e._from)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return _from + ", " + _to;
    }

}
