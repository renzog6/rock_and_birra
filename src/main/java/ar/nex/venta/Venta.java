package ar.nex.venta;

import ar.nex.articulo.Articulo;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "venta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v"),
    @NamedQuery(name = "Venta.findById", query = "SELECT v FROM Venta v WHERE v.id = :id"),
    @NamedQuery(name = "Venta.findByFecha", query = "SELECT v FROM Venta v WHERE v.fecha = :fecha"),
    @NamedQuery(name = "Venta.findByImporte", query = "SELECT v FROM Venta v WHERE v.importe = :importe"),
    @NamedQuery(name = "Venta.findByEstado", query = "SELECT v FROM Venta v WHERE v.estado = :estado"),
    @NamedQuery(name = "Venta.findByObservacion", query = "SELECT v FROM Venta v WHERE v.observacion = :observacion")})
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Column(name = "fecha")
    private String fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "importe")
    private Double importe;
    @Column(name = "estado")
    private Integer estado;
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "articuloID", referencedColumnName = "id")
    @ManyToOne
    private Articulo articuloID;
    @JoinColumn(name = "clienteID", referencedColumnName = "id")
    @ManyToOne
    private Cliente clienteID;

    public Venta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Articulo getArticuloID() {
        return articuloID;
    }

    public void setArticuloID(Articulo articuloID) {
        this.articuloID = articuloID;
    }

    public Cliente getClienteID() {
        return clienteID;
    }

    public void setClienteID(Cliente clienteID) {
        this.clienteID = clienteID;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.id;
        hash = 11 * hash + Objects.hashCode(this.fecha);
        hash = 11 * hash + Objects.hashCode(this.importe);
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
        final Venta other = (Venta) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.fecha, other.fecha)) {
            return false;
        }
        if (!Objects.equals(this.importe, other.importe)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "ar.nex.articulo.Venta[ ventaPK=";
    }
    
}
