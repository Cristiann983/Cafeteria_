/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author juanm
 */
@Entity
@Table(name = "insumo")
@NamedQueries({
    @NamedQuery(name = "Insumo.findAll", query = "SELECT i FROM Insumo i"),
    @NamedQuery(name = "Insumo.findByIdInsumo", query = "SELECT i FROM Insumo i WHERE i.idInsumo = :idInsumo"),
    @NamedQuery(name = "Insumo.findByNombre", query = "SELECT i FROM Insumo i WHERE i.nombre = :nombre"),
    @NamedQuery(name = "Insumo.findByDescripcion", query = "SELECT i FROM Insumo i WHERE i.descripcion = :descripcion"),
    @NamedQuery(name = "Insumo.findByCantidad", query = "SELECT i FROM Insumo i WHERE i.cantidad = :cantidad")})
public class Insumo implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "precio_u")
    private BigDecimal precioU;
    @JoinColumn(name = "id_tipocantidad", referencedColumnName = "id_tipo")
    @ManyToOne
    private Tipocantidad idTipocantidad;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idInsumo")
    private Integer idInsumo;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private int cantidad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insumo")
    private Collection<Recetainsumo> recetainsumoCollection;

    public Insumo() {
    }

    public Insumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public Insumo(Integer idInsumo, String nombre, int cantidad) {
        this.idInsumo = idInsumo;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Collection<Recetainsumo> getRecetainsumoCollection() {
        return recetainsumoCollection;
    }

    public void setRecetainsumoCollection(Collection<Recetainsumo> recetainsumoCollection) {
        this.recetainsumoCollection = recetainsumoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInsumo != null ? idInsumo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Insumo)) {
            return false;
        }
        Insumo other = (Insumo) object;
        if ((this.idInsumo == null && other.idInsumo != null) || (this.idInsumo != null && !this.idInsumo.equals(other.idInsumo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Insumo[ idInsumo=" + idInsumo + " ]";
    }

    public BigDecimal getPrecioU() {
        return precioU;
    }

    public void setPrecioU(BigDecimal precioU) {
        this.precioU = precioU;
    }

    public Tipocantidad getIdTipocantidad() {
        return idTipocantidad;
    }

    public void setIdTipocantidad(Tipocantidad idTipocantidad) {
        this.idTipocantidad = idTipocantidad;
    }
    
}
