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
@Table(name = "caja_mov_cliente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CajaMovCliente.findAll", query = "SELECT c FROM CajaMovCliente c")
    , @NamedQuery(name = "CajaMovCliente.findById", query = "SELECT c FROM CajaMovCliente c WHERE c.id = :id")
    , @NamedQuery(name = "CajaMovCliente.findByClienteId", query = "SELECT c FROM CajaMovCliente c WHERE c.clienteId = :clienteId")
    , @NamedQuery(name = "CajaMovCliente.findByFechaCompra", query = "SELECT c FROM CajaMovCliente c WHERE c.fechaCompra = :fechaCompra")
    , @NamedQuery(name = "CajaMovCliente.findByArticulo", query = "SELECT c FROM CajaMovCliente c WHERE c.articulo = :articulo")
    , @NamedQuery(name = "CajaMovCliente.findByImporte", query = "SELECT c FROM CajaMovCliente c WHERE c.importe = :importe")
    , @NamedQuery(name = "CajaMovCliente.findByEstado", query = "SELECT c FROM CajaMovCliente c WHERE c.estado = :estado")
    , @NamedQuery(name = "CajaMovCliente.findByComentario", query = "SELECT c FROM CajaMovCliente c WHERE c.comentario = :comentario")
    , @NamedQuery(name = "CajaMovCliente.findByFechaPago", query = "SELECT c FROM CajaMovCliente c WHERE c.fechaPago = :fechaPago")})
public class CajaMovCliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "cliente_id")
    private Integer clienteId;    
    @Column(name = "fecha_compra")
    private String fechaCompra;    
    @Column(name = "articulo")
    private String articulo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "importe")
    private Double importe;
    @Column(name = "estado")
    private Integer estado;
    @Column(name = "comentario")
    private String comentario;
    @Column(name = "fecha_pago")
    private String fechaPago;

    public CajaMovCliente() {
        this.estado = 0;
        this.importe = 0.0;
    }

    public CajaMovCliente(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
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
        if (!(object instanceof CajaMovCliente)) {
            return false;
        }
        CajaMovCliente other = (CajaMovCliente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return articulo + " - $" + importe;
    }
    
}
