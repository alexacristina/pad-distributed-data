/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.model;

import java.net.InetSocketAddress;

/**
 *
 * @author alexacris
 */
public class NodeInfo extends Location {
    private int neighbours;
    
    public NodeInfo(InetSocketAddress location, int neighbours) {
        super(location);
        this.neighbours = neighbours;
    }
    
    public int getNeighboursNr() {
        return neighbours;
    }
    
    public void setNeighboursNr(int neighbours) {
        this.neighbours = neighbours; 
    }
    
}
