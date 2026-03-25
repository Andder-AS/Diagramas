package compuwork.modelo;

import java.util.ArrayList;
import java.util.List;

public class Departamento {
    private int id;
    private String nombre;
    private List<Empleado> empleados = new ArrayList<>();

    public Departamento(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public void agregarEmpleado(Empleado emp) {
        if (!empleados.contains(emp)) {
            empleados.add(emp);
            emp.setDepartamento(this);
        }
    }

    public void removerEmpleado(Empleado emp) {
        empleados.remove(emp);
        emp.setDepartamento(null);
    }

    public double getDesempenoPromedio() {
        return empleados.stream().mapToDouble(Empleado::calcularDesempeno).average().orElse(0);
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<Empleado> getEmpleados() { return empleados; }
    
    @Override
    public String toString() { return id + " - " + nombre + " (" + empleados.size() + " emp)"; }
}