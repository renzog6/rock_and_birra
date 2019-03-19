package ar.nex.articulo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "stockDetalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockDetalle.findAll", query = "SELECT s FROM StockDetalle s"),
    @NamedQuery(name = "StockDetalle.findById", query = "SELECT s FROM StockDetalle s WHERE s.id = :id"),
    @NamedQuery(name = "StockDetalle.findByFecha", query = "SELECT s FROM StockDetalle s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "StockDetalle.findByEvento", query = "SELECT s FROM StockDetalle s WHERE s.evento = :evento"),
    @NamedQuery(name = "StockDetalle.findByCantidad", query = "SELECT s FROM StockDetalle s WHERE s.cantidad = :cantidad"),
    @NamedQuery(name = "StockDetalle.findByUsuario", query = "SELECT s FROM StockDetalle s WHERE s.usuario = :usuario")})
public class StockDetalle implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "articulo", referencedColumnName = "id")
    @ManyToOne
    private Articulo articulo;

    private static final long serialVersionUID = 1L;
    @Column(name = "fecha")
    private String fecha;
    @Column(name = "evento")
    private String evento;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "usuario")
    private String usuario;

    public StockDetalle() {
    }

    public StockDetalle(Integer id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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
        if (!(object instanceof StockDetalle)) {
            return false;
        }
        StockDetalle other = (StockDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.articulo.StockDetalle[ id=" + id + " ]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public String getEventoFull() {
        return fecha + " - " + evento + " - " + cantidad;
    }

}
