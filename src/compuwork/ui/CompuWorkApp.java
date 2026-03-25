package compuwork.ui;

import compuwork.modelo.*;
import compuwork.gestores.*;
import compuwork.reportes.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CompuWorkApp extends JFrame {
    private final GestorEmpleados ge = GestorEmpleados.getInstancia();
    private final GestorDepartamentos gd = GestorDepartamentos.getInstancia();
    private final GeneradorReportes gr = new GeneradorReportes();
    
    private final JTabbedPane tabs;
    private JTable tablaEmpleados;
    private JTable tablaDeptos;
    private JTextArea areaReporte;
    
    public CompuWorkApp() {
        setTitle("CompuWork - Sistema de Gestión");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        tabs = new JTabbedPane();
        tabs.addTab("Empleados", crearPanelEmpleados());
        tabs.addTab("Departamentos", crearPanelDepartamentos());
        tabs.addTab("Reportes", crearPanelReportes());
        
        add(tabs);
    }
    
    private JPanel crearPanelEmpleados() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tabla
        tablaEmpleados = new JTable(new EmpleadoTableModel());
        JScrollPane scroll = new JScrollPane(tablaEmpleados);
        panel.add(scroll, BorderLayout.CENTER);
        
        // Formulario
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Agregar Empleado"));
        
        JTextField txtNombre = new JTextField();
        JTextField txtEmail = new JTextField();
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Permanente", "Temporal"});
        JTextField txtCampo1 = new JTextField(); // Salario o Tarifa
        JTextField txtCampo2 = new JTextField(); // Bonos o Horas
        
        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        form.add(new JLabel("Email:")); form.add(txtEmail);
        form.add(new JLabel("Tipo:")); form.add(comboTipo);
        form.add(new JLabel("Valor 1:")); form.add(txtCampo1);
        form.add(new JLabel("Valor 2:")); form.add(txtCampo2);
        
        JButton btnAgregar = new JButton("Agregar Empleado");
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = txtNombre.getText().trim();
                    String email = txtEmail.getText().trim();
                    if (nombre.isEmpty() || email.isEmpty()) return;
                    Empleado emp;
                    if (comboTipo.getSelectedItem().equals("Permanente")) {
                        emp = new EmpleadoPermanente(0, nombre, email,
                                Double.parseDouble(txtCampo1.getText()),
                                Double.parseDouble(txtCampo2.getText()));
                    } else {
                        emp = new EmpleadoTemporal(0, nombre, email,
                                Double.parseDouble(txtCampo1.getText()),
                                Integer.parseInt(txtCampo2.getText()));
                    }   emp.agregarMetrica("Inicial", 7.5);
                    ge.crear(emp);
                    ((EmpleadoTableModel)tablaEmpleados.getModel()).fireTableDataChanged();
                    txtNombre.setText("");
                    txtEmail.setText("");
                    txtCampo1.setText("");
                    txtCampo2.setText("");
                    JOptionPane.showMessageDialog(CompuWorkApp.this, "Empleado agregado");
                } catch (HeadlessException | NumberFormatException ex) {
                    JOptionPane.showMessageDialog(CompuWorkApp.this, "Error en los datos");
                }
            }
        });
        
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        btnEliminar.addActionListener(e -> {
            int fila = tablaEmpleados.getSelectedRow();
            if (fila >= 0) {
                int id = (int)tablaEmpleados.getValueAt(fila, 0);
                ge.eliminar(id);
                ((EmpleadoTableModel)tablaEmpleados.getModel()).fireTableDataChanged();
            }
        });
        
        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnAgregar);
        botones.add(btnEliminar);
        
        panel.add(form, BorderLayout.NORTH);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel crearPanelDepartamentos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        tablaDeptos = new JTable(new DepartamentoTableModel());
        JScrollPane scroll = new JScrollPane(tablaDeptos);
        panel.add(scroll, BorderLayout.CENTER);
        
        JPanel form = new JPanel(new FlowLayout());
        JTextField txtNombre = new JTextField(20);
        JButton btnAgregar = new JButton("Agregar Departamento");
        JButton btnEliminar = new JButton("Eliminar");
        
        btnAgregar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            if (!nombre.isEmpty()) {
                gd.crear(new Departamento(0, nombre));
                ((DepartamentoTableModel)tablaDeptos.getModel()).fireTableDataChanged();
                txtNombre.setText("");
            }
        });
        
        btnEliminar.addActionListener(e -> {
            int fila = tablaDeptos.getSelectedRow();
            if (fila >= 0) {
                int id = (int)tablaDeptos.getValueAt(fila, 0);
                gd.eliminar(id);
                ((DepartamentoTableModel)tablaDeptos.getModel()).fireTableDataChanged();
            }
        });
        
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(btnAgregar);
        form.add(btnEliminar);
        panel.add(form, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        areaReporte = new JTextArea();
        areaReporte.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaReporte);
        
        JPanel botones = new JPanel(new FlowLayout());
        JButton btnIndividual = new JButton("Reporte Individual");
        JButton btnDepartamento = new JButton("Reporte por Depto");
        JButton btnGeneral = new JButton("Reporte General");
        
        btnIndividual.addActionListener(e -> {
            String[] empleados = ge.listar().stream().map(Empleado::toString).toArray(String[]::new);
            String selec = (String)JOptionPane.showInputDialog(this, "Seleccione empleado", "Reporte",
                JOptionPane.PLAIN_MESSAGE, null, empleados, empleados[0]);
            if (selec != null) {
                int id = Integer.parseInt(selec.split(" - ")[0]);
                areaReporte.setText(gr.reporteIndividual(ge.buscar(id)));
            }
        });
        
        btnDepartamento.addActionListener(e -> {
            String[] deptos = gd.listar().stream().map(Departamento::toString).toArray(String[]::new);
            String selec = (String)JOptionPane.showInputDialog(this, "Seleccione departamento", "Reporte",
                JOptionPane.PLAIN_MESSAGE, null, deptos, deptos[0]);
            if (selec != null) {
                int id = Integer.parseInt(selec.split(" - ")[0]);
                areaReporte.setText(gr.reporteDepartamento(gd.buscar(id)));
            }
        });
        
        btnGeneral.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("╔════════════════════════════════════════╗\n");
            sb.append("║     REPORTE GENERAL DE LA EMPRESA      ║\n");
            sb.append("╚════════════════════════════════════════╝\n\n");
            sb.append("Total Empleados: ").append(ge.listar().size()).append("\n\n");
            sb.append("DEPARTAMENTOS:\n");
            for (Departamento d : gd.listar()) {
                sb.append(String.format("  • %-15s: %.2f (%d emp)\n", 
                    d.getNombre(), d.getDesempenoPromedio(), d.getEmpleados().size()));
            }
            areaReporte.setText(sb.toString());
        });
        
        botones.add(btnIndividual);
        botones.add(btnDepartamento);
        botones.add(btnGeneral);
        
        panel.add(botones, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    
    // TableModels
    class EmpleadoTableModel extends AbstractTableModel {
        private final String[] cols = {"ID", "Nombre", "Email", "Tipo", "Desempeño"};
        
        @Override
        public int getRowCount() { return ge.listar().size(); }
        @Override
        public int getColumnCount() { return cols.length; }
        @Override
        public String getColumnName(int col) { return cols[col]; }
        
        @Override
        public Object getValueAt(int row, int col) {
            Empleado emp = (Empleado)ge.listar().toArray()[row];
            return switch (col) {
                case 0 -> emp.getId();
                case 1 -> emp.getNombre();
                case 2 -> emp.getEmail();
                case 3 -> emp.getClass().getSimpleName();
                case 4 -> String.format("%.2f", emp.calcularDesempeno());
                default -> null;
            };
        }
    }
    
    class DepartamentoTableModel extends AbstractTableModel {
        private final String[] cols = {"ID", "Nombre", "Empleados", "Promedio"};
        
        @Override
        public int getRowCount() { return gd.listar().size(); }
        @Override
        public int getColumnCount() { return cols.length; }
        @Override
        public String getColumnName(int col) { return cols[col]; }
        
        @Override
        public Object getValueAt(int row, int col) {
            Departamento d = (Departamento)gd.listar().toArray()[row];
            return switch (col) {
                case 0 -> d.getId();
                case 1 -> d.getNombre();
                case 2 -> d.getEmpleados().size();
                case 3 -> String.format("%.2f", d.getDesempenoPromedio());
                default -> null;
            };
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CompuWorkApp().setVisible(true));
    }
}
