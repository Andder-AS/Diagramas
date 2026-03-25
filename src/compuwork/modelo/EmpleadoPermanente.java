package compuwork.modelo;

public class EmpleadoPermanente extends Empleado {
    private double salarioBase, bonos;

    public EmpleadoPermanente(int id, String nombre, String email, double salarioBase, double bonos) {
        super(id, nombre, email);
        this.salarioBase = salarioBase;
        this.bonos = bonos;
    }

    @Override
    public double calcularDesempeno() {
        double promedio = getMetricas().values().stream().mapToDouble(Double::doubleValue).average().orElse(0);
        return (promedio * 0.7) + ((bonos / 10000) * 0.3);
    }
}
