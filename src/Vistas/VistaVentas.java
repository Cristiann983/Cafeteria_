/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vistas;

import entity.Pedido;
import entity.Pedidoproducto;
import entity.Persistencia;
import entity.Producto;
import entity.Venta;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import jpaController.EnumStatusJpaController;
import jpaController.InsumoJpaController;
import jpaController.PedidoJpaController;
import jpaController.PedidoproductoJpaController;
import jpaController.ProductoJpaController;
import jpaController.RecetainsumoJpaController;
import jpaController.VentaJpaController;

/**
 *
 * @author juanm
 */
public class VistaVentas extends javax.swing.JPanel {

    private VentaJpaController jpaVenta;
    private Integer pedidoSeleccionado = null;
    private double totales;
    private final ProductoJpaController jpaproducto;
    private final InsumoJpaController jpainsumo;
    private final PedidoJpaController jpapedido;
    private final RecetainsumoJpaController jparecetai;
    private final EnumStatusJpaController jpaestatus;
    private final PedidoproductoJpaController jpapedidop;
    private DefaultTableModel tabla;
    private final String[] columnas = {"Id Pedido", "Id venta", "Fecha", "Producto", "Cantidad", "Total","Estatus"};

    /**
     * Creates new form Venta
     */
    public VistaVentas() {
        initComponents();
        Persistencia persis = new Persistencia();
        EntityManagerFactory emf = persis.getEmf();
        jpaproducto = new ProductoJpaController(emf);
        jpainsumo = new InsumoJpaController(emf);
        jpapedido = new PedidoJpaController(emf);
        jparecetai = new RecetainsumoJpaController(emf);
        jpaestatus = new EnumStatusJpaController(emf);
        jpapedidop = new PedidoproductoJpaController(emf);
        jpaVenta = new VentaJpaController(emf);
        llenarComboPedidosEnProceso();
        inicializarTabla();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        comboFecha = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        btnDia = new javax.swing.JButton();
        btnMes = new javax.swing.JButton();
        btnAño = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Venta");

        comboFecha.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Fecha De Venta:");

        btnDia.setText("Dia");
        btnDia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiaActionPerformed(evt);
            }
        });

        btnMes.setText("Mes");
        btnMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMesActionPerformed(evt);
            }
        });

        btnAño.setText("Año");
        btnAño.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAñoActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel4.setFont(new java.awt.Font("Bookman Old Style", 0, 14)); // NOI18N
        jLabel4.setText("TOTAL:");

        txtTotal.setFont(new java.awt.Font("Bookman Old Style", 3, 14)); // NOI18N
        txtTotal.setText("SUBTOTAL:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 836, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(30, 30, 30)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(271, 271, 271)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnDia)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(btnMes)
                                .addGap(27, 27, 27)
                                .addComponent(btnAño))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(comboFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAño)
                    .addComponent(btnMes)
                    .addComponent(btnDia))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotal)
                    .addComponent(jLabel4))
                .addGap(101, 101, 101))
        );
    }// </editor-fold>//GEN-END:initComponents
  private void llenarComboPedidosEnProceso() {
        try {
            comboFecha.removeAllItems();
            comboFecha.addItem("Seleccione una fecha");
            Set<String> fechasAgregadas = new HashSet<>();

            for (Pedido p : jpapedido.findPedidoEntities()) {
                if ("Mesa Desocupada".equalsIgnoreCase(p.getIdStatus().getDescripcion())) {
                    Date fecha = p.getFecha();
                    if (fecha != null) {
                        String fechaFormateada = new SimpleDateFormat("yyyy-MM-dd").format(fecha);
                        if (fechasAgregadas.add(fechaFormateada)) {
                            comboFecha.addItem(fechaFormateada);
                        }
                    }
                }
            }

            if (comboFecha.getItemCount() == 1) {
                comboFecha.addItem("No hay pedidos");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al llenar combo de pedidos: " + e.getMessage());
        }
    }
    private void inicializarTabla() {
        tabla = new DefaultTableModel(null, columnas);
        jTable1.setModel(tabla);
    }

    private void cargarProductosFiltradosPorFecha(String tipoFiltro) {
        tabla.setRowCount(0);
        String fechaSeleccionada = (String) comboFecha.getSelectedItem();

        if (fechaSeleccionada == null || fechaSeleccionada.contains("Seleccione") || fechaSeleccionada.equals("No hay pedidos")) {
            return;
        }

        String parteFechaSeleccionada = "";
        switch (tipoFiltro.toLowerCase()) {
            case "anio":
                parteFechaSeleccionada = fechaSeleccionada.substring(0, 4); // yyyy
                break;
            case "mes":
                parteFechaSeleccionada = fechaSeleccionada.substring(0, 7); // yyyy-MM
                break;
            case "dia":
            default:
                parteFechaSeleccionada = fechaSeleccionada; // yyyy-MM-dd
                break;
        }

        double total = 0;

        for (Venta v : jpaVenta.findVentaEntities()) {
            Pedido pedido = v.getIdPedio();
            if (!"Mesa Desocupada".equalsIgnoreCase(pedido.getIdStatus().getDescripcion())) continue;

            Date fechaPedido = pedido.getFecha();
            if (fechaPedido == null) continue;

            String fechaPedidoFormateada = new SimpleDateFormat("yyyy-MM-dd").format(fechaPedido);
            String parteFechaPedido = "";

            switch (tipoFiltro.toLowerCase()) {
                case "anio":
                    parteFechaPedido = fechaPedidoFormateada.substring(0, 4);
                    break;
                case "mes":
                    parteFechaPedido = fechaPedidoFormateada.substring(0, 7);
                    break;
                case "dia":
                default:
                    parteFechaPedido = fechaPedidoFormateada;
                    break;
            }

            if (parteFechaSeleccionada.trim().equals(parteFechaPedido.trim())) {
                for (Pedidoproducto pp : jpapedidop.findPedidoproductoEntities()) {
                    if (pp.getIdPedido().getIdPedido().equals(pedido.getIdPedido())) {
                        int cantidad = pp.getCantidad();
                        double precioUnitario = pp.getIdProducto().getPrecio();
                        double precioTotal = cantidad * precioUnitario;
                        total += precioTotal;
                        String  estatus= "Pagado";
                        tabla.addRow(new Object[]{
                            pedido.getIdPedido(),
                            v.getIdVenta(),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pedido.getFecha()),
                            pp.getIdProducto().getDescripcion(),
                            cantidad,
                            precioTotal,
                            estatus
                        });
                    }
                }
            }
        }

        txtTotal.setText(String.format("%.2f", total));
    }



    private void btnDiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiaActionPerformed
        cargarProductosFiltradosPorFecha("dia");
    }//GEN-LAST:event_btnDiaActionPerformed

    private void btnMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMesActionPerformed
        cargarProductosFiltradosPorFecha("mes");
    }//GEN-LAST:event_btnMesActionPerformed

    private void btnAñoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAñoActionPerformed
        cargarProductosFiltradosPorFecha("anio");
    }//GEN-LAST:event_btnAñoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAño;
    private javax.swing.JButton btnDia;
    private javax.swing.JButton btnMes;
    private javax.swing.JComboBox<String> comboFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel txtTotal;
    // End of variables declaration//GEN-END:variables
}
