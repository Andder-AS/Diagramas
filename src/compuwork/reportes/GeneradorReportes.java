package compuwork.reportes;

import compuwork.modelo.*;
import java.text.DecimalFormat;

public class GeneradorReportes {
    private DecimalFormat df = new DecimalFormat("#.##");
    
    public String reporteIndividual(Empleado emp) {
        return String.format("""
            ═══════════════════════════════════════
                REPORTE DE DESEMPEÑO INDIVIDUAL
            ═══════════════════════════════════════
            
            Empleado: %s
            ID: %d
            Email: %s
            Tipo: %s
            Departamento: %s
            
            MÉTRICAS:
            %s
            DESEMPEÑO TOTAL: %.2f / 10.0
            
            ═══════════════════════════════════════
            """,
            emp.getNombre(), emp.getId(), emp.getEmail(),
            emp.getClass().getSimpleName(),
            emp.getDepartamento() != null ? emp.getDepartamento().getNombre() : "No asignado",
            formatoMetricas(emp),
            emp.calcularDesempeno()
        );
    }
    
    public String reporteDepartamento(Departamento depto) {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════\n");
        sb.append("   REPORTE DE DESEMPEÑO POR DEPARTAMENTO\n");
        sb.append("═══════════════════════════════════════════\n\n");
        sb.append("Departamento: ").append(depto.getNombre()).append("\n");
        sb.append("Empleados: ").append(depto.getEmpleados().size()).append("\n");
        sb.append("Desempeño Promedio: ").append(df.format(depto.getDesempenoPromedio())).append("\n\n");
        sb.append("DETALLE:\n");
        for (Empleado emp : depto.getEmpleados()) {
            sb.append(String.format("  • %-20s: %.2f\n", emp.getNombre(), emp.calcularDesempeno()));
        }
        return sb.toString();
    }
    
    private String formatoMetricas(Empleado emp) {
        if (emp.getMetricas().isEmpty()) return "  Sin métricas registradas\n";
        StringBuilder sb = new StringBuilder();
        emp.getMetricas().forEach((k, v) -> sb.append(String.format("  • %-12s: %.2f\n", k, v)));
        return sb.toString();
    }
}
