package service.model;

import java.io.Serializable;
import java.net.InetSocketAddress;


public class Location implements Serializable {
    private InetSocketAddress location;
    
    public Location() {
        
    }
    
    public Location(InetSocketAddress address) {
        this.location = address;
    }
    
    public Location(String ipAddress, int port) {
        this.location = new InetSocketAddress(ipAddress, port);
    }
    
    public InetSocketAddress getLocation() {
        return location;
    }
    
    public void setLocation(InetSocketAddress address) {
        this.location = address;
    }
    
    @Override
    public String toString() {
        return "Location {"
                + "ip_address = "+location.getHostString() +
                "port = " + location.getPort() + '}';
    }
}