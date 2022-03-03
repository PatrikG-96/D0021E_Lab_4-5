package Sim;

import java.util.ArrayList;

public class Topology {

    private ArrayList<NetworkRouter> _routers;

    public Topology() {
        _routers = new ArrayList<>();
    }

    public void addRouter(NetworkRouter router) {
        _routers.add(router);
    }

    public void initOSPFProtocol() {
        for(NetworkRouter router: _routers) {
            router.advertiseState();
        }
    }


}
