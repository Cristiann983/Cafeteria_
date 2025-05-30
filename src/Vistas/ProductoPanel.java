/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;

import entity.Persistencia;
import entity.Producto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import jpaController.PedidoJpaController;
import jpaController.PedidoproductoJpaController;



/**
 *
 * @author crist
 */
public class ProductoPanel extends JPanel {
    private Persistencia pers;
    private PedidoJpaController controladorPedido;
    private PedidoproductoJpaController controladorPedidoProducto;
    private Producto producto;
    private Productos_ parentPanel;
    private boolean pedidoActivo;
    
    public ProductoPanel(Producto producto, Productos_ parent, boolean pedidoActivo) {
        this.producto = producto;
        this.parentPanel = parent;
        this.pedidoActivo = pedidoActivo;
        this.pers = new Persistencia();
        this.controladorPedido = new PedidoJpaController(pers.getEmf());
        this.controladorPedidoProducto = new PedidoproductoJpaController(pers.getEmf());
        
        panelConElementos();
    }
    
    
    
    
    public void panelConElementos() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        JLabel titleLabel = new JLabel("PRECIO: $ " + producto.getPrecio());
        titleLabel.putClientProperty( "FlatLaf.style",  "font: semibold $h2.regular.font; " +"foreground: #1976D2" );
        add(titleLabel, gbc);
        
        // Imagen
        gbc.gridy = 1;
        ImageIcon icon = Productos_.byteArrayToImageIcon(producto.getImagenProducto());
        JLabel imageLabel = new JLabel();
        if (icon != null) {
            imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(120, 90, Image.SCALE_SMOOTH)));
        }
        add(imageLabel, gbc);
        
        // Descripción
        gbc.gridy = 2;
        JLabel descLabel = new JLabel(producto.getDescripcion());
        add(descLabel, gbc);
        
        // Botón
        gbc.gridy = 3;
        JButton btn = new JButton("Agregar al carrito");
        btn.setEnabled(pedidoActivo);
        if (!pedidoActivo) {
            btn.setText("Crear pedido primero");
            btn.setBackground(Color.LIGHT_GRAY);
        }
        
        btn.addActionListener(e -> {
            if (pedidoActivo && parentPanel != null) {
                parentPanel.agregarProductoAlCarrito(producto);
            }
        });
        add(btn, gbc);
    }
                }
    

    

