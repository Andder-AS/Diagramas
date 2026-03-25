package compuwork.gestores;

import compuwork.modelo.*;
import java.util.*;

public class GestorDepartamentos {
    private static GestorDepartamentos instancia;
    private Map<Integer, Departamento> deptos = new HashMap<>();
    private int nextId = 1;

    private GestorDepartamentos() {
        deptos.put(nextId, new Departamento(nextId++, "Tecnología"));
        deptos.put(nextId, new Departamento(nextId++, "Recursos Humanos"));
    }

    public static GestorDepartamentos getInstancia() {
        if (instancia == null) instancia = new GestorDepartamentos();
        return instancia;
    }

    public void crear(Departamento depto) {
        if (depto.getId() == 0) depto.setId(nextId++);
        deptos.put(depto.getId(), depto);
    }

    public void eliminar(int id) { deptos.remove(id); }
    public Departamento buscar(int id) { return deptos.get(id); }
    public Collection<Departamento> listar() { return deptos.values(); }
}
