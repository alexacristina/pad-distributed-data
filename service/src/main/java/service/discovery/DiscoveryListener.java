package service.discovery;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.model.Location;

public class DiscoveryListener extends Thread {
    private InetSocketAddress nodeAddress;
    
    public DiscoveryListener(InetSocketAddress nodeAddress) {
        this.nodeAddress = nodeAddress;
    }
    
    @Override
    public void run() {
        sendNodeLocation(receiveClientRequest(), 
                new Location(nodeAddress));
    }
    
    private Location receiveClientRequest() {
        Location clientLocation = null;
        try {
            MulticastSocket socket = new MulticastSocket(30000);
            socket.joinGroup(InetAddress.getByName("224.5.5.5"));
            byte dataReceived[] = new byte[2048];
            DatagramPacket pack = new DatagramPacket(dataReceived, dataReceived.length);
            socket.receive(pack);
            
            
            ByteArrayInputStream is = new ByteArrayInputStream(dataReceived);
            ObjectInputStream ois = new ObjectInputStream(is);
            clientLocation = (Location) ois.readObject();
            socket.leaveGroup(InetAddress.getByName("224.5.5.5"));
            socket.close();
            System.out.println("Received location: " +
                    clientLocation.toString());
        } catch (IOException ex) {
            Logger.getLogger(DiscoveryListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DiscoveryListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return clientLocation;
    }
    
    private void sendNodeLocation(Location clientLocation, Location nodeLocation) {
        try {
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(nodeLocation);
            oos.flush();
            byte dataSend[] = bos.toByteArray();
            
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket pack = new DatagramPacket(dataSend, 
                                                    dataSend.length,
                                                    clientLocation.getLocation());
            System.out.println(nodeLocation.toString());
            socket.send(pack);
            socket.close();
        } catch (SocketException ex) {
            Logger.getLogger(DiscoveryListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DiscoveryListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}