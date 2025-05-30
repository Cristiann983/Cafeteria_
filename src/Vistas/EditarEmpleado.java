/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vistas;

import entity.Empleado;
import entity.EnumRol;
import entity.EnumTurno;
import entity.Persistencia;
import entity.Turno;
import entity.Usuario;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import jpaController.EmpleadoJpaController;
import jpaController.EnumRolJpaController;
import jpaController.EnumTurnoJpaController;
import jpaController.TurnoJpaController;
import jpaController.UsuarioJpaController;

/**
 *
 * @author crist
 */
public class EditarEmpleado extends javax.swing.JPanel {
 private Persistencia persis;
    private EnumRolJpaController rolJPA;
    private UsuarioJpaController usuarioJPA;
    private EmpleadoJpaController empleadoJPA;
    private EnumTurnoJpaController enumturnoJPA;
    private TurnoJpaController turnoJPA;
    
    // Variables para el empleado a editar
    private Empleado empleadoAEditar;
    private Empleados panelPadre;

    /**
     * Creates new form agregarEmpleado
     */
    public EditarEmpleado(Empleado empleado, Empleados padre) {
       this.empleadoAEditar = empleado;
        this.panelPadre = padre;
        
        initComponents();
        persis = new Persistencia();
        EntityManagerFactory emf = persis.getEmf();
        
        rolJPA = new EnumRolJpaController(emf);
        usuarioJPA = new UsuarioJpaController(emf);
        empleadoJPA = new EmpleadoJpaController(emf);
        enumturnoJPA = new EnumTurnoJpaController(emf);
        turnoJPA = new TurnoJpaController(emf);
        
        llenarComboRoles();
        llenarComboTurnos();
        cargarDatosEmpleado(); // Cargar datos del empleado a editar
        configurarInterfazParaEdicion();
    }
        public EditarEmpleado() {
        this.empleadoAEditar = null;
        this.panelPadre = null;
        
        initComponents();
        persis = new Persistencia();
        // ... resto del código original
        llenarComboRoles();
        llenarComboTurnos();
    }
        public void llenarComboTurnos() {
        try {
            List<EnumTurno> listurno = enumturnoJPA.findEnumTurnoEntities();
            comboTurno.removeAllItems();
            for (EnumTurno turno : listurno) {
                comboTurno.addItem(turno.getDescripcion());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al llenar Datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void llenarComboRoles() {
        try {
            List<EnumRol> listrol = rolJPA.findEnumRolEntities();
            comboRol.removeAllItems();
            for (EnumRol rol : listrol) {
                comboRol.addItem(rol.getDescripcion());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al llenar Datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
        
        private void cargarDatosEmpleado() {
    if (empleadoAEditar != null) {
        // Cargar datos básicos
        txtNombre.setText(empleadoAEditar.getNombre());
        txtApellidos.setText(empleadoAEditar.getApellidos());
        
        // Cargar datos del usuario
        Usuario usuario = empleadoAEditar.getIdUsuario();
        if (usuario != null) {
            TxtUsuario.setText(usuario.getNombreU());
            password.setText(usuario.getContrasena());
            
            // Seleccionar el rol en el combo
            String rolActual = usuario.getIdRol().getDescripcion();
            comboRol.setSelectedItem(rolActual);
        }
        
        // Cargar turno actual
        if (!empleadoAEditar.getTurnoCollection().isEmpty()) {
            // Obtener el primer turno (asumiendo que tiene uno)
            Turno turnoActual = empleadoAEditar.getTurnoCollection().iterator().next();
            String turnoDescripcion = turnoActual.getIdTurnoTipo().getDescripcion();
            comboTurno.setSelectedItem(turnoDescripcion);
            }
        }
    }
       private void configurarInterfazParaEdicion() {
        if (empleadoAEditar != null) {
         // Cambiar títulos y textos para modo edición
        jLabel1.setText("E D I T A R   E M P L E A D O");
        btnAgregar.setText("Guardar Cambios");
        
        // Opcional: deshabilitar campo de usuario si no quieres que se cambie
        // TxtUsuario.setEnabled(false);
        }
    }
       private void editarEmpleado(String nombre, String apellidos, String nombreUsuario, 
                           String contrasena, String rolSeleccionado, String turnoSeleccionado) {
    
    // Confirmar cambios
    int confirmacion = JOptionPane.showConfirmDialog(this,
        "¿Está seguro que desea guardar los cambios?\n\n" +
        "Empleado: " + nombre + " " + apellidos + "\n" +
        "Usuario: " + nombreUsuario + "\n" +
        "Rol: " + rolSeleccionado + "\n" +
        "Turno: " + turnoSeleccionado,
        "Confirmar cambios",
        JOptionPane.YES_NO_OPTION);
    
    if (confirmacion != JOptionPane.YES_OPTION) {
        return;
    }
    
    try {
        // 1. Actualizar datos del empleado
        empleadoAEditar.setNombre(nombre);
        empleadoAEditar.setApellidos(apellidos);
        
        // 2. Actualizar datos del usuario
        Usuario usuario = empleadoAEditar.getIdUsuario();
        usuario.setNombreU(nombreUsuario);
        usuario.setContrasena(contrasena);
        
        // 3. Actualizar rol
        List<EnumRol> roles = rolJPA.findEnumRolEntities();
        for (EnumRol rol : roles) {
            if (rol.getDescripcion().equals(rolSeleccionado)) {
                usuario.setIdRol(rol);
                break;
            }
        }
        
        // 4. Guardar cambios del usuario
        usuarioJPA.edit(usuario);
        
        // 5. Guardar cambios del empleado
        empleadoJPA.edit(empleadoAEditar);
        
        // 6. Actualizar turno si cambió
        actualizarTurnoEmpleado(turnoSeleccionado);
        
        JOptionPane.showMessageDialog(this, 
            "Empleado actualizado exitosamente", 
            "Actualización exitosa", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // 7. Recargar tabla en el panel padre y volver
        if (panelPadre != null) {
            panelPadre.cargarEmpleados();
            MainAdmin.showJpane(panelPadre);
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error al actualizar empleado: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    }
       private void actualizarTurnoEmpleado(String turnoSeleccionado) {
    try {
        // Buscar el nuevo tipo de turno
        EnumTurno nuevoTipoTurno = null;
        List<EnumTurno> listTurnos = enumturnoJPA.findEnumTurnoEntities();
        for (EnumTurno turno : listTurnos) {
            if (turno.getDescripcion().equals(turnoSeleccionado)) {
                nuevoTipoTurno = turno;
                break;
            }
        }
        
        if (nuevoTipoTurno == null) {
            return;
        }
        
        // Obtener el turno actual del empleado
        if (!empleadoAEditar.getTurnoCollection().isEmpty()) {
            Turno turnoActual = empleadoAEditar.getTurnoCollection().iterator().next();
            
            // Verificar si el turno cambió
            if (!turnoActual.getIdTurnoTipo().getDescripcion().equals(turnoSeleccionado)) {
                // Actualizar el turno existente
                turnoActual.setIdTurnoTipo(nuevoTipoTurno);
                turnoActual.setFecha(new Date()); // Actualizar fecha del cambio
                turnoJPA.edit(turnoActual);
            }
        } else {
            // Si no tiene turno, crear uno nuevo
            crearTurno(empleadoAEditar, nuevoTipoTurno, new Date());
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error al actualizar turno: " + e.getMessage(),
            "Error",
            JOptionPane.WARNING_MESSAGE);
    }
}
       private void agregarNuevoEmpleado(String nombre, String apellidos, String nombreUsuario, 
                                 String contrasena, String rolSeleccionado, String turnoSeleccionado) {
    try {
        // Tu código original para agregar empleado
        Usuario usuario = new Usuario();
        usuario.setNombreU(nombreUsuario);
        usuario.setContrasena(contrasena);

        List<EnumRol> roles = rolJPA.findEnumRolEntities();
        for (EnumRol rol : roles) {
            if (rol.getDescripcion().equals(rolSeleccionado)) {
                usuario.setIdRol(rol);
                break;
            }
        }
        usuarioJPA.create(usuario);

        Empleado empleado = new Empleado();
        empleado.setNombre(nombre);
        empleado.setApellidos(apellidos);
        empleado.setIdUsuario(usuario);
        empleadoJPA.create(empleado);

        List<EnumTurno> listurnos = enumturnoJPA.findEnumTurnoEntities();
        for (EnumTurno tu : listurnos) {
            if (tu.getDescripcion().equals(turnoSeleccionado)) {
                crearTurno(empleado, tu, new Date());
                break;
            }
        }

        JOptionPane.showMessageDialog(this, "Empleado agregado exitosamente.");
        limpiarDatos();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al agregar empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }
        public void limpiarDatos() {
        txtNombre.setText("");
        txtApellidos.setText("");
        TxtUsuario.setText("");
        password.setText("");
         }
         public void crearTurno(Empleado empleado, EnumTurno tipoTurno, Date fecha) {
        Turno turno = new Turno();
        turno.setIdEmpleado(empleado);
        turno.setIdTurnoTipo(tipoTurno);
        turno.setFecha(fecha);

        turnoJPA.create(turno);
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
        btnCancelar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        comboRol = new javax.swing.JComboBox<>();
        comboTurno = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        TxtUsuario = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();

        setPreferredSize(new java.awt.Dimension(493, 450));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("A G R E G A R  N U E VO  E M P L E A D O");

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        jLabel2.setText("Nombre ");

        jLabel3.setText("Apellidos");

        jLabel4.setText("Turno");

        jLabel5.setText("Rol empleado");

        txtApellidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellidosActionPerformed(evt);
            }
        });
        txtApellidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidosKeyTyped(evt);
            }
        });

        comboRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        jLabel6.setText("Usuario ");

        TxtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtUsuarioActionPerformed(evt);
            }
        });

        jLabel7.setText("Contraseña");

        password.setText("jPasswordField1");
        password.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                passwordMousePressed(evt);
            }
        });
        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(80, 80, 80))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(comboRol, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap()))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(TxtUsuario)
                                .addComponent(password, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                            .addComponent(comboTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(73, 73, 73))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 119, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnCancelar))
                .addContainerGap(96, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
            String mensaje = empleadoAEditar != null ? 
        "¿Está seguro que desea cancelar la edición? Los cambios no guardados se perderán." :
        "¿Está seguro que desea cancelar?";
    
    int confirmacion = JOptionPane.showConfirmDialog(this,
        mensaje,
        "Confirmar cancelación",
        JOptionPane.YES_NO_OPTION);
    
    if (confirmacion == JOptionPane.YES_OPTION) {
        if (panelPadre != null) {
            MainAdmin.showJpane(panelPadre);
        } else {
            // Lógica para cuando se usa como agregar
            limpiarDatos();
        }
    }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
            String nombre = txtNombre.getText().trim();
    String apellidos = txtApellidos.getText().trim();
    String nombreUsuario = TxtUsuario.getText().trim();
    String contrasena = new String(password.getPassword());
    String rolSeleccionado = comboRol.getSelectedItem() + "";
    String turnoSeleccionado = comboTurno.getSelectedItem() + "";
    
    // Validaciones
    if (nombre.isEmpty() || apellidos.isEmpty() || nombreUsuario.isEmpty() || contrasena.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Verificar si es modo edición o agregar
    if (empleadoAEditar != null) {
        editarEmpleado(nombre, apellidos, nombreUsuario, contrasena, rolSeleccionado, turnoSeleccionado);
    } else {
        agregarNuevoEmpleado(nombre, apellidos, nombreUsuario, contrasena, rolSeleccionado, turnoSeleccionado);
    }

    }//GEN-LAST:event_btnAgregarActionPerformed

    private void txtApellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidosActionPerformed

    private void TxtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtUsuarioActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void passwordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_passwordKeyPressed

    private void passwordMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_passwordMousePressed
        password.setText("");
    }//GEN-LAST:event_passwordMousePressed

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isLetter(c) && !Character.isWhitespace(c) && !Character.isISOControl(c)) {
            JOptionPane.showMessageDialog(null, "Solo se permiten letras y espacios.");
            evt.consume();
            getToolkit().beep();
        }

    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtApellidosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidosKeyTyped
         char c = evt.getKeyChar();

        if (!Character.isLetter(c) && !Character.isWhitespace(c) && !Character.isISOControl(c)) {
            JOptionPane.showMessageDialog(null, "Solo se permiten letras y espacios.");
            evt.consume();
            getToolkit().beep();
        }
    }//GEN-LAST:event_txtApellidosKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TxtUsuario;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JComboBox<String> comboRol;
    private javax.swing.JComboBox<String> comboTurno;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
