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
@Table(name = "caja_mov")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CajaMov.findAll", query = "SELECT c FROM CajaMov c")
    , @NamedQuery(name = "CajaMov.findById", query = "SELECT c FROM CajaMov c WHERE c.id = :id")
    , @NamedQuery(name = "CajaMov.findByFecha", query = "SELECT c FROM CajaMov c WHERE c.fecha = :fecha")
    , @NamedQuery(name = "CajaMov.findByMov", query = "SELECT c FROM CajaMov c WHERE c.mov = :mov")
    , @NamedQuery(name = "CajaMov.findByImporte", query = "SELECT c FROM CajaMov c WHERE c.importe = :importe")
    , @NamedQuery(name = "CajaMov.findBySaldo", query = "SELECT c FROM CajaMov c WHERE c.saldo = :saldo")
    , @NamedQuery(name = "CajaMov.findByComentario", query = "SELECT c FROM CajaMov c WHERE c.comentario = :comentario")
    , @NamedQuery(name = "CajaMov.findByOtro", query = "SELECT c FROM CajaMov c WHERE c.otro = :otro")})
public class CajaMov implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    private String fecha;
    @Column(name = "mov")
    private String mov;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "importe")
    private Double importe;
    @Column(name = "saldo")
    private Double saldo;
    @Column(name = "comentario")
    private String comentario;
    @Column(name = "otro")
    private Integer otro;

    public CajaMov() {
    }

    public CajaMov(Integer id) {
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

    public String getMov() {
        return mov;
    }

    public void setMov(String mov) {
        this.mov = mov;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
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
        if (!(object instanceof CajaMov)) {
            return false;
        }
        CajaMov other = (CajaMov) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.syscontrol.caja.CajaMov[ id=" + id + " - " + mov + " - " + importe + " ]";
    }

    public String toHsitorial() {
        return mov + " - $" + importe;
    }

}
