package Sim.ospf;

import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {

        Graph g = new Graph();
        Graph g2 = new Graph();

        Node n1 = new Node(0, 1);
        Node n2 = new Node(1, 1);
        Node a = new Node(4, 1);

        g.addNode(n1);
        g.addNode(n2);
        g.addNode(a);

        g.connectNodes(n1, n2);
        g.connectNodes(n2,a);

        Node n3 = new Node(0, 1);
        Node n4 = new Node(1, 1);
        Node n5 = new Node(2, 1);
        Node n6 = new Node(3, 1);
        Node n7 = new Node(4, 1);
        Node b = new Node(5, 1);

        g2.addNode(n3);
        g2.addNode(n4);
        g2.addNode(n5);
        g2.addNode(n6);
        g2.addNode(n7);
        g2.addNode(b);

        g2.connectNodes(n3,n4);
        g2.connectNodes(n4,n5);
        g2.connectNodes(n4,n6);
        g2.connectNodes(n6,n7);
        g2.connectNodes(n6, b);

        g.combine(g2);

        TreeNode tree = g.spf(0);

        ArrayList<TreeNode> list = tree.getChildren();

        for(TreeNode m : list) {
            System.out.println("Direct connection to: " + m.getID());
            ArrayList<Integer> ids = new ArrayList<>();
            m.allSubNodes(ids);
            System.out.println("Found ids: ");
            for(int k : ids) {
                System.out.println(k);
            }
        }

    }
}
