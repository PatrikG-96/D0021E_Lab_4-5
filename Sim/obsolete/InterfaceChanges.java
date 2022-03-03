package Sim.obsolete;

import Sim.Node;

import java.util.HashMap;

/**
 * In the context of this simulator and lab 3, I make the interpretation that to notify
 * a sender that the destination node has changed interface
 */
public class InterfaceChanges {

    private HashMap<Key, ChangeEvent> map;
    private int num_interfaces;

    public InterfaceChanges(int num_interfaces) {
        map = new HashMap<>();
        this.num_interfaces = num_interfaces;
    }

    public boolean changeExists(int sender_interface, int receiver_interface) {
        return map.containsKey(new Key(sender_interface, receiver_interface));
    }

    public void removeChange(int sender_interface, int receiver_interface) {
        map.remove(new Key(sender_interface, receiver_interface));
    }

    public ChangeEvent getChange(int sender_interface, int receiver_interface) {
        return map.get(new Key(sender_interface, receiver_interface));
    }

    public void addChange(Node src, int interface_from, int interface_to, Boolean[] activeInterfaces) {
        ChangeEvent change = new ChangeEvent(src,interface_from,interface_to);
        for (int i = 0; i<activeInterfaces.length; i++) {

            if (activeInterfaces[i] && i != interface_to) {
                Key key = new Key(i, interface_to);
                if (map.containsKey(key)) {
                    map.replace(key, change);
                }
                else {
                    map.put(key, change);
                }

            }

        }
    }




    class ChangeEvent {
        Node node;
        int from;
        int to;
        ChangeEvent(Node node, int from, int to) {
            this.node = node;
            this.from = from;
            this.to = to;
        }
    }

    private class Key {
        private final int x;
        private final int y;

        public Key(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key key = (Key) o;
            return x == key.x && y == key.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

    }
}
