package compuwork.modelo;

import java.util.HashMap;
import java.util.Map;

public abstract class Empleado {
    private int id;
    private String nombre;
    private String email;
    private Departamento departamento;
    private Map<String, Double> metricas = new HashMap<>();

    public Empleado(int id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public abstract double calcularDesempeno();

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }
    public Map<String, Double> getMetricas() { return metricas; }
    public void agregarMetrica(String nombre, double valor) { metricas.put(nombre, valor); }
    
    @Override
    public String toString() { return id + " - " + nombre; }
}