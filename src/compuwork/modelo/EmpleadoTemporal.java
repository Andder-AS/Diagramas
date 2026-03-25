package compuwork.modelo;

public class EmpleadoTemporal extends Empleado {
    private double tarifaHora;
    private int horas;

    public EmpleadoTemporal(int id, String nombre, String email, double tarifaHora, int horas) {
        super(id, nombre, email);
        this.tarifaHora = tarifaHora;
        this.horas = horas;
    }

    @Override
    public double calcularDesempeno() {
        double promedio = getMetricas().values().stream().mapToDouble(Double::doubleValue).average().orElse(0);
        return promedio * (Math.min(horas, 160) / 160.0);
    }
}