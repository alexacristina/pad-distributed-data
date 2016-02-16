package service.transport;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import service.model.Employee;
import service.model.Wrapper;
import service.model.Location;

public class TransportServer extends Thread {

    private final InetSocketAddress serverAddress;
    private final ServerSocket serverSocket;
    private final ArrayList<Employee> listEmployees;
    private final ArrayList<Location> neighbours;
    
    public TransportServer(InetSocketAddress serverAddress,
                            ArrayList <Location> neighbours,
                            ArrayList <Employee> employees) throws IOException {
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
                System.out.println("Socket accepted from: " + 
                                    connectionSocket.getRemoteSocketAddress());
                BufferedInputStream bis = new BufferedInputStream(connectionSocket.getInputStream());
                ObjectInputStream ois = new ObjectInputStream(bis);
                Location clientInfo = (Location) ois.readObject();
                System.out.println("DATA LOCATION RECEIVED");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos); 
               if (clientInfo.getLocation().getPort()==3000) {
                    JAXBContext jaxbContext = JAXBContext.newInstance(Wrapper.class, Employee.class);
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    jaxbMarshaller.marshal(new Wrapper(listEmployees), System.out);
                    jaxbMarshaller.marshal(new Wrapper(listEmployees),
                            connectionSocket.getOutputStream());
                    jaxbMarshaller.marshal(new Wrapper(listEmployees), new File("xmlJava.xml"));
                    
                    System.out.println("DATA SENT");
                    
                    JAXBContext jaxbContextu = JAXBContext.newInstance(Employee.class);
                    Unmarshaller jaxbUnmarshaller = jaxbContextu.createUnmarshaller();
                    
                    StreamSource xml = new StreamSource(new FileInputStream("xmlJava.xml"));
                    Wrapper<Employee> list = (Wrapper<Employee>)jaxbUnmarshaller.unmarshal(xml);
                    Collection employees = list.getEmployees();
                    employees.forEach(emp -> System.out.print(emp.toString()));
                } else {
                    
                    
                    oos.writeObject(listEmployees);
                    OutputStream os = connectionSocket.getOutputStream();
                    InputStream answer = new ByteArrayInputStream(baos.toByteArray());
                    byte buffer[] = new byte[2048];
                    while (-1 != answer.read(buffer)) {
                        os.write(buffer);
                    }   
                    os.write(buffer);
                    }
                connectionSocket.close();
               
            } catch (IOException | ClassNotFoundException | JAXBException ex) {
                Logger.getLogger(TransportServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
}