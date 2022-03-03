package Sim.ospf;

import java.util.ArrayList;

public class TreeNode {


    private int _id;
    private ArrayList<TreeNode> children;

    public TreeNode(int id) {
        _id = id;
        children = new ArrayList<>();
    }

    public int getID() {
        return _id;
    }

    public boolean canAdd(int id) {
        if (!exists(id)) {
            return true;
        }
        return false;
    }

    public boolean exists(int id) {
        if(_id == id) {
            return true;
        }

        for(TreeNode node : children) {
            if (node.exists(id)) {
                return true;
            }
        }
        return false;
    }

    public void addChild(int parentID, TreeNode child) {
        if(_id == parentID) {
            children.add(child);
            return;
        }
        for(TreeNode node : children) {
            node.addChild(parentID, child);
        }

    }

    public void allSubNodes(ArrayList<Integer> list) {
        for(TreeNode node : children) {
            list.add(node._id);
            node.allSubNodes(list);
        }
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    public void print() {
        System.out.println(_id);
        for(TreeNode node : children) {
            node.print();
        }


    }

}
