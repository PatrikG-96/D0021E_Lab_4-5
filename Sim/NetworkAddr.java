package Sim;

// This class represent the network address, it consist of a network identity
// "_networkId" represented as an integer (if you want to link this to IP number it can be
// compared to the network part of the IP address like 132.17.9.0). Then _nodeId represent
// the host part.

import java.util.Objects;

public class NetworkAddr {
	private int _networkId;
	private int _nodeId;
	
	NetworkAddr(int network, int node)
	{
		_networkId=network;
		_nodeId=node;
	}

	public void setNetworkId(int networkId) {
		_networkId = networkId;
	}

	public void setNodeId(int nodeId) {
		_nodeId = nodeId;
	}
	
	public int networkId()
	{
		return _networkId;
	}
	
	public int nodeId()
	{
		return _nodeId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NetworkAddr that = (NetworkAddr) o;
		return _networkId == that._networkId && _nodeId == that._nodeId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_networkId, _nodeId);
	}

	public String toString() {
		return _networkId + "." + _nodeId;
	}
}
