


package service.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Employee implements Serializable {
    @XmlElement
    public String lastName;
    @XmlElement
    public String firstName;
    @XmlElement
    public String department;
    @XmlElement
    public double salary;
    
    public Employee() {
        this.firstName="";
        this.lastName="";
        this.department="";
        this.salary=0;
    }
    
    public Employee(String firstName, String lastName, String department, Double salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.salary = salary;
    }

    public String getDepartment() {
        return this.department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    
    public String getLastName() {
        return this.lastName;
    }
    
    public double getSalary() {
        return this.salary;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("%-10s ", firstName));
        s.append(String.format("%-10s @ ", lastName));
        s.append(department);
        s.append(" -> ");
        s.append(salary);
        return s.toString();
    }
}