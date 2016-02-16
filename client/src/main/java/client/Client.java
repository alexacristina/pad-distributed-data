package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import javax.xml.bind.JAXBException;
import service.discovery.DiscoveryClient;
import service.model.Employee;
import service.model.Location;
import service.transport.TransportClient;

public class Client {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, JAXBException {
        InetSocketAddress clientAddress = new InetSocketAddress("127.0.0.1", 3000);
        Location serverLocation = new DiscoveryClient(
                clientAddress).retrieveNodeInfo();
        System.out.println("INFO: " + serverLocation);
        TransportClient transportClient = new TransportClient(new Location(clientAddress), serverLocation);
        List<Employee> employees = transportClient.getEmployees();
        employees.stream().filter(e -> "Information Technology".equals(e.getDepartment()))
                .forEachOrdered(e -> System.out.println(e.getFirstName() + " " + e.getLastName()));
        for (int i=0; i<employees.size(); i++) {
            System.out.println((i+1) + " Employee is: " + employees.get(i).toString());
        }
    }
}