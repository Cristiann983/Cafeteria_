/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author crist
 */
@Entity
@Table(name = "tipocantidad")
@NamedQueries({
    @NamedQuery(name = "Tipocantidad.findAll", query = "SELECT t FROM Tipocantidad t"),
    @NamedQuery(name = "Tipocantidad.findByIdTipo", query = "SELECT t FROM Tipocantidad t WHERE t.idTipo = :idTipo"),
    @NamedQuery(name = "Tipocantidad.findByNombre", query = "SELECT t FROM Tipocantidad t WHERE t.nombre = :nombre")})
public class Tipocantidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo")
    private Integer idTipo;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "idTipocantidad")
    private Collection<Insumo> insumoCollection;

    public Tipocantidad() {
    }

    public Tipocantidad(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public Tipocantidad(Integer idTipo, String nombre) {
        this.idTipo = idTipo;
        this.nombre = nombre;
    }

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Collection<Insumo> getInsumoCollection() {
        return insumoCollection;
    }

    public void setInsumoCollection(Collection<Insumo> insumoCollection) {
        this.insumoCollection = insumoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipo != null ? idTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipocantidad)) {
            return false;
        }
        Tipocantidad other = (Tipocantidad) object;
        if ((this.idTipo == null && other.idTipo != null) || (this.idTipo != null && !this.idTipo.equals(other.idTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Tipocantidad[ idTipo=" + idTipo + " ]";
    }
    
}
