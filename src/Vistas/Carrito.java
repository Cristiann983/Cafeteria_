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
import javax.swing.JOptionPane;
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

    private Persistencia persis;
    private Producto entityproducto;
    private ProductoJpaController jpaproducto;
    private Factura entityFactura;
    private InsumoJpaController jpainsumo;
    private Insumo entityInsumo;
    private Pedido entitypedido;
    private PedidoJpaController jpapedido;
    private Recetainsumo entityrecetai;
    private EnumStatus estatus;
    private EnumStatusJpaController jpaestatus;
    private RecetainsumoJpaController jparecetai;
    private FacturaJpaController jpaFactura;
    private DefaultTableModel tabla;
    private PedidoproductoJpaController jpapedidop;
    private Pedidoproducto entitypedidop;
    private String[] columnas = {"Id Pedido", "Producto", "Cantidad", "Estatus"};

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
        jpaproducto = new ProductoJpaController(emf);
        jpapedidop = new PedidoproductoJpaController(emf);
        jparecetai = new RecetainsumoJpaController(emf);
        jpainsumo = new InsumoJpaController(emf);
        jpapedido = new PedidoJpaController(emf);
        jpaestatus = new EnumStatusJpaController(emf);
    }

    private void llenarComboPedidosEnProceso() {
        try {
            comboPedido.removeAllItems();
            Set<Integer> pedidosAgregados = new HashSet<>();

            for (Pedidoproducto pp : jpapedidop.findPedidoproductoEntities()) {
                Pedido pedido = pp.getIdPedido();
                int id = pedido.getIdPedido();
                String estado = pedido.getIdStatus().getDescripcion();

                if ("En proceso".equalsIgnoreCase(estado) && pedidosAgregados.add(id)) {
                    comboPedido.addItem(String.valueOf(id));
                }
            }

            if (comboPedido.getItemCount() == 0) {
                comboPedido.addItem("No hay pedidos");
            }

        } catch (Exception e) {
            mostrarError("Error al llenar combo", e);
        }
    }

    private void calcularTotalesPedido(int idPedido) {
        double subtotal = 0;

        for (Pedidoproducto pp : jpapedidop.findPedidoproductoEntities()) {
            if (pp.getIdPedido().getIdPedido() == idPedido) {
                subtotal += pp.getCantidad() * pp.getIdProducto().getPrecio();
            }
        }

        double iva = subtotal * 0.16;
        double total = subtotal + iva;

        txtSubtotal.setText("$" + String.format("%.2f", subtotal));
        txtIVA.setText("$" + String.format("%.2f", iva));
        txtTotal.setText("$" + String.format("%.2f", total));

    }

    private void cargarProductosEnTabla(int idPedido) {
        tabla.setRowCount(0); // Limpiar tabla

        for (Pedidoproducto pp : jpapedidop.findPedidoproductoEntities()) {
            if (pp.getIdPedido().getIdPedido() == idPedido) {
                tabla.addRow(new Object[]{
                    idPedido,
                    pp.getIdProducto().getDescripcion(),
                    pp.getCantidad(),
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

        int idPedido = Integer.parseInt(idPedidoStr);
        cargarProductosEnTabla(idPedido);
        calcularTotalesPedido(idPedido);
    }

    private void mostrarError(String mensaje, Exception e) {
        JOptionPane.showMessageDialog(null, mensaje + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
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
        btnFinalizar = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
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

        btnFinalizar.setText("Aceptar");
        btnFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(269, 269, 269)
                                .addComponent(jLabel1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(154, 154, 154)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(comboPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(btnAceptar)))
                        .addGap(0, 146, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(209, 209, 209))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                            .addComponent(txtIVA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(110, 110, 110))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(552, Short.MAX_VALUE)
                    .addComponent(btnFinalizar)
                    .addGap(19, 19, 19)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAceptar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSubtotal))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtIVA))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(481, Short.MAX_VALUE)
                    .addComponent(btnFinalizar)
                    .addGap(5, 5, 5)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        try {
            procesarPedidoSeleccionado();
        } catch (Exception e) {
            mostrarError("Error al procesar el pedido", e);
        }
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            tabla.setRowCount(0);
            comboPedido.removeAllItems();
            txtSubtotal.setText("$0.00");
            txtIVA.setText("$0.00");
            txtTotal.setText("$0.00");
            
            llenarComboPedidosEnProceso();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalizarActionPerformed
        try {
            String idPedidoStr = (String) comboPedido.getSelectedItem();
            if (idPedidoStr == null || idPedidoStr.equals("No hay pedidos")) {
                JOptionPane.showMessageDialog(null, "Selecciona un pedido válido.");
                return;
            }

            int idPedido = Integer.parseInt(idPedidoStr);
            List<Pedidoproducto> productosDelPedido = new ArrayList<>();

            for (Pedidoproducto pp : jpapedidop.findPedidoproductoEntities()) {
                if (pp.getIdPedido().getIdPedido() == idPedido) {
                    productosDelPedido.add(pp);
                }
            }

            for (Pedidoproducto pp : productosDelPedido) {
                Producto producto = pp.getIdProducto();
                int cantidadProducto = pp.getCantidad();

                for (Recetainsumo ri : jparecetai.findRecetainsumoEntities()) {
                    if (ri.getReceta().getIdReceta() == producto.getIdProducto()) {
                        Insumo insumo = ri.getInsumo();
                        double cantidadNecesaria = cantidadProducto * ri.getCantidad();
                        double nuevaCantidad = insumo.getCantidad() - cantidadNecesaria;

                        if (nuevaCantidad < 0) {
                            throw new Exception("No hay suficiente stock del insumo: " + insumo.getNombre());
                        }

                        insumo.setCantidad((int) nuevaCantidad);
                        jpainsumo.edit(insumo);
                    }
                }
            }

            Pedido pedido = productosDelPedido.get(0).getIdPedido();
            EnumStatus status = jpaestatus.findEnumStatus(3);
            pedido.setIdStatus(status);
            jpapedido.edit(pedido);

            tabla.setRowCount(0);
            comboPedido.removeAllItems();
            txtSubtotal.setText("$0.00");
            txtIVA.setText("$0.00");
            txtTotal.setText("$0.00");
            llenarComboPedidosEnProceso();
            JOptionPane.showMessageDialog(null, "Pedido finalizado y stock actualizado correctamente.");

        } catch (Exception e) {
            mostrarError("Error al finalizar el pedido", e);
        }


    }//GEN-LAST:event_btnFinalizarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnFinalizar;
    private javax.swing.JComboBox<String> comboPedido;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel txtIVA;
    private javax.swing.JLabel txtSubtotal;
    private javax.swing.JLabel txtTotal;
    // End of variables declaration//GEN-END:variables
}
