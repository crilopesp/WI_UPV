/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Cristian
 */
public class Metro implements Serializable {
    private String nombre;
    private int id;
    private String latitud;
    private String longitud;
    private ArrayList<Integer> lineas;

    public Metro(String nombre, int id, String latitud, String longitud, ArrayList<Integer> lineas) {
        this.nombre = nombre;
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.lineas = lineas;
    }

    public Metro() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public ArrayList<Integer> getLineas() {
        return lineas;
    }

    public void setLineas(ArrayList<Integer> lineas) {
        this.lineas = lineas;
    }
}
