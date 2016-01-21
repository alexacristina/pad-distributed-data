package service.transport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.model.Employee;
import service.model.Location;

public class TransportServer extends Thread {

    private final InetSocketAddress serverAddress;
    private final ServerSocket serverSocket;
    private final List<Employee> listEmployees;
    private final ArrayList<Location> neighbours;
    
    public TransportServer(InetSocketAddress serverAddress,
                            ArrayList <Location> neighbours,
                            List <Employee> employees) throws IOException {
        this.serverAddress = serverAddress;
        this.listEmployees = employees;
        this.neighbours = neighbours;
        this.serverSocket = new ServerSocket(serverAddress.getPort());
    }
    
    
    @Override
    public void run() {
        while (true) {
            try {
                Socket connectionSocket = serverSocket.accept();
                System.out.println("Socket accepted");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(listEmployees);
                OutputStream os = connectionSocket.getOutputStream();
                InputStream answer = new ByteArrayInputStream(baos.toByteArray());
                byte buffer[] = new byte[2048];
                while (-1 != answer.read(buffer)) {
                    os.write(buffer);
                }   
                os.write(buffer);
            } catch (IOException ex) {
                Logger.getLogger(TransportServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
}