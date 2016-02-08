package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import service.discovery.DiscoveryClient;
import service.model.Employee;
import service.model.Location;
import service.transport.TransportClient;

public class Client {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Location serverLocation = new DiscoveryClient(
                new InetSocketAddress("127.0.0.1", 3000)).retrieveNodeInfo();
        System.out.println("INFO: " + serverLocation);
        TransportClient transportClient = new TransportClient(serverLocation);
        List<Employee> employees = transportClient.getEmployees();
        employees.stream().filter(e -> "Information Technology".equals(e.getDepartment()))
                .forEachOrdered(e -> System.out.println(e.getFirstName() + " " + e.getLastName()));
        for (int i=0; i<employees.size(); i++) {
            System.out.println((i+1) + " Employee is: " + employees.get(i).toString());
        }
    }
}