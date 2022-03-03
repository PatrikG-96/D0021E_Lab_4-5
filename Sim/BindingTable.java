package Sim;

import java.util.HashMap;

public class BindingTable {

    private HashMap<NetworkAddr, NetworkAddr> map;
    private HashMap<NetworkAddr, BindingBuffer> buffers;

    public BindingTable() {
        map = new HashMap<>();
        buffers = new HashMap<>();
    }

    public BindingBuffer getBuffer(NetworkAddr key) {
        return buffers.get(key);
    }

    public void put(NetworkAddr key, NetworkAddr entry) {
        NetworkAddr tablekey = new NetworkAddr(key.networkId(), key.nodeId());
        map.put(tablekey, entry);
        buffers.put(tablekey, new BindingBuffer(100));
    }

    public void replace(NetworkAddr key, NetworkAddr entry) {
        map.replace(key, entry);
    }

    public NetworkAddr get(NetworkAddr key) {
        return map.get(key);
    }

    public void remove(NetworkAddr key) {
        map.remove(key);
    }

    public boolean containsKey(NetworkAddr key) {
        return map.containsKey(key);
    }


}
