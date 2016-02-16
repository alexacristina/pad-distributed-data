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
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Comparator;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.model.Location;
import service.model.NodeInfo;

public class DiscoveryClient {
    private final InetSocketAddress clientAddress;
    
    public DiscoveryClient(InetSocketAddress clientAddress) {
        this.clientAddress = clientAddress;
    }
    
    public Location retrieveNodeInfo() throws IOException {
        ArrayList <NodeInfo> locations = null;
        
        sendLocationRequest();
        locations = getNodesInfo();
        
        if (locations.size() > 0) {
            NodeInfo maven;
            maven = locations
                    .stream()
                    .max(Comparator.comparingInt((nodeInfo) -> nodeInfo.getNeighboursNr()))
                    .get();
            return maven;
        }
        else {
            return null;
        }
    }
    
    private void sendLocationRequest() throws IOException {
        MulticastSocket socket = new MulticastSocket();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(new Location(clientAddress));
        byte sendData[] = bos.toByteArray();
        oos.close();
        DatagramPacket requestPacket = new DatagramPacket(sendData, 
                                                          sendData.length,
                                                          InetAddress.getByName("224.5.5.5"),
                                                          30000);
        socket.send(requestPacket);
        socket.close();
    }
    
    private ArrayList<NodeInfo> getNodesInfo() throws IOException {
        ArrayList<NodeInfo> locations = new ArrayList<>();
        DatagramSocket dataReceiverSocket = new DatagramSocket(clientAddress);
        byte receivedData[] = new byte[2048];
        boolean timeOut = false;
        
        dataReceiverSocket.setSoTimeout((int) SECONDS.toMillis(30));
        
        while (!timeOut) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
                dataReceiverSocket.receive(receivePacket);
                ByteArrayInputStream byteArray = new ByteArrayInputStream(receivedData);
                ObjectInputStream ois = new ObjectInputStream(byteArray);
                locations.add((NodeInfo) ois.readObject()); 
                
            } catch(SocketTimeoutException ste) {
                System.out.println("WARNING: Time out!");
                timeOut = true;
            } catch(ClassNotFoundException ex) {
                Logger.getLogger(DiscoveryClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("[INFO] Received packet: " + locations.get(0));
        dataReceiverSocket.close();
        return locations;
    }
}
        
