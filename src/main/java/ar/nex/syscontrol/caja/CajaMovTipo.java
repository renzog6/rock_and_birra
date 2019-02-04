/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.syscontrol.caja;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "caja_mov_tipo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CajaMovTipo.findAll", query = "SELECT c FROM CajaMovTipo c")
    , @NamedQuery(name = "CajaMovTipo.findById", query = "SELECT c FROM CajaMovTipo c WHERE c.id = :id")
    , @NamedQuery(name = "CajaMovTipo.findByNombre", query = "SELECT c FROM CajaMovTipo c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "CajaMovTipo.findByPrecio", query = "SELECT c FROM CajaMovTipo c WHERE c.importe = :importe")
    , @NamedQuery(name = "CajaMovTipo.findByComentario", query = "SELECT c FROM CajaMovTipo c WHERE c.comentario = :comentario")
    , @NamedQuery(name = "CajaMovTipo.findByOtro", query = "SELECT c FROM CajaMovTipo c WHERE c.otro = :otro")})
public class CajaMovTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "tipo")
    private Integer tipo;
    @Column(name = "nombre")
    private String nombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "importe")
    private Double importe;
    @Column(name = "comentario")
    private String comentario;
    @Column(name = "otro")
    private Integer otro;

    public CajaMovTipo() {
    }

    public CajaMovTipo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getOtro() {
        return otro;
    }

    public void setOtro(Integer otro) {
        this.otro = otro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CajaMovTipo)) {
            return false;
        }
        CajaMovTipo other = (CajaMovTipo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + " - $" + importe.toString();
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

}
