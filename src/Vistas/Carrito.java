/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vistas;

import entity.EnumStatus;
import entity.Factura;
import entity.Insumo;
import entity.Pedido;
import entity.Pedidoproducto;
import entity.Persistencia;
import entity.Producto;
import entity.Recetainsumo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import jpaController.EnumStatusJpaController;
import jpaController.FacturaJpaController;
import jpaController.InsumoJpaController;
import jpaController.PedidoJpaController;
import jpaController.PedidoproductoJpaController;
import jpaController.ProductoJpaController;
import jpaController.RecetainsumoJpaController;

/**
 *
 * @author juanm
 */
public class Carrito extends javax.swing.JPanel {

    private double totalVista;
    private Persistencia persis;
    private ProductoJpaController jpaProducto;
    private InsumoJpaController jpaInsumo;
    private PedidoJpaController jpaPedido;
    private RecetainsumoJpaController jpaRecetaInsumo;
    private EnumStatusJpaController jpaStatus;
    private PedidoproductoJpaController jpaPedidoProducto;
    private DefaultTableModel tabla;
    private final String[] columnas = {"Id Pedido","Id Mesa", "Producto", "Cantidad", "Precio" , "Estatus De Mesa"};
    
    /**
     * Creates new form Carrito
     */
    public Carrito() {
        initComponents();
        inicializarControladores();
        inicializarTabla();
        llenarComboPedidosEnProceso();
    }

    private void inicializarTabla() {
        tabla = new DefaultTableModel(null, columnas);
        jTable1.setModel(tabla);
    }

    private void inicializarControladores() {
        persis = new Persistencia();
        EntityManagerFactory emf = persis.getEmf();
        jpaProducto = new ProductoJpaController(emf);
        jpaPedidoProducto = new PedidoproductoJpaController(emf);
        jpaRecetaInsumo = new RecetainsumoJpaController(emf);
        jpaInsumo = new InsumoJpaController(emf);
        jpaPedido = new PedidoJpaController(emf);
        jpaStatus = new EnumStatusJpaController(emf);
    }

    private void llenarComboPedidosEnProceso() {
        try {
            comboPedido.removeAllItems();
            Set<Integer> pedidosAgregados = new HashSet<>();

            for (Pedidoproducto pp : jpaPedidoProducto.findPedidoproductoEntities()) {
                Pedido pedido = pp.getIdPedido();
                int id = pedido.getIdPedido();
                String estado = pedido.getIdStatus().getDescripcion();

                if ("Mesa Ocupada".equalsIgnoreCase(estado) && pedidosAgregados.add(id)) {
                    comboPedido.addItem(String.valueOf(id));
                }
            }

            if (comboPedido.getItemCount() == 0) {
                comboPedido.addItem("No hay pedidos");
            }

        } catch (Exception e) {
            mostrarError("Error al llenar combo de pedidos", e);
        }
    }

    private void calcularTotalesPedido(int idPedido) {
        double subtotal = 0;

        for (Pedidoproducto pp : jpaPedidoProducto.findPedidoproductoEntities()) {
            if (pp.getIdPedido().getIdPedido() == idPedido) {
                subtotal += pp.getCantidad() * pp.getIdProducto().getPrecio();
            }
        }

        double iva = subtotal * 0.16;
        double total = subtotal + iva;
        totalVista = total;

        txtSubtotal.setText(String.format("$%.2f", subtotal));
        txtIVA.setText(String.format("$%.2f", iva));
        txtTotal.setText(String.format("$%.2f", total));
    }

   private void cargarProductosEnTabla(int idPedido) {
    tabla.setRowCount(0);

    for (Pedidoproducto pp : jpaPedidoProducto.findPedidoproductoEntities()) {
        if (pp.getIdPedido().getIdPedido() == idPedido) {
            int cantidad = pp.getCantidad();
            double precioUnitario = pp.getIdProducto().getPrecio();
            double precioTotal = cantidad * precioUnitario;

            tabla.addRow(new Object[] {
                idPedido,
                pp.getIdPedido().getMesa(),
                pp.getIdProducto().getDescripcion(),
                cantidad,
                precioTotal, 
                pp.getIdPedido().getIdStatus().getDescripcion()
            });
        }
    }
}


    private void procesarPedidoSeleccionado() {
        String idPedidoStr = (String) comboPedido.getSelectedItem();
        if (idPedidoStr == null || idPedidoStr.equals("No hay pedidos")) {
            JOptionPane.showMessageDialog(null, "Selecciona un pedido válido.");
            return;
        }

        try {
            int idPedido = Integer.parseInt(idPedidoStr);
            cargarProductosEnTabla(idPedido);
            calcularTotalesPedido(idPedido);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID de pedido inválido.");
        }
    }

