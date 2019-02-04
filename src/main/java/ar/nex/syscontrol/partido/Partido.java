package ar.nex.syscontrol.partido;

import ar.nex.syscontrol.cliente.Cliente;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "partido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Partido.findAll", query = "SELECT p FROM Partido p")
    , @NamedQuery(name = "Partido.findById", query = "SELECT p FROM Partido p WHERE p.id = :id")
    , @NamedQuery(name = "Partido.findByNombre", query = "SELECT p FROM Partido p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Partido.findByFecha", query = "SELECT p FROM Partido p WHERE p.fecha = :fecha")
    , @NamedQuery(name = "Partido.findByHora", query = "SELECT p FROM Partido p WHERE p.hora = :hora")
    , @NamedQuery(name = "Partido.findByEstado", query = "SELECT p FROM Partido p WHERE p.estado = :estado")
    , @NamedQuery(name = "Partido.findByComentario", query = "SELECT p FROM Partido p WHERE p.comentario = :comentario")
    , @NamedQuery(name = "Partido.findByOtro", query = "SELECT p FROM Partido p WHERE p.otro = :otro")})
public class Partido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "fecha")
    private String fecha;
    @Column(name = "hora")
    private String hora;
    @Column(name = "estado")
    private Integer estado;
    @Column(name = "comentario")
    private String comentario;
    @Column(name = "otro")
    private Integer otro;
    @ManyToMany(mappedBy = "partidoCollection")
    private Collection<Cliente> clienteCollection;

    public Partido() {
    }

    public Partido(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<Cliente> getClienteCollection() {
        return clienteCollection;
    }

    public void setClienteCollection(Collection<Cliente> clienteCollection) {
        this.clienteCollection = clienteCollection;
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
        if (!(object instanceof Partido)) {
            return false;
        }
        Partido other = (Partido) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Partido[ " + id + " ][ Dia: " + fecha + " - Hora: " + hora + " ]";
    }

}
