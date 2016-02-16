
package service.transport;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import service.model.Employee;
import service.model.Wrapper;
import service.model.Location;
import service.model.NodeInfo;

public class TransportClient {
    private final Location clientLocation;
    private final Location serverLocation;
    
    public TransportClient(Location clientLocation, Location serverLocation) {
        this.clientLocation = clientLocation;
        this.serverLocation = serverLocation;
    }
    
    public ArrayList<Employee> getEmployees() throws IOException, ClassNotFoundException, JAXBException {
        Socket s = new Socket();
        Collection <Employee> employees = new ArrayList<>();
        
            s.connect(serverLocation.getLocation());
            ObjectOutputStream oout = new ObjectOutputStream(s.getOutputStream());
            oout.writeObject(clientLocation);


            
            if (this.clientLocation.getLocation().getPort()==3000) {
                
                
                JAXBContext jaxbContext = JAXBContext.newInstance(Wrapper.class, employees.getClass());
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                System.out.println(s.getInputStream().getClass());
                StreamSource xml = new StreamSource(s.getInputStream());
                Wrapper<Employee> list;
                list = (Wrapper<Employee>) jaxbUnmarshaller.unmarshal(s.getInputStream());
                employees = list.getEmployees();
                System.out.println("Everything received" + list);
                employees.stream().forEach(e -> System.out.println(e));
                
                
            } else {
                
                BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
                ObjectInputStream ois = new ObjectInputStream(bis);
                employees = (ArrayList<Employee>) ois.readObject();
            }
            
            s.close();
            return (ArrayList<Employee>)employees;
        
    }
    
    
}