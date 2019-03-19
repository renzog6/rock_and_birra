package ar.nex.articulo;

import ar.nex.venta.Venta;
import ar.nex.articulo.Articulo;
import ar.nex.compra.Compra;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
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
@Table(name = "pedido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pedido.findAll", query = "SELECT p FROM Pedido p"),
    @NamedQuery(name = "Pedido.findByCantidad", query = "SELECT p FROM Pedido p WHERE p.cantidad = :cantidad"),
    @NamedQuery(name = "Pedido.findByPrecio", query = "SELECT p FROM Pedido p WHERE p.precio = :precio"),
    @NamedQuery(name = "Pedido.findByEstado", query = "SELECT p FROM Pedido p WHERE p.estado = :estado"),
    @NamedQuery(name = "Pedido.findByObservacion", query = "SELECT p FROM Pedido p WHERE p.observacion = :observacion")})
public class Pedido implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    private static final long serialVersionUID = 1L;

    @Column(name = "cantidad")
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "precio")
    private Double precio;
    @Column(name = "estado")
    private Integer estado;
    @Column(name = "observacion")
    private String observacion;

    @JoinColumn(name = "articulo", referencedColumnName = "id")
    @ManyToOne
    private Articulo articulo;

    @JoinColumn(name = "compra", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Compra compra;

    @JoinColumn(name = "venta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Venta venta;

    public Pedido() {
        this.id = 0;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra id) {
        this.compra = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta id) {
        this.venta = id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.id);
        hash = 19 * hash + Objects.hashCode(this.cantidad);
        hash = 19 * hash + Objects.hashCode(this.compra);
        hash = 19 * hash + Objects.hashCode(this.venta);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pedido other = (Pedido) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.cantidad, other.cantidad)) {
            return false;
        }
        if (!Objects.equals(this.articulo, other.articulo)) {
            return false;
        }
        if (!Objects.equals(this.compra, other.compra)) {
            return false;
        }
        if (!Objects.equals(this.venta, other.venta)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pedido{" + "id=" + id + ", cantidad=" + cantidad + ", precio=" + precio + ", articulo=" + articulo + '}';
    }

    public double getTotal() {
        return cantidad * precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