    private void mostrarError(String mensaje, Exception e) {
        JOptionPane.showMessageDialog(null, mensaje + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean hayStockDisponible(int idPedido) {
        try {
            // Obtener productos del pedido
            List<Pedidoproducto> productosDelPedido = new ArrayList<>();
            for (Pedidoproducto pp : jpaPedidoProducto.findPedidoproductoEntities()) {
                if (pp.getIdPedido().getIdPedido() == idPedido) {
                    productosDelPedido.add(pp);
                }
            }

            // Verificar stock de insumos para cada producto
            for (Pedidoproducto pp : productosDelPedido) {
                Producto producto = pp.getIdProducto();
                int cantidadProducto = pp.getCantidad();

                for (Recetainsumo ri : jpaRecetaInsumo.findRecetainsumoEntities()) {
                    if (ri.getReceta().getIdReceta() == producto.getIdProducto()) {
                        Insumo insumo = ri.getInsumo();
                        double cantidadNecesaria = cantidadProducto * ri.getCantidad();
                        if (insumo.getCantidad() < cantidadNecesaria) {
                            JOptionPane.showMessageDialog(null,
                                    "No hay suficiente stock del insumo: " + insumo.getNombre(),
                                    "Stock insuficiente",
                                    JOptionPane.WARNING_MESSAGE);
                            return false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al verificar stock: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void abrirVentanaFactura() {
        String idPedidoStr = (String) comboPedido.getSelectedItem();
        if (idPedidoStr == null || idPedidoStr.equals("No hay pedidos")) {
            JOptionPane.showMessageDialog(null, "Selecciona un pedido válido.");
            return;
        }

        try {
            int idPedido = Integer.parseInt(idPedidoStr);

            if (!hayStockDisponible(idPedido)) {
                return;
            }

            // Crear y configurar la vista de factura
            VistaFactura vistaFactura = new VistaFactura();
            vistaFactura.setIdPedido(idPedido);
            vistaFactura.setTotalAPagar(totalVista);
            limpiarVista();

            // Mostrar en nueva ventana
            JFrame frame = new JFrame("Factura");
            frame.setContentPane(vistaFactura);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Eliminar el pedido del combo por valor (no por índice)
            comboPedido.removeItem(String.valueOf(idPedido));

            if (comboPedido.getItemCount() == 0) {
                comboPedido.addItem("No hay pedidos");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID de pedido inválido.");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAceptarv = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comboPedido = new javax.swing.JComboBox<>();
        btnAceptar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtIVA = new javax.swing.JLabel();
        txtSubtotal = new javax.swing.JLabel();
        txtTotal = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btnPago = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Carrito");

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

        jLabel3.setFont(new java.awt.Font("Bookman Old Style", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("ID De Pedido:");

        comboPedido.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bookman Old Style", 0, 14)); // NOI18N
        jLabel4.setText("TOTAL:");

        jLabel5.setFont(new java.awt.Font("Bookman Old Style", 0, 14)); // NOI18N
        jLabel5.setText("SUBTOTAL:");

        jLabel6.setFont(new java.awt.Font("Bookman Old Style", 0, 14)); // NOI18N
        jLabel6.setText("IVA:");

        txtIVA.setFont(new java.awt.Font("Bookman Old Style", 3, 14)); // NOI18N
        txtIVA.setText("SUBTOTAL:");

        txtSubtotal.setFont(new java.awt.Font("Bookman Old Style", 3, 14)); // NOI18N
        txtSubtotal.setText("SUBTOTAL:");

        txtTotal.setFont(new java.awt.Font("Bookman Old Style", 3, 14)); // NOI18N
        txtTotal.setText("SUBTOTAL:");

        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnPago.setText("Aceptar");
        btnPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout btnAceptarvLayout = new javax.swing.GroupLayout(btnAceptarv);
        btnAceptarv.setLayout(btnAceptarvLayout);
        btnAceptarvLayout.setHorizontalGroup(
            btnAceptarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnAceptarvLayout.createSequentialGroup()
                .addGroup(btnAceptarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(btnAceptarvLayout.createSequentialGroup()
                        .addGap(154, 154, 154)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(comboPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(btnAceptar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnAceptarvLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(btnAceptarvLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnAceptarvLayout.createSequentialGroup()
                .addGroup(btnAceptarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(btnAceptarvLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(btnAceptarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addGroup(btnAceptarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(btnAceptarvLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48))
                            .addGroup(btnAceptarvLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(btnAceptarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                    .addComponent(txtIVA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(btnAceptarvLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addGap(35, 35, 35)
                .addComponent(btnPago)
                .addGap(54, 54, 54))
        );
        btnAceptarvLayout.setVerticalGroup(
            btnAceptarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnAceptarvLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addGroup(btnAceptarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAceptar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(btnAceptarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSubtotal))
                .addGap(18, 18, 18)
                .addGroup(btnAceptarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtIVA))
                .addGap(18, 18, 18)
                .addGroup(btnAceptarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(btnAceptarvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btnPago))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnAceptarv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnAceptarv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        try {
            procesarPedidoSeleccionado();
        } catch (Exception e) {
            mostrarError("Error al procesar el pedido", e);
        }
    }//GEN-LAST:event_btnAceptarActionPerformed
    private void limpiarVista() {
        tabla.setRowCount(0);
        txtSubtotal.setText("$0.00");
        txtIVA.setText("$0.00");
        txtTotal.setText("$0.00");

    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         String idPedidoStr = (String) comboPedido.getSelectedItem();
    if (idPedidoStr == null || idPedidoStr.equals("No hay pedidos")) {
        JOptionPane.showMessageDialog(null, "Selecciona un pedido válido.");
        return;
    }

    try {
        int idPedido = Integer.parseInt(idPedidoStr);

   
        Pedido pedido = jpaPedido.findPedido(idPedido);
        if (pedido != null) {
            
            EnumStatus status = jpaStatus.findEnumStatus(3);
            pedido.setIdStatus(status);

            jpaPedido.edit(pedido);
            comboPedido.removeItem(comboPedido.getSelectedItem());
            JOptionPane.showMessageDialog(null, "Estado del pedido actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Pedido no encontrado.");
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "El ID del pedido no es válido.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar el pedido: " + e.getMessage());
    }

    limpiarVista();
    }//GEN-LAST:event_jButton1ActionPerformed


    private void btnPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagoActionPerformed

        abrirVentanaFactura();

    }//GEN-LAST:event_btnPagoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JPanel btnAceptarv;
    private javax.swing.JButton btnPago;
    private javax.swing.JComboBox<String> comboPedido;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel txtIVA;
    private javax.swing.JLabel txtSubtotal;
    private javax.swing.JLabel txtTotal;
    // End of variables declaration//GEN-END:variables
}
