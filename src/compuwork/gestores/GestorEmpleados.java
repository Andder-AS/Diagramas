package compuwork.gestores;

import compuwork.modelo.*;
import java.util.*;

public class GestorEmpleados {
    private static GestorEmpleados instancia;
    private Map<Integer, Empleado> empleados = new HashMap<>();
    private int nextId = 1;

    private GestorEmpleados() {
        // Datos de prueba
        EmpleadoPermanente emp1 = new EmpleadoPermanente(nextId++, "Juan Pérez", "juan@mail.com", 50000, 5000);
        emp1.agregarMetrica("Productividad", 8.5);
        empleados.put(emp1.getId(), emp1);
        
        EmpleadoTemporal emp2 = new EmpleadoTemporal(nextId++, "María García", "maria@mail.com", 25, 140);
        emp2.agregarMetrica("Eficiencia", 7.8);
        empleados.put(emp2.getId(), emp2);
    }

    public static GestorEmpleados getInstancia() {
        if (instancia == null) instancia = new GestorEmpleados();
        return instancia;
    }

    public void crear(Empleado emp) {
        if (emp.getId() == 0) emp.setId(nextId++);
        empleados.put(emp.getId(), emp);
    }

    public void eliminar(int id) {
        Empleado emp = empleados.remove(id);
        if (emp != null && emp.getDepartamento() != null) 
            emp.getDepartamento().removerEmpleado(emp);
    }

    public Empleado buscar(int id) { return empleados.get(id); }
    public Collection<Empleado> listar() { return empleados.values(); }
}
