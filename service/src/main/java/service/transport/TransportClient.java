
package service.transport;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import service.model.Employee;
import service.model.Location;

public class TransportClient {
    private final Location serverLocation;
    
    public TransportClient(Location serverLocation) {
        this.serverLocation = serverLocation;
    }
    
    public ArrayList<Employee> getEmployees() throws IOException, ClassNotFoundException {
        Socket s = new Socket();
        s.connect(serverLocation.getLocation());
        BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
        ObjectInputStream ois = new ObjectInputStream(bis);
        ArrayList<Employee> employees = (ArrayList<Employee>) ois.readObject();
        s.close();
        return employees;
    }
    
    
}