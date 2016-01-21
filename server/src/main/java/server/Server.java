package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import service.discovery.DiscoveryListener;
import service.model.Employee;
import service.model.Location;
import service.transport.TransportServer;

public class Server {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileInputStream config = new FileInputStream(args[0]);
        JSONObject configJson = new JSONObject(new JSONTokener(config));

        InetSocketAddress serverAddress = new InetSocketAddress(
                (String) configJson.get("host"),
                (int) configJson.get("port"));
        
        JSONArray neighboursJson = new JSONArray(new JSONTokener(config));
        ArrayList <Location> neighbours = new ArrayList<>();
        
        for (int i=0; i<neighboursJson.length(); i++) {
            JSONObject neighbour = neighboursJson.getJSONObject(i);
            neighbours.add(
                    new Location( (String) neighbour.getString("host"),
                                  (int) neighbour.getInt("port")
                                ));
        }
        
        new DiscoveryListener(serverAddress).start();
        
        List<Employee> data = loadData(args[1]);
        TransportServer transportService = new TransportServer(serverAddress, neighbours, data);
        transportService.start();
    }
    
    private static ArrayList<Employee> loadData(String pathFile) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(pathFile);
        JSONArray jsonArray = new JSONArray(new JSONTokener(fis));
        ArrayList<Employee> employees = new ArrayList<>();
        
        for (int i=0; i<jsonArray.length(); i++) {
            JSONObject jsonEmployee = jsonArray.getJSONObject(i);
            employees.add(new Employee(
                    jsonEmployee.getString("first_name"),
                    jsonEmployee.getString("last_name"), 
                    jsonEmployee.getString("department"),
                    jsonEmployee.getDouble("salary")
            ));
            
        }
        return employees;
    }
}