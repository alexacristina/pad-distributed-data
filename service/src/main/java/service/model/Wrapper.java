/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alexacris
 * @param <T>
 */
@XmlRootElement(name="employees")
public class Wrapper<T>{
    private final ArrayList<T> items;
        
    public Wrapper() {
        items = new ArrayList<>();
    }
    
    public Wrapper(ArrayList<T> data) {
        items = data;
    }
    
    @XmlAnyElement(lax=true)
    public Collection<T> getEmployees() {
        return (Collection < T >) items;
    }
}
